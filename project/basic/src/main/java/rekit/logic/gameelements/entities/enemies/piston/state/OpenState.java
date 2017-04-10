package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.util.state.State;

public class OpenState extends PistonState {
	
	public OpenState(long duration, State nextState) {
		super(duration, nextState);
	}
	
	@Override
	public float getCurrentHeight() {
		return 0;
	}
	
	
}
