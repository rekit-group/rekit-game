package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.logic.gameelements.entities.enemies.piston.IPistonForState;
import rekit.util.state.State;

/**
 *
 * The opening state of a piston.
 *
 */
public class OpeningState extends PistonState {

	private float currentHeight;

	/**
	 * Create opening state by piston
	 *
	 * @param piston
	 *            the piston
	 */
	public OpeningState(IPistonForState piston) {
		super(piston);
	}

	@Override
	public void internalLogicLoop() {
		this.currentHeight = 1 - this.timer.getProgress();
	}

	@Override
	public float getCurrentHeight() {
		return this.currentHeight;
	}

	@Override
	public State getNextState() {
		return new OpenState(this.piston);
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
