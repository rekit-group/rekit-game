package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.GameState;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;

/**
 * This class is used for the {@link GameState#MENU MenuGameState}.
 *
 * @author Matthias Schmitt
 *
 */
public class MenuCommand implements Command {
	/**
	 * The enum defines the different directions of KeyPress in a Menu Context.
	 *
	 * @author Matthias Schmitt
	 *
	 */
	public enum Dir {
		/**
		 * Up.
		 */
		UP,
		/**
		 * Down.
		 */
		DOWN,
		/**
		 * Left.
		 */
		LEFT,
		/**
		 * Right.
		 */
		RIGHT,
		/**
		 * Back.
		 */
		BACK,
		/**
		 * Select.
		 */
		SELECT
	}

	/**
	 * The direction for the command.
	 */
	private Dir dir;
	/**
	 * The supervisor.
	 */
	protected final CommandSupervisor supervisor;

	/**
	 * Instantiate the MenuCommand.
	 *
	 * @param supervisor
	 *            the supervisor
	 * @param dir
	 *            the direction
	 */
	public MenuCommand(CommandSupervisor supervisor, Dir dir) {
		this.supervisor = supervisor;
		this.dir = dir;
	}

	@Override
	public final void execute(Object... params) {
		if (params.length != 1 || params[0].getClass() != InputMethod.class) {
			throw new IllegalArgumentException("Arguments not valid for input command");
		}
		this.execute((InputMethod) params[0]);
	}

	/**
	 * Execute the command.
	 *
	 * @param inputMethod
	 *            the key state
	 */
	public void execute(InputMethod inputMethod) {
		if (inputMethod != InputMethod.RELEASE) {
			return;
		}
		MenuItem item = this.supervisor.getMenu(this);
		switch (this.dir) {
		case UP:
			item.up();
			break;
		case DOWN:
			item.down();
			break;
		case LEFT:
			item.left();
			break;
		case RIGHT:
			item.right();
			break;
		case BACK:
			item.unselect();
			break;
		case SELECT:
			item.select();
			break;
		default:
			throw new Error("This is impossible: Direction cannot be null");
		}
	}

}
