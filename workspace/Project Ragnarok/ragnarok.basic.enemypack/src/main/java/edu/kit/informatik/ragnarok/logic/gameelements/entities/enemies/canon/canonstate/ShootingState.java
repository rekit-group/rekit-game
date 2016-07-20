package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.Canon;

public class ShootingState extends State {
	
	private float angle;
	
	public ShootingState(float angle) {
		this.angle = angle;
	}
	
	public float getTargetAngle() {
		return this.angle;
	}
	
	@Override
	public State getNextState() {
		return new IdleState();
	}

	@Override
	public float getTimerTime() {
		return Canon.STATE_SHOOTING_DURATION;
	}
	
}
