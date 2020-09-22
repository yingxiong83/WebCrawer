package cn.itcast.jobInfo.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronExpressionTest {

    //@Scheduled(cron = "0/5 * * * * *")
    public void test() {
        System.out.println("test...");
    }

}
