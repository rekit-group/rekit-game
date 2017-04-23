package rekit.controller.commands;

import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gameelements.entities.StateEntity;
import rekit.logic.gameelements.entities.state.FallState;
import rekit.logic.gameelements.entities.state.JumpState;

/**
 * This {@link EntityCommand} will cause a Jump of an {@link Entity}.
 *
 * @author Dominik Fuchss
 * @author Angelo Aracri
 *
 */
public class JumpCommand extends EntityCommand {
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
		StateEntity entity = this.supervisor.getEntity(this);
		if (inputMethod == InputMethod.PRESS) {
			if (entity.getEntityState().canJump()) {
				entity.setEntityState(new JumpState(entity));
			}
		} else {
			entity.setEntityState(new FallState(entity));
		}

	}

}
