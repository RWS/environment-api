package com.fredhopper.lifecycle;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * Owner of a {@link State}.
 * 
 * @see LifeCycle
 * 
 * @author bnobakht
 */
@FunctionalInterface
public interface Stateful extends Supplier<State> {

  /**
   * @return the current state
   */
  State getState();

  /**
   * @return the collection of {@link StateListener} for this.
   */
  default Collection<StateListener> getStateListeners() {
    return Collections.emptySet();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.function.Supplier#get()
   */
  default State get() {
    return getState();
  }
}
