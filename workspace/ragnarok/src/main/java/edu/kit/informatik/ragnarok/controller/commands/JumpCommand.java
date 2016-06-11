package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.gameelements.player.JumpState;
import edu.kit.informatik.ragnarok.logic.gameelements.player.Player;

public class JumpCommand extends InputCommand {	
	public void apply() {
		Player player = this.getModel().getPlayer();
		if (player.getPlayerState().canJump()) {
			player.setVel(player.getVel().setY(c.playerJumpBoost));
			player.setPlayerState(new JumpState());
		}
	}

}
