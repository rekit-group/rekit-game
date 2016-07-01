package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.controller.Controller;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

/**
 * This class defines an Command which will be executed by a {@link Controller}
 * and is linked to an {@link Entity}
 *
 * @author Dominik Fuchß
 *
 */
public abstract class InputCommand implements Command {
	/**
	 * The corresponding supervisor
	 */
	protected CommandSupervisor supervisor;

	public InputCommand(CommandSupervisor supervisor) {
		if (supervisor == null) {
			throw new IllegalArgumentException("Supervisor cannot be null");
		}
		this.supervisor = supervisor;
	}

}
