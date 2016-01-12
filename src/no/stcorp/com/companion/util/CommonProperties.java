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

  /**
   * Singleton pattern
   * @return the common properties
   */
  public static CommonProperties getInstance() {
    if (mInstance == null) {
      mInstance = new CommonProperties();
    }
    return mInstance;
  }

  /**
   * Set the properties
   * @param pProperties the properties to be set
   */
  public void setProperties(Properties pProperties) {
    getInstance().mProperties = pProperties;
  }

  /**
   * @return the properties
   */
  public Properties getProperties() {
    return getInstance().mProperties;
  }
}
