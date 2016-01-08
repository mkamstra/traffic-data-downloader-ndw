/**
 * 
 */
package no.stcorp.com.companion.util;

import java.util.Properties;

/**
 * @author Martijn
 * 
 */
public class CommonProperties {

  private static CommonProperties mInstance = null;

  private Properties mProperties = null;

  private CommonProperties() {
    // Intentionally private to ensure this to be a singleton
    mProperties = new Properties();
  }

  public static CommonProperties getInstance() {
    if (mInstance == null) {
      mInstance = new CommonProperties();
    }
    return mInstance;
  }

  public void setProperties(Properties pProperties) {
    getInstance().mProperties = pProperties;
  }

  public Properties getProperties() {
    return getInstance().mProperties;
  }
}
