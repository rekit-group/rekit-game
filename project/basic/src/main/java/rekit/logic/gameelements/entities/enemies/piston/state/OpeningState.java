package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.util.state.State;

public class OpeningState extends PistonState {
	
	private float currentHeight;
	
	public OpeningState(long duration, State nextState) {
		super(duration, nextState);
	}
	
	@Override
	public void logicLoop() {
		this.currentHeight = 1 - this.timer.getProgress();
	}
	
	@Override
	public float getCurrentHeight() {
		return currentHeight;
	}
	
	
}
