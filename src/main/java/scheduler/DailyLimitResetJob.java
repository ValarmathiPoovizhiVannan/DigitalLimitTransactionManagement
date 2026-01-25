//package scheduler;
//
//import dao.AccountDao;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//public class DailyLimitResetJob implements Job {
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//        AccountDao accountDao = new AccountDao();
//        try {
//            int updatedRows = accountDao.resetDailyLimit();
//            System.out.println("DailyLimitResetJob UPDATED rows = " + updatedRows);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new JobExecutionException(e, false);
//        }
//    }
//}
