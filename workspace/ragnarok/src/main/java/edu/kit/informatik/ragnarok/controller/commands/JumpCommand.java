package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.state.JumpState;

public class JumpCommand extends InputCommand {	
	public void apply() {
		Entity entity = this.getEntity();
		if (entity.getEntityState().canJump()) {
			entity.setVel(entity.getVel().setY(GameConf.playerJumpBoost));
			entity.setEntityState(new JumpState());
		}
	}

}
