package com.fredhopper.environment;

/**
 * A general notion of the mode of the runtime of the
 * application.
 */
public enum RuntimeMode {

  /**
   * Refers to a common production environment.
   */
  PRODUCTION,

  /**
   * Refers to a runtime that is used in the context of testing.
   */
  TEST,

  /**
   * Refers to a runtime that is used for development times.
   */
  DEVELOPMENT,

  /**
   * An unknown runtime. See
   * {@link Environment#getEnvironmentName()}.
   */
  UNKNOWN

  ;

  /**
   * Determine the runtime mode.
   * 
   * @param e the {@link Environment} instance
   * @return the result of {@link #fromEnvironmentName(String)}
   *         from the name of the environment
   */
  public static RuntimeMode fromEnvironment(Environment e) {
    return fromEnvironmentName(e.getEnvironmentName());
  }

  /**
   * @param name the name of the runtime mode
   * @return the resolved runtime mode or {@link #UNKNOWN} if an
   *         exception occurs.
   */
  public static RuntimeMode fromEnvironmentName(String name) {
    try {
      return valueOf(name.toUpperCase());
    } catch (Exception x) {
      return UNKNOWN;
    }
  }

}
