package rekit.logic.gameelements.entities.enemies.cannon.state;

import rekit.logic.gameelements.entities.enemies.cannon.Cannon;
import rekit.util.state.State;

/**
 * First {@link CannonState} of the {@link State} that represents the phase
 * where the {@link Cannon} lazily aims downwards and does nothing.
 *
 * @author Angelo Aracri
 */
public class IdleState extends CannonState {
	/**
	 * Create IdleState.
	 */
	public IdleState() {
		super();
	}

	@Override
	public CannonState getNextState() {
		return new AimingState(this.parentCannon.getScene().getPlayer());
	}

	@Override
	public long getTimerTime() {
		return (long) (1000 * Cannon.STATE_IDLE_DURATION);
	}

}
