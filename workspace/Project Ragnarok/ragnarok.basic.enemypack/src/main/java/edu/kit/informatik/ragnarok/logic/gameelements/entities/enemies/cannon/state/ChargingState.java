package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;

public class ChargingState extends CannonState {
	
	protected float angle;
	private float currentShake;
	
	public ChargingState(float angle) {
		this.angle = angle;
	}
	
	@Override
	public CannonState getNextState() {
		return new ShootingState(this.getTargetAngle());
	}
	
	public float getTargetAngle() {
		return this.angle;
	}
	
	@Override
	public final float getCannonShake() {
		return currentShake;
	}
	
	public float shakeStrength() {
		return this.timer.getProgress();
	}
	
	public void logicLoop(float deltaTime) {
		super.logicLoop(deltaTime);
		this.currentShake = 0.1f * (2 * GameConf.PRNG.nextFloat() - 1) * shakeStrength();
		
	}
	
	@Override
	public float getTimerTime() {
		return Cannon.STATE_CHARGING_DURATION;
	}
}
