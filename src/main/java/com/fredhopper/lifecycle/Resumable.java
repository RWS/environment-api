package com.fredhopper.lifecycle;

/**
 * A resumable can be paused and resumed.
 */
public interface Resumable {

  /**
   * Makes an attempt to pause the resumable.
   * 
   * @throws Exception if the resumable cannot be paused and an
   *         exception is necessary.
   */
  void pause() throws Exception;

  /**
   * Makes an attempt to resume a paused resumable.
   * 
   * @throws Exception if the resumable cannot be resumed. The
   *         exception could be also because of invalid states.
   */
  void resume() throws Exception;

}
