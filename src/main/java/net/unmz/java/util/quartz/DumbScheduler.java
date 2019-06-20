package net.unmz.java.util.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by longyun on 2019/6/20.
 */
public class DumbScheduler {
    public static void main(String[] args) throws SchedulerException {

        JobDetail jobDetail = JobBuilder.newJob(DumbJob.class)
                .withIdentity("myJob", "group1") // name "myJob", group "group1"
                .usingJobData("jobSays", "Hello World!")
                .usingJobData("myFloatValue", 3.141f)
                .build();

        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever())
                .build();

        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);//jobDetail，trigger是其参数
    }
}
