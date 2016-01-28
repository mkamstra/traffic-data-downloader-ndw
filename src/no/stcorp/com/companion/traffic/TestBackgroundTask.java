package no.stcorp.com.companion.traffic;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author Martijn
 *
 */
public class TestBackgroundTask implements Runnable {
  private final static Logger mLogger = Logger.getLogger(TestBackgroundTask.class);

  private int counter = 0;

  @Override
  public void run() {
    try {
      Date startDate = new Date();
      mLogger.info("Running TestBackgroundTask at " + NDWTrafficDataDownloadServlet.getCurrentTimeAsString(startDate));
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      counter++;
      mLogger.info(counter + ", running: " + formatter.format(startDate));
    } catch (Exception ex) {
      mLogger.error("Something went wrong running the test background task", ex);
    }
  }
}
