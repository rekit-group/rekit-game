package rekit.logic.gameelements.entities.enemies.cannon.state;

import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.entities.enemies.cannon.Cannon;
import rekit.util.state.State;

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
	private GameElement target;

	/**
	 * Specialized constructor that stores the reference to the {@link Player}
	 * to aim at.
	 *
	 * @param target
	 *            the {@link Player} to aim at.
	 */
	public AimingState(GameElement target) {
		super();
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
	public long getTimerTime() {
		return (long) (1000 * Cannon.STATE_AIMING_DURATION);
	}

}
