package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate;

import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import rekit.util.state.State;

public abstract class ArmState extends State {
	protected Arm parent;

	public ArmState(Arm parent) {
		this.parent = parent;
	}

	public float getSegmentAmount() {
		return 1;
	}
}
