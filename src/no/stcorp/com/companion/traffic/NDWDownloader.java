package no.stcorp.com.companion.traffic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.stcorp.com.companion.download.HttpDownloader;
import no.stcorp.com.companion.util.CommonProperties;

/**
 * @author Martijn
 *
 */
public class NDWDownloader implements Runnable {
  private final static Logger mLogger = Logger.getLogger(NDWDownloader.class.getName());
  private String mNdwPath = null;
  private List<String> mNdwURL = new ArrayList<>();

  @Override
  public void run() {
    try {
      Date startDate = new Date();
      mLogger.info("Running NDW downloader at " + NDWTrafficDataDownloadServlet.getCurrentTimeAsString(startDate));
      Properties prop = CommonProperties.getInstance().getProperties();
      if (mNdwPath == null && prop.get("ndwPath") != null) {
        mNdwPath = (String) prop.get("ndwPath");
      }
      if (mNdwURL.size() == 0) {
        if (prop.get("nrOfURLs") != null) {
          int nrOfURLS = Integer.valueOf((String) prop.get("nrOfURLs"));
          for (int i = 1; i <= nrOfURLS; i++) {
            String url = (String) prop.get("ndwURL" + i);
            mNdwURL.add(url);
          }
        }
      }
      if (mNdwPath != null && mNdwURL.size() > 0) {
        for (int i = 0; i < mNdwURL.size(); i++) {
          try {
            // Download and save file
            HttpDownloader.downloadFile(mNdwURL.get(i), mNdwPath);
          } catch (IOException ex) {
            mLogger.log(Level.SEVERE, "Something went wrong downloading the NDW file from " + mNdwURL
                + " and saving it to " + mNdwPath, ex);
          }
        }
      }
    } catch (Exception ex) {
      mLogger.log(Level.SEVERE, "Something went wrong running the NDW downloader", ex);
    }
  }
}
