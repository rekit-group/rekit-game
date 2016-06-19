package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.FallState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.JumpState;

public class JumpCommand extends InputCommand {	
	public void apply(InputMethod inputMethod) {
		
		Entity entity = this.getEntity();
		if (inputMethod == InputMethod.PRESS) {
			if (entity.getEntityState().canJump()) {
				entity.setEntityState(new JumpState(entity));
			}
		} else {
			entity.setEntityState(new FallState(entity));
		}
		
		
	}

}
