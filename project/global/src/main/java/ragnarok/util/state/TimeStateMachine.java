package ragnarok.util.state;

/**
 * <p>
 * Simple StateMachine that is meant to be extended or decorated to achieve
 * state dependent behavior where every state has a fixed duration.
 * </p>
 * <p>
 * The method {@link #logicLoop()} must be periodically called to be able to let
 * the {@link State States} check for its time to be up. For this cause, the
 * {@link State} must supply a {@link State#getTimerTime()} as well as a
 * {@link State#getNextState()}.
 * </p>
 *
 * @author Angelo Aracri
 */
public class TimeStateMachine {

	/**
	 * The {@link State}, the {@link TimeStateMachine} is currently in.
	 */
	protected State currentState;

	/**
	 * The constructor that initializes the {@link TimeStateMachine} with a
	 * given {@link State initialState}.
	 *
	 * @param initialState
	 *            the initial {@link State}.
	 */
	public TimeStateMachine(State initialState) {
		this.currentState = initialState;
		initialState.enter(this);
	}

	/**
	 * Method that must be periodically called in order to check for the
	 * {@link State States} time to be up.
	 *
	 */
	public void logicLoop() {
		this.getState().logicLoop();
	}

	/**
	 * <p>
	 * Switches the {@link State} to the next as specified by
	 * {@link State#getNextState()}.
	 * </p>
	 * <p>
	 * Calls both the old {@link State States} {@link State#leave()} as well as
	 * the new ones {@link State#enter(TimeStateMachine parent)}.
	 * </p>
	 */
	public void nextState() {
		this.nextState(this.currentState.getNextState());
	}

	/**
	 * <p>
	 * More specific version of {@link #nextState()} that enables for switching
	 * to a given {@link State} that must not necessarily be specified by the
	 * current {@link State States} {@link State#getNextState()} or that can be
	 * used to alter the {@link State States} before entering them..
	 *
	 * @param nextState
	 *            the new {@link State} to enter.
	 */
	protected void nextState(State nextState) {
		this.currentState.leave();
		nextState.enter(this);
		this.currentState = nextState;
	}

	/**
	 * Getter for the {@link State} the {@link TimeStateMachine} is currently
	 * the in.
	 *
	 * @return the current {@link State}.
	 */
	public State getState() {
		return this.currentState;
	}
}
