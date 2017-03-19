package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate;

import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.util.state.State;

public class ArmUnbuildState extends ArmState {

	public ArmUnbuildState(Arm parentArm) {
		super(parentArm);
	}

	@Override
	public State getNextState() {
		return new ArmBuildState(getParentArm());
	}

	@Override
	public long getTimerTime() {
		return RocketBoss.ARM_STATE_TIME_UNBUILD;
	}

	public float getSegmentAmount() {
		return 1 - this.timer.getProgress();
	}

}
