package cn.itcast.jobInfo.dao;

import cn.itcast.jobInfo.pojo.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobInfoDao extends JpaRepository<JobInfo,Long>, JpaSpecificationExecutor<JobInfo> {
}
