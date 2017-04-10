package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.util.state.State;

/**
 * Encapsules all timing and phase-depending information in a timeable State.
 * See {@link PistonState.getCurrentHeight} for the actual intrinsic value.
 * @author Angelo Aracri
 */
public abstract class PistonState extends State {
	
	/**
	 * The time to kept in state in milliseconds.
	 */
	private long duration;
	
	/**
	 * The next state.
	 */
	private State nextState;
	
	public PistonState(long duration, State nextState) {
		super();
		this.duration = duration;
		this.nextState = nextState;
	}
	
	@Override
	public State getNextState() {
		return this.nextState;
	}

	@Override
	public long getTimerTime() {
		return this.duration;
	}
	
	/**
	 * Sets the instance to the next state after this.
	 * @param nextState the next state
	 */
	public void setNextState(State nextState) {
		this.nextState = nextState;
	}
	/**
	 * The intrinsic state of the State, the current height of the piston between 0 and 1.
	 * @return the height of the piston in 0 to 1.
	 */
	public abstract float getCurrentHeight();
	
}
