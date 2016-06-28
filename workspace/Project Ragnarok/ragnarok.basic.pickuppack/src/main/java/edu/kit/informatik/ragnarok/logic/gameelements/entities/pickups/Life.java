package edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Pickup;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class Life extends Pickup {
	public Life() {
		super();
	}

	public Life(Vec startPos) {
		super(startPos);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.team.isHostile(element.getTeam())) {
			element.addDamage(-1);
			this.addDamage(1);
		}
	}

	@Override
	public void internalRender(Field f) {
		f.drawImage(this.getPos(), this.getSize(), "mrRekt_glasses_left.png");
	}

	@Override
	public Entity create(Vec startPos) {
		return new Life(startPos);
	}

	@Override
	public int getID() {
		return 20;
	}

}
