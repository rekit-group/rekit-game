package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate;

import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.util.state.State;
import rekit.util.state.TimeStateMachine;

public class ArmIdleState extends ArmState {

	public ArmIdleState(Arm parentArm) {
		super(parentArm);
	}

	@Override
	public State getNextState() {
		return new ArmBuildState(getParentArm());
	}
	
	@Override
	public void enter(TimeStateMachine parent) {
		super.enter(parent);
		this.getParentArm().getParent().moveToNextPosition();
	}

	@Override
	public long getTimerTime() {
		return RocketBoss.ARM_STATE_TIME_IDLE;
	}

	public float getSegmentAmount() {
		return 0;
	}

}
