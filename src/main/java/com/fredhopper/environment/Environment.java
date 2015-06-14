package com.fredhopper.environment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Supplier;

import com.google.common.base.StandardSystemProperty;

/**
 * A general abstract presentation of an environment of an
 * application's runtime in JVM. The environment is a read-only
 * source of key-value pairs in the environment.
 */
public interface Environment {

  /**
   * A unique ID that is initialized for an {@link Environment}
   * the first time the environment is initialized. If multiple
   * {@link Environment} instances are used in the same JVM,
   * this is not reliable and should not be used.
   */
  String ID = UUID.randomUUID().toString();

  /**
   * A Java {@link Properties} file that can be used to define
   * an {@link Environment} instance with system property
   * {@value}.
   */
  String ENVIRONMENT_FILE = "environment.file";

  /**
   * The name that is given to the environment instance; e.g.
   * <code>development</code>, <code>test</code>,
   * <code>production</code>.
   */
  String ENVIRONMENT_NAME = "environment.name";

  /**
   * Key of the name of the application: {@value}.
   */
  String APPLICATION_NAME = "application.name";

  /**
   * The application's root directory suffix: {@value}.
   */
  String ROOT_SUFFIX = ".root";

  /**
   * The application's root directory for logs suffix: {@value}.
   */
  String LOG_ROOT_SUFFIX = ".logs.root";

  /**
   * The default log files extension: {@value}.
   */
  String LOG_FILE_SUFFIX = ".log";

  /**
   * The default access log files extension: {@value}.
   */
  String ACCESS_LOG_FILE_SUFFIX = "-access.log";

  /**
   * The pattern suffix to use for Apache Log4j for rolling log
   * files: {@value}.
   */
  String LOG_FILE_ROTATE_PATTERN_SUFFIX = ".%d{yyyy-MM-dd}";

  /**
   * The default suffix for access log files rolling: {@value}.
   */
  String ACCESS_LOG_FILE_ROTATE_PATTERN = ".yyyy-MM-dd";

  /**
   * The default log pattern: {@value}.
   */
  String LOG_PATTERN = "[%d] [%level] [%thread] %msg (%logger{1}:%L)%n%throwable";

  /**
   * The default value: {@value}
   */
  String AUDIT_LOG_PATTERN = "%d{ISO8601} %level %msg%n";

  /**
   * 
   */
  String SERVER_HOST_SUFFIX = ".server.host";

  /**
   * 
   */
  String SERVER_PORT_SUFFIX = ".server.port";

  /**
   * The default password to use for shutting down a local HTTP
   * server that the application might be running.
   */
  String SERVER_SHUTDOWN_TOKEN = "Fredhopper";

  /**
   * The ID of the environment.
   * 
   * @return the id of the environment
   */
  default String getId() {
    return ID;
  }

  /**
   * The name of the environment. The default value is
   * <code>development</code>
   * 
   * @see RuntimeMode
   * @return the name of the environment
   */
  default String getEnvironmentName() {
    return "development";
  }

  /**
   * The application name.
   * 
   * @return the name of the application configured as
   *         <code>application.name</code>.
   */
  String getApplicationName();

  /**
   * The path to application root.
   * 
   * @return the root of the application configured as
   *         <code>${application.name}.root</code>. Can be
   *         <code>null</code>.
   */
  Path getApplicationRoot();

  /**
   * The path to application logs root. By default, the
   * implementations might choose to fall back to
   * <code>${application.name}.root/logs</code>.
   * 
   * @return the root of the application logs. Can be
   *         <code>null</code>.
   */
  Path getApplicationLogsRoot();

  /**
   * The log file name. By default, the implementations might
   * choose to fall back to <code>${application.name}.log</code>
   * 
   * @return the name of the log files for the application.
   */
  String getLogFileName();

  /**
   * The rotating log file pattern.
   * 
   * @return the rotating log file pattern potentially include a
   *         date/time pattern.
   */
  String getRotatingLogFilePattern();

  /**
   * The access log file name. By default, implementations might
   * choose to use {@link #getLogFileName()}-
   * <code>access.log</code>
   * 
   * @return the log file name for access logs of the
   *         application.
   */
  String getAccessLogFileName();

  /**
   * The rotating access log file name.
   * 
   * @return the name pattern for rotating access log files.
   */
  String getRotatingAccessLogFileName();

  /**
   * The context path. By default, it falls back to
   * <code>/${application.name}</code>.
   * 
   * @return the context path at which the application is
   *         accessible through HTTP.
   */
  String getContextPath();

