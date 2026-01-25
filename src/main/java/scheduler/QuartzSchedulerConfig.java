//package scheduler;
//
//import java.io.InputStream;
//import java.util.Properties;
//
//import org.quartz.*;
//import org.quartz.impl.StdSchedulerFactory;
//
//public class QuartzSchedulerConfig {
//
//	public static void startDailyLimitResetScheduler() throws Exception {
//
//	    Properties props = new Properties();
//	    props.load(
//	        QuartzSchedulerConfig.class
//	            .getClassLoader()
//	            .getResourceAsStream("scheduler.properties")
//	    );
//
//	    Scheduler scheduler = new StdSchedulerFactory(props).getScheduler();
//
//	    JobKey jobKey = JobKey.jobKey("dailyLimitResetJob", "limitGroup");
//
//	    if (!scheduler.checkExists(jobKey)) {
//
//	        JobDetail job = JobBuilder.newJob(DailyLimitResetJob.class)
//	                .withIdentity(jobKey)
//	                .build();
//
//	        Trigger trigger = TriggerBuilder.newTrigger()
//	                .withIdentity("dailyLimitResetTrigger", "limitGroup")
//	                .withSchedule(
//	                    CronScheduleBuilder.cronSchedule("0 */1 * * * ?")
//	                )
//	                .build();
//
//	        scheduler.scheduleJob(job, trigger);
//	    }
//
//	    if (!scheduler.isStarted()) {
//	        scheduler.start();
//	    }
//	}}
