package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.DamageParticle;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.Particle;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Frame;

/**
 * {@link Particle} that extends {@link DamageParticle} that is extended by
 * calling a {@link Cannon Cannons} method {@link Cannon#hitSomething()} upon
 * colliding with an {@link Inanimate} or {@link Player}.
 * 
 * @author Angelo Aracri
 */
public class CannonParticle extends DamageParticle {

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
	public Particle clone() {
		return new CannonParticle(this.parent);
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		this.parent.hitSomething();
	}
}
