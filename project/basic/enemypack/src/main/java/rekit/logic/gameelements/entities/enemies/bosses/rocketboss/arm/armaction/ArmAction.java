package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction;

import rekit.config.GameConf;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.RocketBossChild;
import rekit.primitives.geometry.Vec;

public abstract class ArmAction extends RocketBossChild {
	
	private static ArmAction[] possibleArmActions = new ArmAction[]{
			new ArmActionRocketLauncher()
			};
	
	public ArmAction() {
		super();
	}
	
	public ArmAction(GameElement parent, Vec relPos) {
		super(parent, relPos);
	}
	
	public abstract void perform();
	
	
	public static ArmAction getRandomArmAction() {
		return possibleArmActions[GameConf.PRNG.nextInt(possibleArmActions.length)];
	}
	
	
}
