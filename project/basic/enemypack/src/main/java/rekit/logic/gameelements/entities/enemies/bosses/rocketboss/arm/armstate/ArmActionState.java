package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate;

import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.Rocket;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction.ArmAction;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction.ArmActionRocketLauncher;
import rekit.primitives.geometry.Vec;
import rekit.util.state.State;
import rekit.util.state.TimeStateMachine;

public class ArmActionState extends ArmState {
	
	private boolean hasPerformedAction = false;
	
	public ArmActionState(Arm parentArm) {
		super(parentArm);
	}
	
	@Override
	public void enter(TimeStateMachine parent) {
		super.enter(parent);
	}
	
	protected void internalLogicLoop() {
		if (!hasPerformedAction && timer.getProgress() >= getParentArm().getActionProgressThreshold()) {
			System.out.println("PERF " + this.getParentArm().armAction.getPos());
			this.getParentArm().armAction.perform();
			this.hasPerformedAction = true;
		}
		
	}

	@Override
	public State getNextState() {
		return new ArmUnbuildState(getParentArm());
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
