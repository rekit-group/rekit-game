package rekit.controller.commands;

import rekit.controller.Controller;

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
