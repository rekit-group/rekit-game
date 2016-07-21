package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon;


import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.DamageParticle;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.Particle;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;

public class CannonParticle extends DamageParticle {
	
	private Cannon parent;
	
	public CannonParticle(Cannon parent) {
		super();
		this.parent = parent;
	}
	
	@Override
	public Particle clone() {
		return new CannonParticle(this.parent);
	}
	
	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		super.reactToCollision(element, dir);
		Team t = element.getTeam();
		if (t == Team.INANIMATE || t == Team.PLAYER) {
			this.parent.hitSomething();
		}
	}
}
