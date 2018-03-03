package rekit.logic.gui.menu;

import org.fuchss.tools.lambda.VoidFunction;

import rekit.logic.IScene;
import rekit.primitives.geometry.Vec;

/**
 *
 * This class realizes a {@link MenuItem} with an attached Action.
 *
 */
public class MenuActionItem extends MenuItem {
	/**
	 * The action.
	 */
	private final VoidFunction selectAction;

	/**
	 * Create MenuActionItem.
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text
	 * @param selectAction
	 *            the action
	 */
	public MenuActionItem(IScene scene, String text, VoidFunction selectAction) {
		super(scene, text);
		this.selectAction = selectAction;
	}

	/**
	 * Create MenuActionItem.
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text
	 * @param size
	 *            the size
	 * @param selectAction
	 *            the action
	 */
	public MenuActionItem(IScene scene, Vec size, String text, VoidFunction selectAction) {
		this(scene, text, selectAction);
		this.size = size;
	}

	@Override
	public void select() {
		this.selected = true;
		this.unselect();
		this.selectAction.execute();

	}
}
