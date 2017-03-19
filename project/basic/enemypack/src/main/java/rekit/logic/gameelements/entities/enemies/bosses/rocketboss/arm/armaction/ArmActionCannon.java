package rekit.logic.gameelements.entities.enemies.bosses.rocketboss.arm.armaction;

import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.enemies.cannon.Cannon;
import rekit.primitives.geometry.Vec;

public class ArmActionCannon extends ArmAction {
	
	private GameElement innerCannon;
	
	public ArmActionCannon(GameElement parent, Vec relPos) {
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
		// Do nothing here
	}
	
	@Override
	public void tearDown() {
		this.innerCannon.destroy();
	}

	@Override
	public ArmAction create(GameElement parent, Vec relPos) {
		return new ArmActionCannon(parent, relPos);
	}
	
	@Override
	public void logicLoop(float calcX, float deltaX) {
		this.innerCannon.setPos(this.getPos().addY(0.4f));
	}

}
