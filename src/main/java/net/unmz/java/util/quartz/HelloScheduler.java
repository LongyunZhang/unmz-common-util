package net.unmz.java.util.quartz;

import net.unmz.java.util.date.DateUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by longyun on 2019/6/20.
 */
public class HelloScheduler {

    public static void main(String[] args) throws SchedulerException {

        //1、创建一个jobDetail的实例，将该实例与HelloJob Class绑定
        //在创建JobDetail时，将要执行的job的类名传给JobDetail
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                .withIdentity("myJob")
                .build();

        //2、创建一个Trigger触发器的实例，定义该job立即执行
        Date startDate = DateUtils.stringToDate("2019-06-22 21:53:05");
        Date endDate = DateUtils.stringToDate("2019-06-22 21:57:05");
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger1", "group1")
                //.startNow()   //立即开始
                .startAt(startDate) //开始时间
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2) //重复的间隔2秒
                        .repeatForever() //永久重复
                        //.withRepeatCount(10) //重复10次
                )
                .endAt(endDate) //结束时间
                .build();

        //3、创建schedule实例
        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(jobDetail, trigger);//jobDetail，trigger是其参数
    }

}
