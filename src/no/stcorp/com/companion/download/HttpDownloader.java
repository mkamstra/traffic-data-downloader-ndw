package no.stcorp.com.companion.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

/**
 * A utility that downloads a file from a URL.
 * 
 * @author www.codejava.net
 * 
 */
public class HttpDownloader {
  private static final int BUFFER_SIZE = 4096;

  private final static Logger mLogger = Logger.getLogger(HttpDownloader.class);

  /**
   * Downloads a file from a URL
   * 
   * @param pFileURL
   *          HTTP URL of the file to be downloaded
   * @param pSaveDir
   *          path of the directory to save the file
   * @throws IOException
   *           Exception thrown when file not found for example
   * @return the name of the file that was save
   */
  public static Path downloadFile(String pFileURL, String pSaveDir) throws IOException {
    Path returnPath = null;
    URL url = new URL(pFileURL);
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    int responseCode = httpConn.getResponseCode();

    DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
    DateFormat dfFolder = new SimpleDateFormat("yyyy_MM_dd");
    Date dateobj = new Date();
    int startOfName = pFileURL.lastIndexOf("/");
    int endOfURL = pFileURL.substring(startOfName + 1).indexOf('.') + startOfName + 1;
    String fileURL = pFileURL.substring(0, endOfURL);
    String extensions = pFileURL.substring(endOfURL + 1);
    String fileBaseName = fileURL.substring(startOfName + 1);
    String fileOutputName = fileBaseName + "_" + df.format(dateobj) + "." + extensions;
    String dayFolderName = dfFolder.format(dateobj);

    File temp;
    try {
      System.setProperty("java.io.tmpdir", "/tmp"); // set this as Tomcat's folder has restrictions in deleting
      // Set the following in the catalina.bat file set "CATALINA_TMPDIR=c:\\tmp" instead of using the Apache temp
      // folder.
      String tmpDirString = System.getProperty("java.io.tmpdir");
      mLogger.info("Temporary directory: " + tmpDirString);
      temp = File.createTempFile(fileOutputName, ".tmp");
      mLogger.info("Temporary file created with file output name: " + fileOutputName + " at " + temp.getAbsolutePath());
      // always check HTTP response code first
      if (responseCode == HttpURLConnection.HTTP_OK) {
        // String fileName = "";
        // String disposition =
        // httpConn.getHeaderField("Content-Disposition");
        // String contentType = httpConn.getContentType();
        // int contentLength = httpConn.getContentLength();

        // opens input stream from the HTTP connection
        InputStream inputStream = httpConn.getInputStream();
        returnPath = Paths.get(temp.getAbsolutePath());

        // opens an output stream to save into file
        FileOutputStream outputStream = new FileOutputStream(temp.getAbsolutePath());

        int bytesRead = -1;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();
        mLogger.info("File downloaded to " + temp.getAbsolutePath());
      } else {
        mLogger.error("No file to download. Server replied HTTP code: " + responseCode);
      }
      httpConn.disconnect();

      FTPClient client = new FTPClient();
      FileInputStream fis = null;
      try {
        mLogger.info("Trying to connect to the FTP server");
        client.connect("snufkin");
        client.login("companion", "1d1ada");
        client.enterLocalPassiveMode();
        client.setFileType(FTP.BINARY_FILE_TYPE);
        client.changeWorkingDirectory(pSaveDir);
        boolean folderExists = client.changeWorkingDirectory(dayFolderName);
        if (!folderExists) {
          client.makeDirectory(dayFolderName);
          client.changeWorkingDirectory(dayFolderName);
        }
        fis = new FileInputStream(temp.getAbsolutePath());
        client.storeFile(fileOutputName, fis);
        client.logout();
      } catch (IOException ex) {
        mLogger.error("Something went wrong on the FTP server: " + ex.getMessage());
        mLogger.error(ex);
      } finally {
        mLogger.info("Finished uploading file to FTP server");
        try {
          if (fis != null)
            fis.close();

          temp.delete(); // remove the file from the temporary directory

          client.disconnect();
        } catch (IOException ex) {
          mLogger.error("Something went wrong disconnecting from the FTP server: " + ex.getMessage());
          mLogger.error(ex);
        }
      }
    } catch (IOException ex) {
      mLogger.error(ex);
    }


    return returnPath;
  }
}
