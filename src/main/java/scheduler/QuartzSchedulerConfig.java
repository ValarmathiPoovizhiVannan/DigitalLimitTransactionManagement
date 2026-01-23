package scheduler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzSchedulerConfig {

    public static void startDailyLimitResetScheduler() throws SchedulerException, IOException {
    	Properties props = new Properties();
    	FileInputStream fs = new FileInputStream( "C:\\Users\\USER\\Scheduling\\src\\main\\resources\\application.properties");
		props.load(	fs);
		
		Scheduler  scheduler = new StdSchedulerFactory(props).getScheduler();
        JobDetail job = JobBuilder.newJob(DailyLimitResetJob.class)
                .withIdentity("dailyLimitResetJob", "limitGroup")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dailyLimitResetTrigger", "limitGroup")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0))
                .build();

        //Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}