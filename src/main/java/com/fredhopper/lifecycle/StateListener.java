package com.fredhopper.lifecycle;

import java.util.EventListener;
import java.util.function.BiFunction;

/**
 * An event listener for a change of {@link State}. This is a
 * {@link FunctionalInterface} through {@link BiFunction} over
 * {@link State} parameter.
 */
@FunctionalInterface
public interface StateListener extends EventListener, BiFunction<State, State, Void> {

  /**
   * Published a change of {@link State} to the listener
   * implementation.
   * 
   * @param oldState the previous state
   * @param newState the current state effective now
   * @throws Exception if the listener cannot accept such a
   *         change and decides to fail this state change
   */
  void stateChanged(State oldState, State newState) throws Exception;

  @Override
  default Void apply(State t, State u) {
    try {
      stateChanged(t, u);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

}
