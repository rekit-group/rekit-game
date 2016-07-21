package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;

public class IdleState extends State {
	
	@Override
	public State getNextState() {
		return new AimingState(parent.getScene().getPlayer());
	}
	
	@Override
	public float getTimerTime() {
		return Cannon.STATE_IDLE_DURATION;
	}
	
}
