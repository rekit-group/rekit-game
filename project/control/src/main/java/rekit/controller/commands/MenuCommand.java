package rekit.controller.commands;

import rekit.logic.Model.GameState;
import rekit.logic.gui.menu.MenuItem;

/**
 * This class is used for the {@link GameState#MENU MenuGameState}.
 *
 * @author Matthias Schmitt
 *
 */
public class MenuCommand implements Command {

	/**
	 * The direction for the command.
	 */
	private MenuDirection dir;
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
	public MenuCommand(CommandSupervisor supervisor, MenuDirection dir) {
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
		this.dir.accept(item);
	}

}
