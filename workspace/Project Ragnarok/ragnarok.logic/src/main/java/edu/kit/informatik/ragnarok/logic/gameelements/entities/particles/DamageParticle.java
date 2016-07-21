package edu.kit.informatik.ragnarok.logic.gameelements.entities.particles;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Frame;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

public class DamageParticle extends Particle {
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
	public Particle clone() {
		return new DamageParticle();
	}
	
	@Override
	public Frame getCollisionFrame() {
		Vec v1 = this.getPos().add(this.getSize().scalar(-0.2f));
		Vec v2 = this.getPos().add(this.getSize().scalar(0.2f));
		return new Frame(v1, v2);
	}
}
