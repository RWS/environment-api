package com.fredhopper.lifecycle;

/**
 * @author bnobakht
 */
public interface Resumable {

  /**
   * @throws Exception
   */
  void pause() throws Exception;

  /**
   * @throws Exception
   */
  void resume() throws Exception;

}
