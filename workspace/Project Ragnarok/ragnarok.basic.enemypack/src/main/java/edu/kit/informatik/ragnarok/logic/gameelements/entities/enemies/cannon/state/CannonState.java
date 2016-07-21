package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;
import edu.kit.informatik.ragnarok.logic.state.State;

public abstract class CannonState extends State {

	protected Cannon parentCannon;

	public void setCannon(Cannon parentCannon) {
		this.parentCannon = parentCannon;
	}

	public float getTargetAngle() {
		// default: return DOWN
		return 0;
	}

	public float getCannonShake() {
		// default: no shaking
		return 0;
	}

	public void hitSomething() {
		// Do nothing
	}

}
