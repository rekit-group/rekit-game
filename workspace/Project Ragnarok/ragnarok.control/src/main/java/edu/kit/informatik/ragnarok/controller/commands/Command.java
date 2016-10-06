package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.controller.Controller;

/**
 * This interface defines a command for a {@link Controller}.
 *
 * @author Dominik Fuchß
 *
 */
public interface Command {
	/**
	 * Execute the command.
	 *
	 * @param params
	 *            the parameters
	 */
	void execute(Object... params);

}
