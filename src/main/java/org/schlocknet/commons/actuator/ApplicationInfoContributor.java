package org.schlocknet.commons.actuator;

import java.util.HashMap;
import java.util.Map;

import org.schlocknet.commons.model.ApplicationInfo;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

/**
 * Interfaces with the Spring-Boot Actuator API to provide information about the application
 */
public class ApplicationInfoContributor implements InfoContributor
{
  /** Hold information about the application */
  private final ApplicationInfo applicationInfo;

  /** Spring boot actuator attribute name for the application's name */
  private static final String KEY_APP_NAME = "applicationName";

  /** Spring boot actuator attribute name for the application's version */
  private static final String KEY_APP_VERSION = "version";

  /** Spring boot actuator attribute name for the fleet the application is running under */
  private static final String KEY_FLEET = "fleet";

  /** Spring boot actuator attribute name for the application's build date */
  private static final String KEY_BUILD_DATE = "buildDate";

  /**
   * Default constructor
   * @param applicationInfo object containing application information to contribute
   */
  public ApplicationInfoContributor(ApplicationInfo applicationInfo) {
    if (applicationInfo == null) {
      throw new IllegalArgumentException("Argument: \"applicationInfo\" cannot be null");
    }
    this.applicationInfo = applicationInfo;
  }

  /**
   * Makes the application information available to the Spring-Boot Actuator API
   * @param builder provided by spring boot
   */
  @Override
  public void contribute(final Info.Builder builder) {
    final Map<String, Object> appInfo = new HashMap<>();
    appInfo.put(KEY_APP_NAME, applicationInfo.getAppnName());
    appInfo.put(KEY_APP_VERSION, applicationInfo.getAppVersion());
    appInfo.put(KEY_FLEET, applicationInfo.getFleet());
    appInfo.put(KEY_BUILD_DATE, applicationInfo.getBuildDate());
    builder.withDetails(appInfo);
  }
}
