package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuItem;

public interface CommandSupervisor {
	Entity getEntity(Command command);

	MenuItem getMenu(Command command);
}
