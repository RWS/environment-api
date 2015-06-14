package com.fredhopper.environment;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class KeyValueEnvironmentTest {

  @Test
  public void createDefaultEnvironmentReturnsNullValues() throws Exception {
    Environment env = Environment.createEnvironment();
    assertThat(env).isNotNull();
    assertThat(env.getApplicationName()).isNull();
    assertThat(env.getContextPath()).contains("null");
  }

  @Test(expected = NumberFormatException.class)
  public void createDefaultEnvironmentServerPort() throws Exception {
    Environment env = Environment.createEnvironment();
    env.getServerPort();
  }

  @Test
  public void createDefaultEnvironmentServerHostIsNull() throws Exception {
    Environment env = Environment.createEnvironment();
    assertThat(env.getServerHost()).isNull();
  }

}
