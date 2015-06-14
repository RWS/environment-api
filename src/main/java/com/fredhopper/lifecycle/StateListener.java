package com.fredhopper.lifecycle;

import java.util.EventListener;
import java.util.function.BiFunction;

/**
 * An event listener for a change of {@link State}. This is a
 * {@link FunctionalInterface} through {@link BiFunction} over
 * {@link State} parameter.
 * 
 * @author bnobakht
 */
@FunctionalInterface
public interface StateListener extends EventListener, BiFunction<State, State, Void> {

  /**
   * @param oldState
   * @param newState
   * @throws Exception
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
