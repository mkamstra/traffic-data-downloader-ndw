package no.stcorp.com.companion.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerManager {

  private List<Logger> mCreatedLoggers = new ArrayList<>();

  private static LoggerManager mInstance = null;

  private String mLogPath = null;

  private LoggerManager() {
    // Intentionally private to guarantee singleton pattern
  }

  public static LoggerManager getInstance() {
    if (mInstance == null) {
      mInstance = new LoggerManager();
    }
    return mInstance;
  }

  private List<Path> listSourceFiles(Path dir) {
    List<Path> result = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{lck}")) {
      for (Path entry : stream) {
        result.add(entry);
      }
    } catch (DirectoryIteratorException ex) {
      // I/O error encounted during the iteration, the cause is an
      // IOException
      ex.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public void setLogPath(String pLogPath) {
    mLogPath = pLogPath;
    List<Path> lockFiles = listSourceFiles(Paths.get(mLogPath));
    for (Path lockFile : lockFiles) {
      try {
        System.out.println("Deleting lock file " + lockFile.toString());
        Files.delete(lockFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public Logger getLogger(Class<?> pClass) {
    try {
      String logFileName = mLogPath + File.separator + pClass.getSimpleName() + ".log";
      FileHandler fh = new FileHandler(logFileName, 1024 * 5000, 10, true);
      fh.setFormatter(new SimpleFormatter());
      fh.setLevel(Level.INFO);
      Logger logger = Logger.getLogger(pClass.getName());
      logger.addHandler(fh);
      mCreatedLoggers.add(logger);
      return logger;
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void releaseLoggerFileHandlers() {
    for (Logger logger : mCreatedLoggers) {
      for (Handler handler : logger.getHandlers()) {
        handler.close();
      }
    }
  }
}
