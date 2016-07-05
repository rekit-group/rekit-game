package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.GameState;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuItem;

/**
 * This class is used for the {@link GameState#MENU MenuGameState}
 *
 * @author Matthias Schmitt
 *
 */
public class MenuCommand implements Command {
	/**
	 * The enum defines the different directions of KeyPress in a Menu Context
	 *
	 * @author Matthias Schmitt
	 *
	 */
	public enum Dir {
		UP, DOWN, BACK, SELECT
	}

	/**
	 * The command's supervisor
	 */
	private CommandSupervisor supervisor;
	/**
	 * The direction for the command
	 */
	private Dir dir;

	/**
	 * Instantiate the MenuCommand
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
		case BACK:
			item.back();
			break;
		case SELECT:
			item.select();
			break;
		default:
			break;
		}
	}

}
