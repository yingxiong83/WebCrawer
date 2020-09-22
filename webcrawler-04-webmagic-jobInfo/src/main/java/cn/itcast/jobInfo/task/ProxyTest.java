package cn.itcast.jobInfo.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

@Component
public class ProxyTest implements PageProcessor {

    //@Scheduled(cron = "0/10 * * * * *")
    public void doTest() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("123.207.57.92", 1080)));
        Spider.create(new ProxyTest())
                .addUrl("http://localhost:8080/webcrawler/index.jsp")
                .setDownloader(httpClientDownloader)
                .run();
    }


    @Override
    public void process(Page page) {
        System.out.println(page.getHtml().toString());
    }

    private Site site = Site.me();
    @Override
    public Site getSite() {
        return site;
    }
}
