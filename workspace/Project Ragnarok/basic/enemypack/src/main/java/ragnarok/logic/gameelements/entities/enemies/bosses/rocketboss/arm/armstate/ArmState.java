package ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armstate;

import ragnarok.logic.gameelements.entities.enemies.bosses.rocketboss.arm.Arm;
import ragnarok.util.state.State;

public abstract class ArmState extends State {
	protected Arm parent;

	public ArmState(Arm parent) {
		this.parent = parent;
	}

	public float getSegmentAmount() {
		return 1;
	}
}
