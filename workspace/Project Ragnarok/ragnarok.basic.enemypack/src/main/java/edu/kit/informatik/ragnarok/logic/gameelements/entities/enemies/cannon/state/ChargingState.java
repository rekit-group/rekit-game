package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;

/**
 * Third {@link CannonState} of the {@link State} that represents the phase
 * where the {@link Cannon} is about to shoot but does not re-aim. Additionally,
 * a shaking effect starts to increase to create a tense feeling ;)
 *
 * @author Angelo Aracri
 */
public class ChargingState extends CannonState {

	/**
	 * Current angle, the {@link Cannon} is supposed to be aiming at.
	 */
	protected float angle;

	/**
	 * Current x-delta that will be used on the {@link Cannon Cannons} position
	 * to simulate a shaking effect.
	 */
	private float currentShake;

	/**
	 * Constructor that saves the angle in radians to aim at throughout this
	 * whole {@link State}.
	 *
	 * @param angle
	 *            the angle
	 */
	public ChargingState(float angle) {
		this.angle = angle;
	}

	@Override
	public CannonState getNextState() {
		return new ShootingState(this.getTargetAngle());
	}

	@Override
	public float getTargetAngle() {
		return this.angle;
	}

	@Override
	public final float getCannonShake() {
		return this.currentShake;
	}

	/**
	 * Method whose return value will be multiplied to the shaking effects
	 * x-delta to effectively control its strength. The value should be between
	 * 0 and 1.
	 *
	 * @return the value to control the strength of the {@link Cannon Cannons}
	 *          shaking effect.
	 */
	public float shakeStrength() {
		return this.timer.getProgress();
	}

	@Override
	public void logicLoop(float deltaTime) {
		super.logicLoop(deltaTime);
		this.currentShake = Cannon.MAX_SHAKING * (2 * GameConf.PRNG.nextFloat() - 1) * this.shakeStrength();

	}

	@Override
	public float getTimerTime() {
		return Cannon.STATE_CHARGING_DURATION;
	}
}
