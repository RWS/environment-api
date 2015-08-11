package com.fredhopper.environment;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

  @Test(expected = UnsupportedOperationException.class)
  public void umodifiableMapShouldThrowWhenPutAttempt() {
    Environment env = Environment.createEnvironment();
    env.asMap().put("key", "value");
  }

  @Test
  public void unmodifiableMapShouldContainAllKeyValuePairsFromEnvironment() {
    Map<String, String> init = new HashMap<String, String>();
    init.put("key1", "value1");
    init.put("key2", "value2");
    Environment env = Environment.createEnvironment(init);
    Map<String, String> fromEnv = env.asMap();
    for (Entry<String, String> entry : init.entrySet()) {
      assertEquals(entry.getValue(), fromEnv.get(entry.getKey()));
    }
  }

}
