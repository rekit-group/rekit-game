package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.util.state.State;

public class ClosedState extends PistonState {
	
	public ClosedState(long duration, State nextState) {
		super(duration, nextState);
	}
	
	@Override
	public float getCurrentHeight() {
		return 1;
	}
	
}
