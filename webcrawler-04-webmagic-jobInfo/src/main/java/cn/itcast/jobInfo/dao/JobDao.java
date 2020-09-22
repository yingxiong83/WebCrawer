package cn.itcast.jobInfo.dao;

import cn.itcast.jobInfo.pojo.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

public interface JobDao extends ElasticsearchRepository<Job,Long> {

    Page<Job> findBySalaryMinBetweenAndSalaryMaxBetweenAndJobAddrAndJobNameAndJobInfo(Integer minSalary, Integer maxSalary, Integer salary, Integer integer, String jobaddr, String keyword, String s, Pageable pageable);

}
