package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.logic.gameelements.entities.enemies.piston.IPistonForState;
import rekit.util.state.State;

public class OpenState extends PistonState {
	
	
	public OpenState(IPistonForState piston) {
		super(piston);
	}
	
	@Override
	public float getCurrentHeight() {
		return 0;
	}

	@Override
	public State getNextState() {
		return new ClosingState(this.piston);
	}

	@Override
	public long getTimerTime() {
		// if the reference to piston has not been set yet
		if (this.piston == null) {
			return 0;
		} 
		return this.piston.getCalcTimeOpen();
	}
	
	
}
