package cn.itcast.jobInfo.task;

import cn.itcast.jobInfo.pojo.JobInfo;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Component
public class JobProcessor implements PageProcessor {

    private static final String URL = "https://search.51job.com/list/000000,000000,0000,01%252C32,9,99,java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    @Override
    public void process(Page page) {
        List<Selectable> selectableList = page.getHtml().css("div#resultList div.el").nodes();
        if (selectableList.size() == 0) {
            //访问的是详情页面
            this.saveJobInfo(page);
        } else {
            //访问的是列表页面
            for (Selectable selectable : selectableList) {
                String jobInfoUrl = selectable.links().toString();
                if (jobInfoUrl != null) {
                    page.addTargetRequest(jobInfoUrl);
                }
            }
            //获取下一页url，添加到Scheduler
            String nextPage = page.getHtml().css("div.p_in li.bk").nodes().get(1).links().toString();
            //添加到任务队列
            page.addTargetRequest(nextPage);
        }
    }

    private void saveJobInfo(Page page) {
        JobInfo jobInfo = new JobInfo();

        Html html = page.getHtml();

        jobInfo.setCompanyName(html.css("a.catn","text").toString());
        jobInfo.setCompanyAddr(html.css("p.ltype", "text").toString().split("\\u00A0")[0]);
        jobInfo.setCompanyInfo(html.css("div.tmsg","text").toString());
        jobInfo.setJobName(html.css("div.cn>h1", "text").toString());
        jobInfo.setJobAddr(html.css("p.fp","text").nodes().get(2).toString().split(":")[0]);
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        Integer[] salary = MathSalary.getSalary(html.css("div.cn>strong", "text").toString());
        jobInfo.setSalaryMin(salary[0]);
        jobInfo.setSalaryMax(salary[1]);
        jobInfo.setUrl(page.getUrl().toString());
        String str = html.css("p.ltype", "text").toString();
        jobInfo.setTime(str.substring(str.indexOf("发布") - 5, str.indexOf("发布")));

        page.putField("jobInfo",jobInfo);
    }

    //配置爬虫
    private Site site = Site.me()
            .setCharset("gbk")
            .setTimeOut(5000)
            .setRetrySleepTime(5000)
            .setRetryTimes(3);

    @Override
    public Site getSite() {
        return site;
    }

    @Autowired
    private SpringDataJpaPipeline springDataJpaPipeline;
    //开启抓取招聘信息的定时任务
    //@Scheduled(initialDelay = 1000, fixedDelay = 30 * 60 * 1000)
    public void doJobProcessor() {
        Spider.create(new JobProcessor())
                //第一次访问地址
                .addUrl(URL)
                //设置URL管理器，使用布隆过滤器去重
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000)))
                //使用10个线程爬取数据
                .thread(10)
                //添加管道，输出到mysql数据库
                .addPipeline(springDataJpaPipeline)
                //执行爬虫
                .run();
    }
}
