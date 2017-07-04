package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.logic.gameelements.entities.enemies.piston.IPistonForState;
import rekit.util.state.State;

/**
 *
 * The closing state of a piston.
 *
 */
public class ClosingState extends PistonState {
	private float currentHeight;

	/**
	 * Create closing state by piston
	 *
	 * @param piston
	 *            the piston
	 */
	public ClosingState(IPistonForState piston) {
		super(piston);
	}

	@Override
	public void internalLogicLoop() {
		this.currentHeight = this.timer.getProgress();
	}

	@Override
	public float getCurrentHeight() {
		return this.currentHeight;
	}

	@Override
	public State getNextState() {
		return new ClosedState(this.piston);
	}

	@Override
	public long getTimerTime() {
		// if the reference to piston has not been set yet
		if (this.piston == null) {
			return 0;
		}
		return this.piston.getCalcTimeTransistion();
	}

}
