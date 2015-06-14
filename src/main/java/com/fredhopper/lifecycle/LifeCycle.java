package com.fredhopper.lifecycle;

/**
 * A {@link LifeCycle} is an abstraction that
 * <ul>
 * <li>owns a {@link State},
 * <li><i>potentially</i> owns a collection of
 * {@link StateListener} to publish changes of state to,
 * <li>changes its state through life cycle methods
 * {@link #initLifeCycle()}, {@link #startLifeCycle()}, and
 * {@link #stopLifeCycle()}.
 * </ul>
 * In addition, a {@link LifeCycle} is {@link Resumable} in
 * order to support {@link Resumable#pause()} and
 * {@link Resumable#resume()} behavior.
 * 
 * @see State
 * @see Resumable
 * 
 * @author bnobakht
 */
public interface LifeCycle extends Resumable, Stateful {

  /**
   * Initializes the life cycle before invoking
   * {@link #startLifeCycle()}. It is expected that
   * {@link #getState()} should be {@link State#INITIALIZED}
   * after completion of this method.
   * 
   * @throws Exception expected to be thrown if the
   *         initialization cannot be complete.
   */
  void initLifeCycle() throws Exception;

  /**
   * Starts the life cycle. It is expected that
   * {@link #getState()} should be {@link State#STARTING} while
   * the method is not finished and {@link State#RUNNING} on
   * successful completion.
   * 
   * @throws Exception should be thrown if the starting of the
   *         life cycle object cannot complete.
   */
  void startLifeCycle() throws Exception;

  /**
   * Stops the life cycle. It is expected that
   * {@link #getState()} should be {@link State#STOPPING} while
   * the method is not finished and {@link State#STOPPED} on
   * successful completion.
   * 
   * @throws Exception should be thrown if the stopping fails.
   *         It is recommended to perform clean-up guidelines.
   *         In addition, it is recommended to still fail in a
   *         state such that killing JVM process does not have a
   *         corruptive effect on the state.
   */
  void stopLifeCycle() throws Exception;

}
