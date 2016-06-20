package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.FallState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.JumpState;

/**
 * This {@link InputCommand} will cause a Jump of an {@link Entity}
 *
 * @author Dominik Fuchß
 * @author Angelo Aracri
 *
 */
public class JumpCommand extends InputCommand {
	@Override
	public void execute(InputMethod inputMethod) {
		if (inputMethod == InputMethod.PRESS) {
			if (this.entity.getEntityState().canJump()) {
				this.entity.setEntityState(new JumpState(this.entity));
			}
		} else {
			this.entity.setEntityState(new FallState(this.entity));
		}

	}

}
