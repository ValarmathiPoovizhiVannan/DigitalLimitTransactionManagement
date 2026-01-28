package scheduler;

import dao.AccountDao; 
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DailyLimitResetJob implements Job {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DailyLimitResetJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            int updated = new AccountDao().resetDailyLimit();
            LOGGER.info("Daily limit reset completed. Rows updated={}", updated);
        } catch (Exception e) {
            throw new JobExecutionException(e, true);
        }
    }
}
