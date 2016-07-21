package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.Particle;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;

public class CanonParticle extends Particle {
	
	public CanonParticle() {
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
	public Particle clone() {
		return new CanonParticle();
	}
}
