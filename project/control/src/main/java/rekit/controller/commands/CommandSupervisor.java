package rekit.controller.commands;

import rekit.logic.IScene;
import rekit.logic.Model;
import rekit.logic.gameelements.entities.StateEntity;
import rekit.logic.gui.menu.MenuItem;

/**
 * This interface defines methods to get info from a {@link Model} and will be
 * used by {@link EntityCommand EntityCommands}.
 *
 * @author Dominik Fuchss
 *
 */
public interface CommandSupervisor {
	/**
	 * Get the entity the Command is assigned to.
	 *
	 * @param command
	 *            the command
	 * @return the entity for the command or {@code null} if none assigned
	 */
	StateEntity getEntity(Command command);

	/**
	 * Get the menu the Command is assigned to.
	 *
	 * @param command
	 *            the command
	 * @return the menu for the command or {@code null} if none assigned
	 */
	MenuItem getMenu(Command command);

	/**
	 * Get the current scene.
	 *
	 * @return the current scene
	 */
	IScene getScene();

	/**
	 * Indicates whether commands like moving a player (no menu / basic
	 * commands) allowed.
	 *
	 * @return {@code true} if allowed, {@code false} otherwise
	 */
	boolean entityCommandAllowed();
}
