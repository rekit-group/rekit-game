package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.JumpState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.WalkState;

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
		Entity entity = this.getEntity();

		// Update x velocity with corresponding direction and acceleration
		Vec2D newVel = entity.getVel().addX(
				this.dir.getVector().getX() * c.playerWalkAccel);

		// check if max speed achieved
		if (Math.abs(newVel.getX()) > c.playerWalkMaxSpeed) {
			newVel = newVel.setX(Math.signum(newVel.getX())
					* c.playerWalkMaxSpeed);
		}
		
		// Save new velocity
		entity.setVel(newVel);
		
		// Update PlayerState
		if (!(entity.getEntityState() instanceof JumpState)) {
			entity.setEntityState(new WalkState());
		}
	}
}
