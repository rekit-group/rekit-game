package edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class Life extends Entity {
	public Life() {
		super(null);
	}

	public Life(Vec startPos) {
		super(startPos);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.isHostile(element)) {
			element.addDamage(-1);
			this.addDamage(1);
		}
	}

	@Override
	public void logicLoop(float deltaTime) {
		// do nothing
	}

	@Override
	public void internalRender(Field f) {
		f.drawImage(this.getPos(), this.getSize(), "mrRekt_glasses_left.png");
	}

	@Override
	public Entity create(Vec startPos) {
		return new Life(startPos);
	}

}
