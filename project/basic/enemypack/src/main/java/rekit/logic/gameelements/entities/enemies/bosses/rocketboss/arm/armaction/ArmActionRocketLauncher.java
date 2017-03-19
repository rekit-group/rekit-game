package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction;

import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.Rocket;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
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
	
	
	@Override
	public void internalRender(GameGrid f) {
		f.drawRectangle(this.getPos().addX(-0.2f), RocketBoss.ARM_ACTION_ROCKET_LAUNCHER_SIZE, RocketBoss.ARM_ACTION_ROCKET_LAUNCHER_COLOR);
	}
}
