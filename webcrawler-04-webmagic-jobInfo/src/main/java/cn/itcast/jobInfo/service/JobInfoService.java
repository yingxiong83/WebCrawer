package cn.itcast.jobInfo.service;

import cn.itcast.jobInfo.pojo.JobInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobInfoService {

    void save(JobInfo jobInfo);

    List<JobInfo> findJobInfo(JobInfo jobInfo);

    Page<JobInfo> findAll(int pageNum, int pageSize);
}
