package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction;

import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.RocketBossChild;
import rekit.primitives.geometry.Vec;

public abstract class ArmAction extends RocketBossChild {
	
	public ArmAction(GameElement parent, Vec relPos) {
		super(parent, relPos);
	}
	
	public abstract void perform();
}
