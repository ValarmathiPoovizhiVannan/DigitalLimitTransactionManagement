package scheduler;

import java.io.InputStream;
import java.util.Properties;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzSchedulerConfig {

    public static void startDailyLimitResetScheduler() throws Exception {

        Properties props = new Properties();
        try (InputStream is =
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream("scheduler.properties")) {

            props.load(is);
        }

        Scheduler scheduler = new StdSchedulerFactory(props).getScheduler();
        scheduler.start();

        JobKey jobKey = JobKey.jobKey("dailyLimitResetJob", "limitGroup");
        TriggerKey triggerKey =
                TriggerKey.triggerKey("dailyLimitResetTrigger", "limitGroup");

        if (!scheduler.checkExists(jobKey)) {
            JobDetail job = JobBuilder.newJob(DailyLimitResetJob.class)
                    .withIdentity(jobKey)
                    .storeDurably(true)
                    .requestRecovery(true)
                    .build();

            scheduler.addJob(job, true); 
        }

        if (!scheduler.checkExists(triggerKey)) {

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .forJob(jobKey)
                    .startNow()
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 */1 * * * ?")
                    )
                    .build();

            scheduler.scheduleJob(trigger);
        }
    }
}
