package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.Canon;

public class IdleState extends State {
	
	@Override
	public State getNextState() {
		return new AimingState(parent.getScene().getPlayer());
	}
	
	@Override
	public float getTimerTime() {
		return Canon.STATE_IDLE_DURATION;
	}
	
}
