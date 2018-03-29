package org.schlocknet.commons.model;

import lombok.Value;

@Value
public class ApplicationInfo
{
  /** The application environment/fleet */
  private final String fleet;

  /** The name of this application */
  private final String appnName;

  /** The application version number */
  private final String appVersion;

  /** The date this application was built / compiled */
  private final String buildDate;
}
