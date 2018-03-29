package org.schlocknet.commons.util;

import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang.StringUtils;
import org.schlocknet.commons.model.ApplicationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class application-environment related operations
 */
public class AppEnvironment
{
  /** Local logger instance */
  private static final Logger LOGGER = LoggerFactory.getLogger(AppEnvironment.class);

  /** Default value for unknown fields */
  public static final String UNKNOWN = "unknown";

  /** The java property where the current fleet/environment can be found */
  public static final String PROP_FLEET = "spring.profiles.active";

  /** The environment variable where the current fleet/environment can be found */
  public static final String ENV_FLEET = "FLEET";

  /** Manifest attribute used to get the application name */
  public static final String KEY_APP_NAME = "Implementation-Title";

  /** Manifest attribute used to get the application version */
  public static final String KEY_APP_VERSION = "Implementation-Version";

  /** Manifest attribute used to get the application build date */
  public static final String KEY_BUILD_DATE = "Build-Date";

  /** The allows fleet/environment values */
  public static final Set<String> FLEET_VALUES = ImmutableSet.of("local","dev","test","prod");

  /**
   * Default, private constructor
   */
  private AppEnvironment() {
    // Do Nothing
  }

  /**
   * Tries to determine which fleet/environment the application is running in and sets the fleet. By default, if no
   * fleet can be determined, it will be set to "dev"
   */
  public static void initializeFleet() {
    String fleet = System.getProperty(PROP_FLEET, System.getenv(ENV_FLEET));
    if (StringUtils.isBlank(fleet)) {
      System.out.println("Env var [FLEET] and Java Property [spring.profiles.active] was not found. "
          + "Setting default value of spring.profiles.active=dev");
      fleet = "dev";
    }
    System.out.println("Starting application using fleet: [" + fleet + "]");
    System.setProperty(PROP_FLEET, fleet);
  }

  /**
   * Attempts to extract application information from the jar manifest of the jar file that a given class resides
   * in.
   * @param clazz The class for which whose jar file is used to extract application information
   * @return An object containing application information (if the information was available)
   */
  public static ApplicationInfo creatApplicationInfoFromJarManifest(Class clazz) {
    if (null == clazz) {
      throw new IllegalArgumentException("argument: clazz cannot be null");
    }
    final String classPath = clazz.getResource(clazz.getSimpleName() + ".class").toString();

    if(!classPath.startsWith("jar")) {
      LOGGER.error("Class: [{}] does not appear to be from a jar file. Unable to load manifest", clazz);
      return new ApplicationInfo(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }

    final String manifestPath = classPath.substring(0, classPath.indexOf('!') + 1) +
        "/META-INF/MANIFEST.MF";
    try {
      final Manifest manifest = new Manifest(new URL(manifestPath).openStream());
      Attributes attr = manifest.getMainAttributes();
      return new ApplicationInfo(
          System.getProperty(PROP_FLEET, UNKNOWN),
          attr.getValue(KEY_APP_NAME),
          attr.getValue(KEY_APP_VERSION),
          attr.getValue(KEY_BUILD_DATE)
      );
    } catch (IOException ex) {
      LOGGER.error("Error while reading jar manifest for classPath {} with manifest: {}",
          classPath, manifestPath, ex);
      return new ApplicationInfo(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
    }
  }
}
