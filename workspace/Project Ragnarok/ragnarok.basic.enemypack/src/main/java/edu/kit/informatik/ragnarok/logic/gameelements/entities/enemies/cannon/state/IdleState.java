package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;

/**
 * First {@link CannonState} of the {@link State} that represents the phase
 * where the {@link Cannon} lazily aims downwards and does nothing.
 *
 * @author Angelo Aracri
 */
public class IdleState extends CannonState {

	@Override
	public CannonState getNextState() {
		return new AimingState(this.parentCannon.getScene().getPlayer());
	}

	@Override
	public float getTimerTime() {
		return Cannon.STATE_IDLE_DURATION;
	}

}
