package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction;

import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.Rocket;
import rekit.primitives.geometry.Vec;

public class ArmActionRocketLauncher extends ArmAction {
	
	public ArmActionRocketLauncher(GameElement parent, Vec relPos) {
		super(parent, relPos);
	}
	
	public ArmActionRocketLauncher() {
		super();
	}
	
	public void perform() {
		GameElement rocket = new Rocket().create(this.getPos(), new String[]{});
		this.parent.getScene().addGameElement(rocket);
	}

	@Override
	public ArmAction create(GameElement parent, Vec relPos) {
		return new ArmActionRocketLauncher(parent, relPos);
	}
}
