package no.stcorp.com.companion.traffic;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import no.stcorp.com.companion.util.CommonProperties;

/**
 * A listener to the servlet so background task can start when the servlet starts.
 * 
 * Example taken from: http://www.devsumo.com/technotes/2013/10/java-web-apps-running-regular-background-tasks/
 *
 * @author companion
 */
@WebListener("application context listener")
public class BackgroundTaskManager implements ServletContextListener {

  private static final int MAXIMUM_CONCURRENT = 1;
  private static final String INIT_PARAMETER = "ndwDownloadTasks";

  private final static Logger mLogger = Logger.getLogger(BackgroundTaskManager.class);
  private CommonProperties mProperties = null;
  private ScheduledThreadPoolExecutor mExecutor = null;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      mProperties = CommonProperties.getInstance();
      Properties prop = mProperties.getProperties();
      ServletContext context = sce.getServletContext();
      String log4jConfigFile = context.getInitParameter("log4j-config-location");
      String fullPath = context.getRealPath("") + File.separator + log4jConfigFile;
      PropertyConfigurator.configure(fullPath);

      prop.load(context.getResourceAsStream("/WEB-INF/resources/config.properties"));
      // String logPath = (String) prop.get("logPath");
      mExecutor = new ScheduledThreadPoolExecutor(MAXIMUM_CONCURRENT);
      String[] jobDetails = sce.getServletContext().getInitParameter(INIT_PARAMETER).split(",(\\s)*");
      for (int i = 0; i < jobDetails.length; i += 2) {
        try {
          Runnable object = (Runnable) Class.forName(
              jobDetails[i]).newInstance();
          int interval = Integer.parseInt(jobDetails[i + 1]);
          mLogger.info("Starting NDW download");
          mExecutor.scheduleAtFixedRate(object,
              (long) (interval / 10), interval, TimeUnit.SECONDS);
        } catch (Exception ex) {
          mLogger.error("Error initializing NDW downloader" + ex.getMessage() + ex.getStackTrace());
          mLogger.error(ex);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    mLogger.info("Shutting down servlet and thereby stopping NDW download");
    mExecutor.shutdown();
  }
}
