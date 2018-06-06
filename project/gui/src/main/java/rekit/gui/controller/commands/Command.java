package rekit.gui.controller.commands;

import rekit.gui.controller.Controller;

/**
 * This interface defines a command for a {@link Controller}.
 *
 * @author Dominik Fuchss
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
