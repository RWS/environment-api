package com.fredhopper.lifecycle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class ContainerTest {

  @Test
  public void containerEnsuresStateTransition() throws Exception {
    TestHttpServer server = new TestHttpServer();
    Container container = new Container(server);
    container.initLifeCycle();
    assertNotNull(server.getServer());
    assertEquals(State.INITIALIZED, server.get());
    container.startLifeCycle();
    assertEquals(State.RUNNING, server.get());
    server.stopLifeCycle();
    assertEquals(State.STOPPED, server.get());
  }

  @Theory
  public void containerStatesDelegatesToManagedLifeCycle(State s) throws Exception {
    AbstractLifeCycle lc = new AbstractLifeCycle() {
      @Override
      protected void doStopLifeCycle() throws Exception {}

      @Override
      protected void doStartLifeCycle() throws Exception {}

      @Override
      protected void doInitLifeCycle() throws Exception {}
    };
    Container container = new Container(lc);
    assertEquals(lc.getState(), container.getState());
    while (lc.getState().ordinal() < s.ordinal()) {
      lc.changeState(lc.getState(), State.values()[lc.getState().ordinal() + 1]);
      assertEquals(lc.getState(), container.getState());
    }
    assertEquals(lc.getState(), container.getState());
  }

}
