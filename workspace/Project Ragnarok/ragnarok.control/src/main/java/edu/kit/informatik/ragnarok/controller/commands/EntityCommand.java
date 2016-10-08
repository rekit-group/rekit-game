
package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.controller.Controller;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

/**
 * This class defines an Command which will be executed by a {@link Controller}
 * and is linked to an {@link Entity}.
 *
 * @author Dominik Fuchß
 * @see JumpCommand
 * @see WalkCommand
 */
public abstract class EntityCommand implements Command {

	/**
	 * The corresponding supervisor.
	 */
	protected final CommandSupervisor supervisor;

	/**
	 * Instantiate the Command.
	 *
	 * @param supervisor
	 *            the supervisor
	 */
	public EntityCommand(CommandSupervisor supervisor) {
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
		if (this.supervisor.entityCommandAllowed()) {
			this.execute((InputMethod) params[0]);
		}
	}

	/**
	 * Execute the command.
	 *
	 * @param inputMethod
	 *            the key state
	 */
	abstract void execute(InputMethod inputMethod);

}
