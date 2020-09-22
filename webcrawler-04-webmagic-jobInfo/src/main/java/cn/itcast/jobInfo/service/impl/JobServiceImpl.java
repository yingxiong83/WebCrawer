package cn.itcast.jobInfo.service.impl;

import cn.itcast.jobInfo.dao.JobDao;
import cn.itcast.jobInfo.pojo.Job;
import cn.itcast.jobInfo.pojo.ResultPage;
import cn.itcast.jobInfo.service.JobService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobDao jobDao;

    @Override
    public void save(Job job) {
        jobDao.save(job);
    }

    @Override
    public void saveAll(List<Job> jobList) {
        jobDao.saveAll(jobList);
    }

    @Override
    public ResultPage findAll(String salary, String jobaddr, String keyword, Integer page) {
        /*
            处理参数
         */
        String[] str = salary.split("-");
        Integer minSalary;
        Integer maxSalary;
        if ("*".equals(str[0])) {
            minSalary = 0;
        } else {
            minSalary = Integer.parseInt(str[0]) * 10000;
        }
        if ("*".equals(str[1])) {
            maxSalary = 100000000;
        } else {
            maxSalary = Integer.parseInt(str[1]) * 10000;
        }

        if (StringUtils.isBlank(jobaddr)) {
            jobaddr = "*";
        }
        if (StringUtils.isBlank(keyword)) {
            keyword = "*";
        }

        //查询
        Page<Job> jobPage = jobDao.findBySalaryMinBetweenAndSalaryMaxBetweenAndJobAddrAndJobNameAndJobInfo(minSalary, maxSalary, minSalary, maxSalary,
                jobaddr, keyword, keyword, PageRequest.of(page - 1, 10));

        ResultPage resultPage = new ResultPage();
        resultPage.setRows(jobPage.getContent());
        int totalPage = (int) Math.ceil((double) jobPage.getTotalElements() / 10.0);
        resultPage.setPageTotal(totalPage);

        return resultPage;
    }
}
