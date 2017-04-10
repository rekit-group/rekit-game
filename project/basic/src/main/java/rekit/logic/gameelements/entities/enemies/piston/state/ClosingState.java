package rekit.logic.gameelements.entities.enemies.piston.state;

import rekit.util.state.State;

public class ClosingState extends PistonState {
	
	private float currentHeight;
	
	public ClosingState(long duration, State nextState) {
		super(duration, nextState);
	}
	
	@Override
	public void logicLoop() {
		this.currentHeight = this.timer.getProgress();
	}
	
	@Override
	public float getCurrentHeight() {
		return currentHeight;
	}
	
	
}
