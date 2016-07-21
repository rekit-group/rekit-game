package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;

/**
 * Second {@link CannonState} of the {@link State} that represents the phase
 * where the {@link Cannon} aims at the {@link Player} and follows his
 * movements.
 * 
 * @author Angelo Aracri
 */
public class AimingState extends CannonState {

	/**
	 * The {@link Player} to aim at.
	 */
	private Player target;

	/**
	 * Specialized constructor that stores the reference to the {@link Player}
	 * to aim at.
	 * 
	 * @param target
	 *            the {@link Player} to aim at.
	 */
	public AimingState(Player target) {
		this.target = target;
	}

	@Override
	public float getTargetAngle() {
		return this.parentCannon.getPos().getAngleTo(this.target.getPos());
	}

	@Override
	public CannonState getNextState() {
		return new ChargingState(this.getTargetAngle());
	}

	@Override
	public float getTimerTime() {
		return Cannon.STATE_AIMING_DURATION;
	}

}
