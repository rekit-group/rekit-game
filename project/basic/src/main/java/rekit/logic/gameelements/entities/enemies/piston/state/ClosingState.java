package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.logic.gameelements.entities.enemies.piston.IPistonForState;
import rekit.util.state.State;

public class ClosingState extends PistonState {
	
	public ClosingState(IPistonForState piston) {
		super(piston);
	}

	private float currentHeight;
	
	@Override
	public void internalLogicLoop() {
		this.currentHeight = this.timer.getProgress();
	}
	
	@Override
	public float getCurrentHeight() {
		return currentHeight;
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
