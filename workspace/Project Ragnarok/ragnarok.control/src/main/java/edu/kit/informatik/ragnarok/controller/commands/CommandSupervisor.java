package edu.kit.informatik.ragnarok.controller.commands;

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
	Entity getEntity(Command command);

	MenuItem getMenu(Command command);
}
