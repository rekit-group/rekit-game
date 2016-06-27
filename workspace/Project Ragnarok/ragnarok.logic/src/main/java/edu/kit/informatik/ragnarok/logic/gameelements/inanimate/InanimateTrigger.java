package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public abstract class InanimateTrigger extends Inanimate {

	public InanimateTrigger(Vec pos, Vec size) {
		super(pos, size, new RGBColor(0, 0, 0));
	}

	@Override
	public final void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			this.performTrigger();
		}
	}

	public void performTrigger() {

	}

}
