//package scheduler;
//
//import jakarta.servlet.ServletContextEvent;
//import jakarta.servlet.ServletContextListener;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class QuartzStartupListener implements ServletContextListener {
//
//    private static final Logger LOGGER =
//            LoggerFactory.getLogger(QuartzStartupListener.class);
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        try {
//            QuartzSchedulerConfig.startDailyLimitResetScheduler();
//            LOGGER.info("Quartz Scheduler started successfully");
//        } catch (Exception e) {
//            LOGGER.error("Quartz Scheduler failed to start", e);
//        }
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        LOGGER.info("Application stopped");
//    }
//}
