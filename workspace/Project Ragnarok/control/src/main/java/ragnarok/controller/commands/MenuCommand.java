package ragnarok.controller.commands;

import java.util.function.Consumer;

import ragnarok.logic.Model.GameState;
import ragnarok.logic.gui.menu.MenuItem;

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
	 * @author Dominik Fuchss
	 *
	 */
	public enum Dir {
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
		private Dir(Consumer<MenuItem> action) {
			this.action = action;
		}

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
		this.dir.action.accept(item);
	}

}
