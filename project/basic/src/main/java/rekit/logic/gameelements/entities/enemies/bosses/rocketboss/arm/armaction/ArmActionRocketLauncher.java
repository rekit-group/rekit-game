package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction;

import rekit.core.GameGrid;
import rekit.logic.gameelements.entities.enemies.Rocket;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;

/**
 * This concrete {@link ArmAction} launches a {@link Rocket}.
 * @author Angelo Aracri
 */
public class ArmActionRocketLauncher extends ArmAction {
	
	private Rocket rocket;
	
	/**
	 * Standard Constructor used to fulfill the prototype pattern
	 */
	public ArmActionRocketLauncher() {
		super();
	}
	
	/**
	 * Constructor that spawns the {@link Rocket} and sets its speed to 0.
	 * @param parent the parenting {@link RocketBoss}
	 * @param relPos the position of this {@link ArmAction} relative to the {@link RocketBoss}
	 */
	public ArmActionRocketLauncher(RocketBoss parent, Vec relPos) {
		super(parent, relPos);
		
		// Create option string to send rocket in right direction
		String[] opts = new String[]{Integer.toString(this.parent.getDirection().ordinal()), "0.8F"};
		
		// Instantiate Rocket by prototype
		this.rocket = (Rocket) new Rocket().create(this.getRocketPos(), opts);
		this.parent.getScene().addGameElement(this.rocket);
		
		// Let Rocket stay still
		this.rocket.setSpeed(0);
	}
	
	@Override
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
	
	/**
	 * Calculates the position where to spawn a {@link Rocket}.
	 * @return the absolute position of the {@link Rocket}
	 */
	private Vec getRocketPos() {
		return this.getPos().addX(this.parent.getXSignum() * 0.6f);
	}
	
	@Override
	public void internalRender(GameGrid f) {
		if (this.rocket != null) {
			this.rocket.setPos(this.getRocketPos());
		}
		
		Vec renderPos = this.getPos().addX(this.parent.getXSignum() * 0.6f);
		String imageSrc = (this.getParent().getDirection() == Direction.LEFT) ? RocketBoss.ROCKET_LAUNCHER_SOURCE_LEFT : RocketBoss.ROCKET_LAUNCHER_SOURCE_RIGHT;
		f.drawImage(renderPos, RocketBoss.ROCKET_LAUNCHER_SIZE, imageSrc);
	}
}
