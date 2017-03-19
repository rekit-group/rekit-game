package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate;

import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.util.state.State;
import rekit.util.state.TimeStateMachine;

public class ArmActionState extends ArmState {

	public ArmActionState(Arm parent) {
		super(parent);
	}

	@Override
	public void enter(TimeStateMachine parent) {
		super.enter(parent);
	}

	@Override
	public State getNextState() {
		return new ArmUnbuildState(parent);
	}

	@Override
	public long getTimerTime() {
		return RocketBoss.ARM_STATE_TIME_ACTION;
	}

	public float getSegmentAmount() {
		// Return 1 for all segments
		return 1;
	}

}
