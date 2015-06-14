package com.fredhopper.lifecycle;

/**
 * @author bnobakht
 */
public interface LifeCycleFactory {

  /**
   * @return
   * @throws Exception
   */
  LifeCycle createLifeCycle() throws Exception;

}
