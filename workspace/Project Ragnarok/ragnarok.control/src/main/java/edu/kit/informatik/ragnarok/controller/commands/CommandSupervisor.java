package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;

/**
 * This interface defines methods to get infos from a {@link Model} and will be
 * used by {@link InputCommand InputCommands}
 *
 * @author Dominik Fuchß
 *
 */
public interface CommandSupervisor {
	/**
	 * Get the entity the Command is assigned to
	 * 
	 * @param command
	 *            the command
	 * @return the entity for the command or {@code null} if none assigned
	 */
	Entity getEntity(Command command);

	/**
	 * Get the menu the Command is assigned to
	 * 
	 * @param command
	 *            the command
	 * @return the menu for the command or {@code null} if none assigned
	 */
	MenuItem getMenu(Command command);

	/**
	 * Get the current scene
	 * 
	 * @return the current scene
	 */
	IScene getScene();
}
