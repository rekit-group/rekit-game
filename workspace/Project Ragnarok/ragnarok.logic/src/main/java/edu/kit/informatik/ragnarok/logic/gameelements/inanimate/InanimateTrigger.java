package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;

public class InanimateTrigger extends Inanimate {

	public InanimateTrigger(Vec pos, Vec size) {
		super(pos, size, new RGBAColor(0, 0, 0, 255), Team.TRIGGER);
	}

	@Override
	public void internalRender(Field f) {
		// Do nothing
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			this.perform();
			// destroy invisible InanimateTrigger
			this.destroy();
		}
	}

	public void perform() {

	}

}
