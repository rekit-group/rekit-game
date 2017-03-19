package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm;

import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction.ArmAction;
import rekit.primitives.geometry.Vec;

/**
 * Represents any visual child of the RocketBoss, whose position is relative
 * to a parent.
 * 
 * @author angeloaracri
 */
public abstract class RocketBossChild {
	
	protected GameElement parent;
	protected Vec relPos;
	
	public RocketBossChild (GameElement parent, Vec relPos) {
		this.parent = parent;
		this.relPos = relPos;
	}
	
	public RocketBossChild() {
		// prototype instantiation constructor
	}
	
	public GameElement getParent() {
		return this.parent;
	}
	
	public Vec getPos() {
		return this.parent.getPos().add(this.relPos);
	}
	
	
	
	public abstract RocketBossChild create(GameElement parent, Vec relPos);
}
