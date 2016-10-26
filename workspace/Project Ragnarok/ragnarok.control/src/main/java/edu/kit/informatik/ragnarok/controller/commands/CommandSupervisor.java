package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;

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
	Entity getEntity(Command command);

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
