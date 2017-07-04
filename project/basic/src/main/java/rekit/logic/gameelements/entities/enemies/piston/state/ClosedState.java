package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.logic.gameelements.entities.enemies.piston.IPistonForState;
import rekit.util.state.State;

/**
 *
 * The closed state of a piston.
 *
 */
public class ClosedState extends PistonState {
	/**
	 * Create closed state by piston
	 *
	 * @param piston
	 *            the piston
	 */
	public ClosedState(IPistonForState piston) {
		super(piston);
	}

	@Override
	public float getCurrentHeight() {
		return 1;
	}

	@Override
	public State getNextState() {
		return new OpeningState(this.piston);
	}

	@Override
	public long getTimerTime() {
		// if the reference to piston has not been set yet
		if (this.piston == null) {
			return 0;
		}
		return this.piston.getCalcTimeClosed();
	}

}
