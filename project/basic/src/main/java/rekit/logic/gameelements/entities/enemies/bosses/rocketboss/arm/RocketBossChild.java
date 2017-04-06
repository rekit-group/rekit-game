package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm;

import rekit.core.GameGrid;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.primitives.geometry.Vec;

/**
 * Represents any visual child of the RocketBoss, whose position is relative to
 * a parent.
 *
 * @author angeloaracri
 */
public abstract class RocketBossChild {

	protected RocketBoss parent;
	protected Vec relPos;

	public RocketBossChild(RocketBoss parent, Vec relPos) {
		this.parent = parent;
		this.relPos = relPos;
	}

	public RocketBossChild() {
		// prototype instantiation constructor
	}

	public RocketBoss getParent() {
		return this.parent;
	}

	public Vec getPos() {
		return this.parent.getPos().add(this.relPos);
	}

	public abstract void logicLoop(float calcX, float deltaX);

	public abstract void internalRender(GameGrid f);

	public abstract RocketBossChild create(RocketBoss parent, Vec relPos);
}
