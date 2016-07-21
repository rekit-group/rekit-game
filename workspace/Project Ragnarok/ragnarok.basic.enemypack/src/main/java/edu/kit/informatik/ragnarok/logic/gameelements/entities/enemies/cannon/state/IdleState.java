package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;

public class IdleState extends CannonState {

	@Override
	public CannonState getNextState() {
		return new AimingState(parentCannon.getScene().getPlayer());
	}

	@Override
	public float getTimerTime() {
		return Cannon.STATE_IDLE_DURATION;
	}

}
