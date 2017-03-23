package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction;

import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.Rocket;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;

public class ArmActionRocketLauncher extends ArmAction {
	
	public ArmActionRocketLauncher(RocketBoss parent, Vec relPos) {
		super(parent, relPos);
		
		// Create option string to send rocket in right direction
		String[] opts = new String[]{Integer.toString(this.parent.getDirection().ordinal())};
		
		// Instantiate Rocket by prototype
		this.rocket = (Rocket) new Rocket().create(this.getRocketPos(), opts);
		this.parent.getScene().addGameElement(this.rocket);
		
		// Let Rocket stay still
		this.rocket.setSpeed(0);
	}
	
	private Rocket rocket;
	
	public ArmActionRocketLauncher() {
		super();
	}
	
	public void perform() {
		if (this.rocket != null) {
			this.rocket.resetSpeed();
			this.rocket = null;
		}
	}

	@Override
	public ArmAction create(RocketBoss parent, Vec relPos) {
		return new ArmActionRocketLauncher(parent, relPos);
	}
	
	private Vec getRocketPos() {
		return this.getPos().addX(this.parent.getXSignum() * 0.6f);
	}
	
	@Override
	public void internalRender(GameGrid f) {
		if (this.rocket != null) {
			this.rocket.setPos(this.getRocketPos());
		}
		
		Vec renderPos = this.getPos().addX(this.parent.getXSignum() * 0.6f);
		String imageSrc = (this.getParent().getDirection() == Direction.LEFT) ? RocketBoss.ARM_ACTION_ROCKET_LAUNCHER_SOURCE_LEFT : RocketBoss.ARM_ACTION_ROCKET_LAUNCHER_SOURCE_RIGHT;
		f.drawImage(renderPos, RocketBoss.ARM_ACTION_ROCKET_LAUNCHER_SIZE, imageSrc);
	}
}
