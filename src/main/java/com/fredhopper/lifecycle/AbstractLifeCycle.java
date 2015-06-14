package com.fredhopper.lifecycle;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A base implementation structure for {@link LifeCycle}.
 */
public abstract class AbstractLifeCycle implements LifeCycle {

  private final AtomicReference<State> state = new AtomicReference<State>(State.STOPPED);
  private final Collection<StateListener> stateListeners = new CopyOnWriteArrayList<>();

  @Override
  public State getState() {
    return state.get();
  }

  @Override
  public Collection<StateListener> getStateListeners() {
    return this.stateListeners;
  }

  @Override
  public void initLifeCycle() throws Exception {
    doInitLifeCycle();
    changeState(State.STOPPED, State.INITIALIZED);
  }


  @Override
  public void startLifeCycle() throws Exception {
    changeState(State.INITIALIZED, State.STARTING);
    doStartLifeCycle();
    changeState(State.STARTING, State.RUNNING);
  }

  @Override
  public void pause() throws Exception {
    doPause();
    changeState(State.RUNNING, State.PAUSED);
  }

  @Override
  public void resume() throws Exception {
    doResume();
    changeState(State.PAUSED, State.RUNNING);
  }

  @Override
  public void stopLifeCycle() throws Exception {
    changeState(State.RUNNING, State.STOPPING);
    doStopLifeCycle();
    changeState(State.STOPPING, State.STOPPED);
  }

  /**
   * Add a new {@link StateListener}
   * 
   * @param listener the {@link StateListener} to add
   */
  public void addStateListener(StateListener listener) {
    this.stateListeners.add(listener);
  }

  /**
   * Remove a registered {@link StateListener}
   * 
   * @param listener the {@link StateListener} to remove
   */
  public void removeStateListener(StateListener listener) {
    this.stateListeners.remove(listener);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
  }

  /**
   * First changes the state from a current value to a target
   * value. Second, publishes the change of state to all the
   * listeners through {@link #getStateListeners()}.
   * 
   * @param from the current state
   * @param to the new state
   * @throws Exception if the state cannot be changed or some
   *         {@link StateListener}failed to apply the same
   *         change.
   */
  protected void changeState(State from, State to) throws Exception {
    if (!this.state.compareAndSet(from, to)) {
      throw new Exception("Cannot change state from " + from + " to " + to + " for " + this);
    }
    publishState(from, to);
  }

  /**
   * Propagates a change of {@link State} to all the
   * {@link StateListener}registered with this life cycle
   * object.
   * 
   * @see #changeState(State, State)
   * 
   * @param from the old state
   * @param to the new state
   * @throws Exception if a listener fails to accept the change
   */
  protected void publishState(State from, State to) throws Exception {
    final Collection<StateListener> listeners = getStateListeners();
    synchronized (listeners) {
      for (StateListener listener : listeners) {
        listener.stateChanged(from, to);
      }
    }
  }

  /**
   * Similar to {@link #initLifeCycle()} for inherited classes.
   * 
   * @see LifeCycle#initLifeCycle()
   * @throws Exception See {@link #initLifeCycle()}
   */
  protected abstract void doInitLifeCycle() throws Exception;

  /**
   * Similar to {@link #startLifeCycle()} for inherited classes.
   * 
   * @see LifeCycle#startLifeCycle()
   * @throws Exception See {@link #startLifeCycle()}
   */
  protected abstract void doStartLifeCycle() throws Exception;

  /**
   * Similar to {@link #pause()} for inherited classes.
   * 
   * @see Resumable#pause()
   * @throws Exception See {@link #pause()}
   */
  protected void doPause() throws Exception {}

  /**
   * Similar to {@link #resume()} for inherited classes.
   * 
   * @see Resumable#resume()
   * @throws Exception See {@link #resume()}
   */
  protected void doResume() throws Exception {}

  /**
   * Similar to {@link #stopLifeCycle()} for inherited classes.
   * 
   * @see LifeCycle#stopLifeCycle()
   * @throws Exception See {@link #stopLifeCycle()}
   */
  protected abstract void doStopLifeCycle() throws Exception;
}
