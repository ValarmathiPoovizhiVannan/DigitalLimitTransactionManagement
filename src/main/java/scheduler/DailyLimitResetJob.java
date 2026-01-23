package scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.AccountDao;

import java.math.BigDecimal;

public class DailyLimitResetJob implements Job {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DailyLimitResetJob.class);

    private static final BigDecimal DEFAULT_DAILY_LIMIT =
            new BigDecimal("5000");

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        try {
            AccountDao accountDao = new AccountDao();
            int updatedRows = accountDao.resetDailyLimitForAllAccounts(DEFAULT_DAILY_LIMIT);

            LOGGER.info("Daily limit reset completed. Accounts updated: {}", updatedRows);

        } catch (Exception e) {
            LOGGER.error("Daily limit reset failed", e);
            throw new JobExecutionException(e);
        }
    }
}