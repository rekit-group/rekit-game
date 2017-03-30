package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate;

import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.util.state.State;

public abstract class ArmState extends State {
	private Arm parentArm;
	
	public ArmState(Arm parentArm) {
		super();
		
		this.parentArm = parentArm;
	}
	
	public Arm getParentArm() {
		return this.parentArm;
	}

	public float getSegmentAmount() {
		return 1;
	}
}
