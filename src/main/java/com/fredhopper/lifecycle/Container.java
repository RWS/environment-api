package com.fredhopper.lifecycle;

/**
 * A container manages an instance of {@link LifeCycle} through
 * its stages potentially in different {@link Thread}s. It is a
 * common pattern that {@link LifeCycle} stages need to occur in
 * a different thread than the current one. Though, this is
 * <i>not</i> a hard restriction.
 * 
 * @author bnobakht
 */
public class Container extends AbstractLifeCycle {

  /**
   * A runnable that executes {@link LifeCycle#startLifeCycle()}
   * if it is not starting nor running already.
   */
  private class Bootstrap implements Runnable {
    @Override
    public void run() {
      if (getState() != State.INITIALIZED) {
        return;
      }
      try {
        lifeCycle.startLifeCycle();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * A runnable that executes {@link LifeCycle#stopLifeCycle()}
   * if it is not already stopping nor stopped.
   */
  private class Shutdownstrap implements Runnable {
    @Override
    public void run() {
      if (getState() != State.RUNNING) {
        return;
      }
      try {
        lifeCycle.stopLifeCycle();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  private final LifeCycle lifeCycle;
  private final boolean separateThreads;
  private final Thread bootstrapThread;
  private final Thread shutdownstrapThread;

  /**
   * By default, container uses separate threads for life cycle
   * stages and installs a JVM shutdown hook.See
   * {@link #Container(LifeCycle, boolean, boolean)}.
   * 
   * @param lifeCycle the managed {@link LifeCycle} object
   */
  public Container(LifeCycle lifeCycle) {
    this(lifeCycle, true, true);
  }

  /**
   * C'tor.
   * 
   * @param lifeCycle the managed {@link LifeCycle} object
   * @param separateThreads if {@code true}, the life cycle
   *        methods {@link #startLifeCycle()} and
   *        {@link #stopLifeCycle()} will be executed in
   *        separate thread each.
   * @param shutdownHook if {@code true}, a shutdown hook is
   *        installed through
   *        {@link Runtime#addShutdownHook(Thread)} to stop the
   *        managed life cycle object.
   */
  public Container(LifeCycle lifeCycle, final boolean separateThreads, final boolean shutdownHook) {
    this.lifeCycle = lifeCycle;
    this.separateThreads = separateThreads;
    if (separateThreads) {
      this.bootstrapThread = new Thread(new Bootstrap(), "bootstrap-" + toString(lifeCycle));
      this.shutdownstrapThread =
          new Thread(new Shutdownstrap(), "shutdownstrap-" + toString(lifeCycle));
    } else {
      this.bootstrapThread = null;
      this.shutdownstrapThread = null;
    }
    if (shutdownHook) {
      Runtime.getRuntime()
          .addShutdownHook(new Thread(new Shutdownstrap(), "jvm-shutdown-" + toString(lifeCycle)));
    }
  }

  @Override
  public State getState() {
    return this.lifeCycle.getState();
  }

  @Override
  protected void doInitLifeCycle() throws Exception {
    this.lifeCycle.initLifeCycle();
  }

  @Override
  protected void doStartLifeCycle() throws Exception {
    if (this.separateThreads) {
      this.bootstrapThread.start();
    } else {
      this.lifeCycle.startLifeCycle();
    }
    awaitState(State.RUNNING);
  }

  @Override
  protected void doPause() throws Exception {
    this.lifeCycle.pause();
  }

  @Override
  protected void doResume() throws Exception {
    this.lifeCycle.resume();
  }

  @Override
  protected void doStopLifeCycle() throws Exception {
    if (this.separateThreads) {
      this.shutdownstrapThread.start();
    } else {
      this.lifeCycle.stopLifeCycle();
    }
    awaitState(State.STOPPED);
  }

  @Override
  public String toString() {
    return "Container[" + toString(lifeCycle) + "]";
  }

  /**
   * Waits for the life cycle to reach the expected state
   * waiting for 1 milli-second in between. If an
   * {@link InterruptedException} occurs, basically it gives up
   * the effort.
   * 
   * @param expectedState
   * @throws Exception most likely if an
   *         {@link InterruptedException} happens during waiting
   */
  private void awaitState(State expectedState) throws Exception {
    while (this.lifeCycle.getState() != expectedState) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        // We do not know why this happened.
        // We do not know how to handle it, either.
        throw e;
      }
    }
  }

  /**
   * @param lc
   * @return
   */
  private String toString(LifeCycle lc) {
    return lc.getClass().getSimpleName() + "@" + Integer.toHexString(lc.hashCode());
  }

}
