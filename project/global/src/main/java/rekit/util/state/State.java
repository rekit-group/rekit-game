package rekit.util.state;

import rekit.primitives.time.Timer;

/**
 * <p>
 * Abstract State whose concrete implementations that a {@link TimeStateMachine}
 * can be in.
 * </p>
 * <p>
 * Concrete classes may add functionality to
 * {@link #enter(TimeStateMachine parent)} (that is called upon entering this
 * state), {@link #leave()} (that is called upon leaving this state) and
 * {@link #logicLoop()} (that is called periodically by the
 * {@link TimeStateMachine}).
 * </p>
 * <p>
 * For automatic switching states, the implementations must supply a
 * {@link #getTimerTime()} that specifies how (in millis) the
 * {@link TimeStateMachine} is supposed to be in this state as well as a
 * {@link #getNextState()} that returns the next state to enter after that time.
 * </p>
 *
 * @author Angelo Aracri
 */
public abstract class State {

	/**
	 * A reference to the parenting {@link TimeStateMachine} that is used to
	 * switch {@link State States}.
	 */
	protected TimeStateMachine parent;

	/**
	 * {@link Timer} that is used to keep track of how much time this
	 * {@link State} is remaining before it switches to the next {@link State}.
	 */
	protected Timer timer;
	/*
	 * The time of the last invoke of {@link #logicLoop()}.
	 */
	// private long lastTime = GameTime.getTime();
	
	
	/**
	 * Constructor that initializes the {@link #timer} using
	 * {@link #getTimerTime()}.
	 */
	public State() {
		this.timer = new Timer(this.getTimerTime());
	}

	/**
	 * Method that is called by the {@link TimeStateMachine} upon entering this
	 * {@link State}.
	 *
	 * @param parent
	 *            the parenting {@link TimeStateMachine} that handles this (and
	 *            probably other) {@link State States}.
	 */
	public void enter(TimeStateMachine parent) {
		this.parent = parent;
	}

	/**
	 * Method that is called by the {@link TimeStateMachine} upon leaving this
	 * {@link State}.
	 */
	public void leave() {
		// Do nothing
	}

	/**
	 * Method that is periodically called by the {@link TimeStateMachine}.
	 *
	 */
	public void logicLoop() {

		// long deltaTime = GameTime.getTime() - this.lastTime;
		// this.lastTime += deltaTime;
		this.timer.logicLoop();
		// this.timer.removeTime(deltaTime);
		if (this.timer.timeUp()) {
			this.parent.nextState();
		}
		
		this.internalLogicLoop();
	}
	
	/**
	 * Method that may or may not be implemented by a concrete state to perform
	 * periodic actions
	 */
	protected void internalLogicLoop() {
		
	}

	/**
	 * Must supply a fully instantiated {@link State} that the parenting
	 * {@link TimeStateMachine} will enter after this {@link State} is out of
	 * time as specified in {@link #getTimerTime()}.
	 *
	 * @return the next {@link State} to enter.
	 */
	public abstract State getNextState();

	/**
	 * Must supply the time in millis how long the {@link TimeStateMachine} is
	 * supposed to be in this {@link State} before switching to the next as
	 * specified in {@link #getNextState()}.
	 *
	 * @return the time in millis to stay in this {@link State}.
	 */
	public abstract long getTimerTime();
}
