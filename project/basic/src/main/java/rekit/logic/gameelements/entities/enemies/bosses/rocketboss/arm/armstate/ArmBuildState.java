package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate;

import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.util.state.State;
import rekit.util.state.TimeStateMachine;

public class ArmBuildState extends ArmState {

	public ArmBuildState(Arm parentArm) {
		super(parentArm);
	}

	@Override
	public void enter(TimeStateMachine parent) {
		super.enter(parent);
		this.getParentArm().createArmSegments();
	}

	@Override
	public State getNextState() {
		return new ArmActionState(getParentArm());
	}

	@Override
	public long getTimerTime() {
		return RocketBoss.ARM_STATE_TIME_BUILD;
	}

	public float getSegmentAmount() {
		return this.timer.getProgress();
	}

}
