package edu.kit.informatik.ragnarok.logic.gui.menu;

import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

/**
 *
 * This class realizes a {@link MenuItem} with an attached Action.
 *
 */
public class MenuActionItem extends MenuItem {
	/**
	 * The action.
	 */
	private Runnable selectAction;

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
	public MenuActionItem(IScene scene, Vec size, String text, Runnable selectAction) {
		this(scene, text, selectAction);
		this.size = size;
	}

	@Override
	public void select() {
		this.selected = true;
		this.selectAction.run();
	}

}
