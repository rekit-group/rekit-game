
package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.controller.Controller;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

/**
 * This class defines an Command which will be executed by a {@link Controller}
 * and is linked to an {@link Entity}
 *
 * @author Dominik Fuchß
 * @see JumpCommand
 * @see WalkCommand
 */
public abstract class InputCommand implements Command {

	/**
	 * This enum is used to indicate a press or release state of a key
	 *
	 * @author Dominik Fuchß
	 *
	 */
	public enum InputMethod {
		/**
		 * Key Pressed
		 */
		PRESS,
		/**
		 * Key Released
		 */
		RELEASE
	}

	/**
	 * The corresponding supervisor
	 */
	protected final CommandSupervisor supervisor;

	/**
	 * Instantiate the Command
	 *
	 * @param supervisor
	 *            the supervisor
	 */
	public InputCommand(CommandSupervisor supervisor) {
		if (supervisor == null) {
			throw new IllegalArgumentException("Supervisor cannot be null");
		}
		this.supervisor = supervisor;
	}

	@Override
	public final void execute(Object... params) {
		if (params.length != 1 || params[0].getClass() != InputMethod.class) {
			throw new IllegalArgumentException("Arguments not valid for input command");
		}
		this.execute((InputMethod) params[0]);
	}

	/**
	 * Execute the command
	 *
	 * @param inputMethod
	 *            the key state
	 */
	abstract void execute(InputMethod inputMethod);

}
