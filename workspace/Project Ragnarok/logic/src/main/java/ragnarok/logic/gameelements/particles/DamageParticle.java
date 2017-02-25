package ragnarok.logic.gameelements.particles;

import ragnarok.core.GameElement;
import ragnarok.core.Team;
import ragnarok.logic.gameelements.entities.Player;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Frame;
import ragnarok.primitives.geometry.Vec;

/**
 * Regular {@link Particle} with the only difference, that it damages the
 * {@link Player} upon collision.
 *
 * @author Angelo Aracri
 */
public abstract class DamageParticle extends Particle {
	/**
	 * Constructor that sets the {@link DamageParticle DamageParticles}
	 * {@link Team} to {@link Team#ENEMY}.
	 */
	public DamageParticle() {
		super();
		this.team = Team.ENEMY;
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			element.addDamage(1);
		}
	}

	@Override
	public abstract DamageParticle clone();

	@Override
	public Frame getCollisionFrame() {
		Vec v1 = this.getPos().add(this.getSize().scalar(-0.2f));
		Vec v2 = this.getPos().add(this.getSize().scalar(0.2f));
		return new Frame(v1, v2);
	}
}
