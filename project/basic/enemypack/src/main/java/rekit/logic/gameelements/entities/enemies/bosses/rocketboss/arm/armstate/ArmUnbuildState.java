package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate;

import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.util.state.State;

public class ArmUnbuildState extends ArmState {

	public ArmUnbuildState(Arm parent) {
		super(parent);
	}

	@Override
	public State getNextState() {
		return new ArmBuildState(parent);
	}

	@Override
	public long getTimerTime() {
		return RocketBoss.ARM_STATE_TIME_BUILD;
	}

	public float getSegmentAmount() {
		return 1 - this.timer.getProgress();
	}

}