  /**
   * The host name of the server.
   * 
   * @return the host name of the server running the
   *         application.
   */
  String getServerHost();

  /**
   * The port number of the server.
   * 
   * @return the port number of the server running the
   *         application.
   */
  int getServerPort();

  /**
   * The value of a key.
   * 
   * @param key the environment key
   * @return the value for the key or <code>null</code>
   */
  default String getValue(String key) {
    return getValue(key, () -> null);
  }

  /**
   * The value of a key with a supplier.
   * 
   * @param key the environment key
   * @param supplier a {@link Supplier} for the key in case
   *        there is no value available
   * @return the value for the key or delegates to the supplier
   */
  String getValue(String key, Supplier<String> supplier);

  /**
   * The description of the environment.
   * 
   * @return a formatted {@link StringBuilder} to describe the
   *         environment instance.
   */
  default StringBuilder getDescription() {
    final String newLine = StandardSystemProperty.LINE_SEPARATOR.value();
    final StringBuilder sb = new StringBuilder("");
    sb.append(newLine);
    try (final Formatter f = new Formatter(sb)) {
      final String format = "%1$24s     %2$s" + newLine;
      sb.append(newLine);
      f.format(format, "OS",
          StandardSystemProperty.OS_NAME.value() + " " + StandardSystemProperty.OS_ARCH.value()
              + " " + StandardSystemProperty.OS_VERSION.value());
      f.format(format, "Java Runtime", System.getProperty("java.runtime.name") + " "
          + System.getProperty("java.runtime.version"));
      f.format(format, "Java VM",
          StandardSystemProperty.JAVA_VM_NAME.value() + " "
              + StandardSystemProperty.JAVA_VM_VENDOR.value() + " "
              + StandardSystemProperty.JAVA_VM_VERSION.value());
      f.format(format, "Java Class Ver.", StandardSystemProperty.JAVA_CLASS_VERSION.value());
      f.format(format, "Environment ID", getId());
      f.format(format, "Environment Ver.",
          Environment.class.getPackage().getImplementationVersion());
      f.format(format, "Environment Name", getEnvironmentName());
      f.format(format, "Runtime Mode", RuntimeMode.fromEnvironment(this));
      f.format(format, "Application", getApplicationName());
      f.format(format, "Application Root", getApplicationRoot());
      f.format(format, "Application Logs", getApplicationLogsRoot());
      f.format(format, "Server Host", getServerHost());
      f.format(format, "Server Port", getServerPort());
      sb.append(newLine);
    } catch (Exception e) {
      // Ignore
    }
    return sb;
  }

  /**
   * Creates an environment populating in order and overriding:
   * <ul>
   * <li>{@link System#getenv()}
   * <li>{@link System#getProperties()}
   * <li>If {@link #ENVIRONMENT_FILE} points to a readable file
   * and an instance of {@link Properties} can be loaded. Throw
   * an {@link IllegalArgumentException} if an
   * {@link IOException} occurs.
   * </ul>
   * 
   * @return an instance of {@link Environment}
   */
  static Environment createEnvironment() {
    return createEnvironment(new HashMap<>());
  }

  /**
   * Create an environment.
   * 
   * @param environment the base environment to use. Values will
   *        be over-written first by {@link System#getenv()},
   *        then {@link System#getProperties()}, and then
   *        finally if {@link #ENVIRONMENT_FILE} provides any.
   * @return the created {@link Environment} instance.
   */
  static Environment createEnvironment(Map<String, String> environment) {
    final Map<String, String> env = new HashMap<>();
    env.putAll(environment);
    env.putAll(new HashMap<String, String>(System.getenv()));
    final Properties systemProperties = System.getProperties();
    systemProperties.stringPropertyNames()
        .forEach(p -> env.put(p, systemProperties.getProperty(p)));
    final String environmentFilePath = systemProperties.getProperty(ENVIRONMENT_FILE, null);
    final Properties environmentFileProperties = new Properties();
    if (environmentFilePath != null && Files.isReadable(Paths.get(environmentFilePath))) {
      try (InputStream is = Files.newInputStream(Paths.get(environmentFilePath))) {
        environmentFileProperties.load(is);
      } catch (IOException e) {
        throw new IllegalArgumentException("Cannot load environment from " + environmentFilePath,
            e);
      }
    }
    environmentFileProperties.stringPropertyNames()
        .forEach(p -> env.put(p, environmentFileProperties.getProperty(p)));
    return new KeyValueEnvironment(env);
  }

}
