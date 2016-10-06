package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.FallState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.JumpState;

/**
 * This {@link InputCommand} will cause a Jump of an {@link Entity}.
 *
 * @author Dominik Fuchß
 * @author Angelo Aracri
 *
 */
public class JumpCommand extends InputCommand {
	/**
	 * Create the Command.
	 *
	 * @param supervisor
	 *            the supervisor
	 */
	public JumpCommand(CommandSupervisor supervisor) {
		super(supervisor);
	}

	@Override
	public void execute(InputMethod inputMethod) {
		Entity entity = this.supervisor.getEntity(this);
		if (inputMethod == InputMethod.PRESS) {
			if (entity.getEntityState().canJump()) {
				entity.setEntityState(new JumpState(entity));
			}
		} else {
			entity.setEntityState(new FallState(entity));
		}

	}

}
