package rekit.gui.controller.commands;

import rekit.config.GameConf;
import rekit.logic.IScene;

/**
 * This {@link Command} will pass the users attack indicator into the current
 * {@link IScene}.
 *
 * @author Dominik Fuchss
 * @see GameConf#CONTINUOS_ATTACK
 */
public final class AttackCommand implements Command {

	private final CommandSupervisor supervisor;
	private boolean active = false;

	/**
	 * Create the command by its supervisor.
	 *
	 * @param supervisor
	 *            the supervisor
	 */
	public AttackCommand(CommandSupervisor supervisor) {
		this.supervisor = supervisor;
	}

	@Override
	public void execute(Object... params) {
		if (params.length != 1 || params[0].getClass() != InputMethod.class) {
			throw new IllegalArgumentException("Arguments not valid for input command");
		}
		InputMethod method = (InputMethod) params[0];
		boolean newActivationState = method == InputMethod.PRESS;
		if (newActivationState != this.active) {
			this.active = newActivationState;
			if (this.active) {
				this.supervisor.getScene().attack();
			}

		} else if (this.active && GameConf.CONTINUOS_ATTACK) {
			this.supervisor.getScene().attack();
		}
	}

}
