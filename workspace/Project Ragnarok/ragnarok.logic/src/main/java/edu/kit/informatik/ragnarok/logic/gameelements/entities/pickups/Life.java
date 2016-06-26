package edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class Life extends Entity {
	public Life() {
		super(Team.INANIMATE, null);
	}

	public Life(Vec2D startPos) {
		super(Team.INANIMATE, startPos);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			element.addDamage(-1);
			this.addDamage(1);
		}
	}

	@Override
	public void logicLoop(float deltaTime) {
		// do nothing
	}

	@Override
	public void internRender(Field f) {
		f.drawImage(this.getPos(), this.size, "mrRekt_glasses_left.png");
	}

	@Override
	public Entity create(Vec2D startPos) {
		return new Life(startPos);
	}

}
