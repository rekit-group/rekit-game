package rekit.logic.gameelements.entities.enemies.cannon.state;

import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.entities.enemies.cannon.Cannon;
import rekit.logic.gameelements.entities.enemies.cannon.CannonParticle;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.util.state.State;

/**
 * Abstract extension of {@link State} that adds functionality specific to the
 * {@link Cannon}. Each state is supposed to represent a phase of the
 * {@link Cannon Cannons} cycle.
 *
 * @author Angelo Aracri.
 */
public abstract class CannonState extends State {

	/**
	 * The parenting {@link Cannon}.
	 */
	protected Cannon parentCannon;

	/**
	 * Setter for the stored reference to the parenting {@link Cannon}.
	 *
	 * @param parentCannon
	 *            the parenting {@link Cannon} to set.
	 */
	public void setCannon(Cannon parentCannon) {
		this.parentCannon = parentCannon;
	}

	/**
	 * Template method that can be filled by concrete implementations to return
	 * a custom angle in radians, the {@link Cannon} will aim at.
	 *
	 * @return the angle in radians, the {@link Cannon} will aim to.
	 */
	public float getTargetAngle() {
		// default: return DOWN
		return 0;
	}

	/**
	 * Template method that can be filled by concrete implementations to return
	 * a custom delta in x-direction, that will be applied to the {@link Cannon
	 * Cannons} position to simulate a shaking effect.
	 *
	 * @return the x-delta for the {@link Cannon} shaking effect.
	 */
	public float getCannonShake() {
		// default: no shaking
		return 0;
	}

	/**
	 * <p>
	 * Template method that can be filled by concrete implementations to perform
	 * custom actions when a {@link CannonParticle} collided with an
	 * {@link Inanimate} or a {@link Player}.
	 * </p>
	 * <p>
	 * Is only used by {@link ShootingState}.
	 * </p>
	 */
	public void hitSomething() {
		// Do nothing
	}

}
