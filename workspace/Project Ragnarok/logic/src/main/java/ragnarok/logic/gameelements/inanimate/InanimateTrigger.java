package ragnarok.logic.gameelements.inanimate;

import ragnarok.core.GameGrid;
import ragnarok.core.GameElement;
import ragnarok.core.Team;
import ragnarok.logic.gameelements.GameElementFactory;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;

/**
 *
 * This is the default implementation of an inanimate trigger in the game.
 *
 */
public class InanimateTrigger extends Inanimate {
	/**
	 * Utility function for easy use with lambdas.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param onPerform
	 *            {@link #perform()}
	 */
	public static void createTrigger(Vec pos, Vec size, Runnable onPerform) {
		GameElementFactory.generate(new InanimateTrigger(pos, size) {
			@Override
			public void perform() {
				onPerform.run();
			}
		});
	}

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
	public void internalRender(GameGrid f) {
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
