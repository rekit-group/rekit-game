package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction;

import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.bosses.rocketboss.RocketBoss;
import rekit.logic.gameelements.entities.enemies.cannon.Cannon;
import rekit.primitives.geometry.Vec;

/**
 * This concrete {@link ArmAction} spawns and de-spawns a modified {@link Cannon}.
 * @author Angelo Aracri
 */
public class ArmActionCannon extends ArmAction {
	
	/**
	 * Reference to the internally used {@link Cannon}.
	 */
	private GameElement innerCannon;
	
	public ArmActionCannon(RocketBoss parent, Vec relPos) {
		super(parent, relPos);
		
		innerCannon = new Cannon().create(this.getPos(), new String[]{});
		innerCannon.setSize(innerCannon.getSize().scalar(0.7f));
		this.parent.getScene().addGameElement(innerCannon);
	}
	
	public ArmActionCannon() {
		super();
	}
	
	@Override
	public void perform() {
		// Do nothing here, as the Cannon acts on its own.
	}
	
	@Override
	public void tearDown() {
		this.innerCannon.destroy();
	}

	@Override
	public ArmAction create(RocketBoss parent, Vec relPos) {
		return new ArmActionCannon(parent, relPos);
	}
	
	@Override
	public void logicLoop(float calcX, float deltaX) {
		this.innerCannon.setPos(this.getPos().addY(0.4f));
	}

}
