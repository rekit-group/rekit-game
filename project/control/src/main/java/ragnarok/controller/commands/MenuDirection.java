package ragnarok.controller.commands;

import java.util.function.Consumer;

import ragnarok.logic.gui.menu.MenuItem;

/**
 * The enum defines the different directions of KeyPress in a Menu Context.
 *
 * @author Matthias Schmitt
 * @author Dominik Fuchss
 *
 */
public enum MenuDirection implements Consumer<MenuItem> {
	/**
	 * Up.
	 */
	UP(item -> item.up()),
	/**
	 * Down.
	 */
	DOWN(item -> item.down()),
	/**
	 * Left.
	 */
	LEFT(item -> item.left()),
	/**
	 * Right.
	 */
	RIGHT(item -> item.right()),
	/**
	 * Back.
	 */
	BACK(item -> item.unselect()),
	/**
	 * Select.
	 */
	SELECT(item -> item.select());
	/**
	 * The action bound to the direction.
	 */
	private final Consumer<MenuItem> action;

	/**
	 * Bind action to Dir.
	 *
	 * @param action
	 *            the action
	 */
	private MenuDirection(Consumer<MenuItem> action) {
		this.action = action;
	}

	@Override
	public void accept(MenuItem t) {
		this.action.accept(t);
	}

}
