package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Inanimate;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class InanimateTrigger extends Inanimate {

	public InanimateTrigger(Vec pos, Vec size) {
		super(pos, size, new RGBColor(0, 0, 0));
		this.team = Team.TRIGGER;
	}

	@Override
	public void internalRender(Field f) {
		// Do nothing
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.team.isHostile(element.getTeam())) {
			this.perform();
			// destroy invisible InanimateTrigger
			this.destroy();
		}
	}

	public void perform() {

	}

	@Override
	public int getID() {
		return 70;
	}

}
