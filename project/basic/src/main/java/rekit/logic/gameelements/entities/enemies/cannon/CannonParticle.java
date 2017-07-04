package rekit.logic.gameelements.entities.enemies.cannon;

import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.logic.gameelements.particles.DamageParticle;
import rekit.logic.gameelements.particles.Particle;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;

/**
 * {@link Particle} that extends {@link DamageParticle} that is extended by
 * calling a {@link Cannon Cannons} method {@link Cannon#hitSomething()} upon
 * colliding with an {@link Inanimate} or {@link Player}.
 *
 * @author Angelo Aracri
 */
public final class CannonParticle extends DamageParticle {

	/**
	 * The parenting {@link Cannon} whose {@link Cannon#hitSomething()} will be
	 * called upon a collision with an {@link Inanimate} or {@link Player}.
	 */
	private Cannon parent;

	/**
	 * Constructor that saves the reference of the given parenting
	 * {@link Cannon} whose {@link Cannon#hitSomething()} will be called upon a
	 * collision with an {@link Inanimate} or {@link Player}.
	 *
	 * @param parent
	 *            the parenting {@link Cannon} to message.
	 */
	public CannonParticle(Cannon parent) {
		super();
		this.parent = parent;
	}

	@Override
	public DamageParticle create() {
		return new CannonParticle(this.parent);
	}

	@Override
	public void collidedWithSolid(Frame collision, Direction dir) {
		this.parent.hitSomething();
	}
}
