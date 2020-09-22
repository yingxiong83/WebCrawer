package cn.itcast.jobInfo.service;

import cn.itcast.jobInfo.pojo.Job;
import cn.itcast.jobInfo.pojo.ResultPage;

import java.util.List;

public interface JobService {

    void save(Job job);

    void saveAll(List<Job> jobList);

    ResultPage findAll(String salary, String jobaddr, String keyword, Integer page);
}
