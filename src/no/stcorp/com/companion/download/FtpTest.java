package no.stcorp.com.companion.download;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public class FtpTest {

  public static void main(String[] args) {
    String tempDir = System.getProperty("java.io.tmpdir");
    System.out.println(tempDir);
    FTPClient client = new FTPClient();
    FileInputStream fis = null;
    String temp =
        "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.0\\temp\\gebeurtenisinfo.xml_2016_01_08_09_55_44_729.gz5366770304459519841.tmp";
    String fileOutputName = "gebeurtenisinfo.xml_2016_01_08_09_55_44_729.gz";
    try {
      System.out.println("Trying to connect to the FTP server");
      client.connect("snufkin");
      client.login("companion", "1d1ada");
      // String saveFilePath = pSaveDir + File.separator + fileOutputName;
      System.out.println("Working directory: " + client.printWorkingDirectory());
      System.out.println("Number of files at server: " + client.listNames().length);
      for (String fileName : client.listNames()) {
        System.out.print(fileName);
      }
      client.changeWorkingDirectory("Projects//companion//downloadedData//NDW//2016_01_08//");
      System.out.println("Working directory: " + client.printWorkingDirectory());
      System.out.println("Number of files at server: " + client.listNames().length);
      System.out.println("Number of files at server: " + client.listFiles().length);
      // fis = new FileInputStream(temp);
      // client.storeFile(fileOutputName, fis);
      client.logout();
    } catch (IOException ex) {
      System.err.println("Something went wrong on the FTP server: " + ex.getMessage());
      ex.printStackTrace();
    } finally {
      System.out.println("Finished uploading file to FTP server");
      try {
        if (fis != null)
          fis.close();

        client.disconnect();
      } catch (IOException ex) {
        System.err.println("Something went wrong disconnecting from the FTP server: " + ex.getMessage());
        ex.printStackTrace();
      }
    }

  }

}
