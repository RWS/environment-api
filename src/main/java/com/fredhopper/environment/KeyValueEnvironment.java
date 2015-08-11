package com.fredhopper.environment;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

/**
 * An implementation of {@link Environment} using an instance of
 * {@link Map}. For all the {@link Path} values, this
 * implementation does <i>not</i> provide any guarantee that
 * they exist or created.
 */
final class KeyValueEnvironment implements Environment {

  /**
   * The default name for the directory containing application
   * logs.
   */
  private static final String DEFAULT_LOGS_DIRECTORY_NAME = "logs";

  private final Map<String, String> environment;
  private final String name;
  private final String application;
  private final Path applicationRoot;
  private final Path applicationLogs;

  /**
   * Ctor.
   *
   * @param env the provided default default environment values
   * @see #KeyValueEnvironment(Map, String)
   */
  public KeyValueEnvironment(Map<String, String> env) {
    this(env, env.getOrDefault(APPLICATION_NAME, null));
  }

  /**
   * Ctor.
   *
   * @param env the provided default environment values
   * @param applicationName the name of the application
   */
  public KeyValueEnvironment(Map<String, String> env, String applicationName) {
    this.application = applicationName;
    this.environment = Collections.unmodifiableMap(env);
    this.name = this.environment.getOrDefault(ENVIRONMENT_NAME, null);
    final String envAppRoot = this.environment.getOrDefault(applicationName + ROOT_SUFFIX, null);
    this.applicationRoot = envAppRoot == null ? null : Paths.get(envAppRoot).toAbsolutePath();
    final String logsPath = this.environment.getOrDefault(applicationName + LOG_ROOT_SUFFIX, null);
    this.applicationLogs = logsPath == null
        ? this.applicationRoot == null ? null
            : this.applicationRoot.resolve(DEFAULT_LOGS_DIRECTORY_NAME)
        : Paths.get(logsPath).toAbsolutePath();
  }

  @Override
  public String getEnvironmentName() {
    if (this.name != null) {
      return this.name;
    }
    return Environment.super.getEnvironmentName();
  }

  @Override
  public String getApplicationName() {
    return this.application;
  }

  @Override
  public Path getApplicationRoot() {
    return this.applicationRoot;
  }

  @Override
  public Path getApplicationLogsRoot() {
    return this.applicationLogs;
  }

  @Override
  public String getLogFileName() {
    return (this.application + LOG_FILE_SUFFIX).intern();
  }

  @Override
  public String getRotatingLogFilePattern() {
    return (this.application + LOG_FILE_SUFFIX + LOG_FILE_ROTATE_PATTERN_SUFFIX).intern();
  }

  @Override
  public String getAccessLogFileName() {
    return (this.application + ACCESS_LOG_FILE_SUFFIX).intern();
  }

  @Override
  public String getRotatingAccessLogFileName() {
    return (this.application + ACCESS_LOG_FILE_SUFFIX + ACCESS_LOG_FILE_ROTATE_PATTERN).intern();
  }

  @Override
  public String getContextPath() {
    return ("/" + application).intern();
  }

  @Override
  public String getServerHost() {
    return getValue(application + SERVER_HOST_SUFFIX);
  }

  @Override
  public int getServerPort() {
    try {
      return Integer.parseInt(getValue(application + SERVER_PORT_SUFFIX));
    } catch (NumberFormatException e) {
      throw e;
    }
  }

  @Override
  public String getValue(String key, Supplier<String> supplier) {
    String value = this.environment.get(key);
    if (value != null) {
      return value;
    }
    return supplier.get();
  }

  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + "[" + getId() + "]";
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public Map<String, String> asMap() {
    return Collections.unmodifiableMap(environment);
  }

}
