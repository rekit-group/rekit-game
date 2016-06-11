package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.player.Player;

public class WalkCommand extends InputCommand {
	private Direction dir;

	private void setDir(Direction value) {
		this.dir = value;
	}

	private Direction getDir() {
		return this.dir;
	}

	public WalkCommand(Direction dir) {
		this.setDir(dir);
	}

	@Override
	public void apply() {
		// Get old velocity
		Player player = this.getModel().getPlayer();

		// Update x velocity with corresponding direction and acceleration
		Vec2D newVel = player.getVel().addX(
				this.dir.getVector().getX() * c.playerWalkAccel);

		// check if max speed achieved
		if (Math.abs(newVel.getX()) > c.playerWalkMaxSpeed) {
			newVel = newVel.setX(Math.signum(newVel.getX())
					* c.playerWalkMaxSpeed);
		}

		// Save new velocity
		player.setVel(newVel);
	}
}
