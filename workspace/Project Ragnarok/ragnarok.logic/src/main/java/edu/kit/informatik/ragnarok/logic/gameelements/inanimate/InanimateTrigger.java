package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;

/**
 *
 * This is the default implementation of an inanimate trigger in the game.
 *
 */
public class InanimateTrigger extends Inanimate {
	/**
	 * Create the trigger.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 */
	public InanimateTrigger(Vec pos, Vec size) {
		super(pos, size, new RGBAColor(0, 0, 0, 255), Team.TRIGGER);
	}

	@Override
	public void internalRender(Field f) {
		// Do nothing
	}

	@Override
	public final void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			this.perform();
			// destroy invisible InanimateTrigger
			this.destroy();
		}
	}

	/**
	 * Perform trigger-action.
	 */
	public void perform() {

	}

}
