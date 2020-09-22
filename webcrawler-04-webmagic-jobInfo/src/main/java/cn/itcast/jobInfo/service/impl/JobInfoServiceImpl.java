package cn.itcast.jobInfo.service.impl;

import cn.itcast.jobInfo.dao.JobInfoDao;
import cn.itcast.jobInfo.pojo.JobInfo;
import cn.itcast.jobInfo.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired
    private JobInfoDao jobInfoDao;

    @Override
    @Transactional
    public void save(JobInfo jobInfo) {
        //1.先查询，判断是否有该数据，该数据是否是最新的
        JobInfo param = new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());
        List<JobInfo> jobInfoList = this.findJobInfo(param);
        if (jobInfoList.size() == 0) {
            //2.不存在或不是最新的
            jobInfoDao.saveAndFlush(jobInfo);
        }
    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        Example<JobInfo> example = Example.of(jobInfo);
        return jobInfoDao.findAll(example);
    }

    @Override
    public Page<JobInfo> findAll(int pageNum, int pageSize) {
        return jobInfoDao.findAll(PageRequest.of(pageNum - 1, pageSize));
    }
}
