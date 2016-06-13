package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.gameelements.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.JumpState;

public class JumpCommand extends InputCommand {	
	public void apply() {
		Entity entity = this.getEntity();
		if (entity.getEntityState().canJump()) {
			entity.setVel(entity.getVel().setY(c.playerJumpBoost));
			entity.setEntityState(new JumpState());
		}
	}

}
