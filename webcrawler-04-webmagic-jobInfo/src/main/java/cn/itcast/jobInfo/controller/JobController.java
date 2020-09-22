package cn.itcast.jobInfo.controller;

import cn.itcast.jobInfo.dao.JobDao;
import cn.itcast.jobInfo.pojo.ResultPage;
import cn.itcast.jobInfo.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    @Autowired
    private JobService jobService;

    @RequestMapping("/search")
    public ResultPage search(String salary, String jobaddr, String keyword, Integer page) {
        ResultPage resultPage = jobService.findAll(salary,jobaddr,keyword,page);
        return resultPage;
    }

}
