package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class Life extends Entity {

	public Life(Vec2D startPos) {
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
	public void render(Field f) {
		f.drawImage(this.getPos(), this.getSize(), "mrRekt_glasses_left.png");
	}

}
