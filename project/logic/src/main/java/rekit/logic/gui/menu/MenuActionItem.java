package rekit.logic.gui.menu;

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
	private final Runnable selectAction;
	private final boolean isOnlyAction;

	/**
	 * Create MenuActionItem.
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text
	 * @param selectAction
	 *            the action
	 * @param isOnlyAction
	 *            indicates whether this item can't be selected for further
	 *            actions and just invoke the action or not
	 */
	public MenuActionItem(IScene scene, String text, Runnable selectAction, boolean isOnlyAction) {
		super(scene, text);
		this.selectAction = selectAction;
		this.isOnlyAction = isOnlyAction;
	}

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
	public MenuActionItem(IScene scene, String text, Runnable selectAction) {
		this(scene, text, selectAction, false);
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
	public MenuActionItem(IScene scene, Vec size, String text, Runnable selectAction) {
		this(scene, text, selectAction);
		this.size = size;
	}

	@Override
	public void select() {
		this.selected = true;
		if (this.isOnlyAction) {
			this.unselect();
		}
		this.selectAction.run();
	}

}
