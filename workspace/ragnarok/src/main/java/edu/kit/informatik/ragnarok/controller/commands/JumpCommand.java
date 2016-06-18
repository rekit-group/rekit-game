package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.JumpState;

public class JumpCommand extends InputCommand {	
	public void apply() {
		Entity entity = this.getEntity();
		if (entity.getEntityState().jump()) {
			entity.setEntityState(new JumpState(entity));
		}
	}

}
