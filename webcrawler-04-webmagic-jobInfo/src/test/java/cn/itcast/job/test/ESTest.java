package cn.itcast.job.test;

import cn.itcast.jobInfo.Application;
import cn.itcast.jobInfo.dao.JobDao;
import cn.itcast.jobInfo.dao.JobInfoDao;
import cn.itcast.jobInfo.pojo.Job;
import cn.itcast.jobInfo.pojo.JobInfo;
import cn.itcast.jobInfo.service.JobInfoService;
import cn.itcast.jobInfo.service.JobService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private JobInfoDao jobInfoDao;

    @Autowired
    private JobDao jobDao;

    /**
     * 不要用，不好用，id属性出错，手动创建
     */
    @Test
    public void createIndex() {
        elasticsearchTemplate.createIndex(Job.class);
        elasticsearchTemplate.putMapping(Job.class);
    }

    /**
     * 将mysql数据库的招聘信息装到索引库
     *      查询mysql数据库，保存到es
     */
    @Test
    public void dbToEs() {
        int pageNum = 1;
        do {
            Page<JobInfo> page = jobInfoService.findAll(pageNum, 200);
            List<JobInfo> jobInfoList = page.getContent();
            if (jobInfoList.size() == 0) {
                break;
            }
            List<Job> jobList = new ArrayList<>();
            for (JobInfo jobInfo : jobInfoList) {
                Job job = new Job();
                BeanUtils.copyProperties(jobInfo, job);
                jobList.add(job);
            }
            jobService.saveAll(jobList);
            pageNum++;
        } while (true);
    }

    @Test
    public void test() {
        Page<JobInfo> page = jobInfoDao.findAll(PageRequest.of(1, 10));
        System.out.println(page.getTotalPages());
    }

    @Test
    public void test1() {
        Page<Job> page = jobDao.findAll(PageRequest.of(1,10));
        System.out.println(page.getTotalPages());
    }


}
