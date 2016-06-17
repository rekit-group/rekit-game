package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.JumpState;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.WalkState;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class WalkCommand extends InputCommand {
	private Direction dir;

	private void setDir(Direction value) {
		this.dir = value;
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
				this.dir.getVector().getX() * GameConf.playerWalkAccel);

		// check if max speed achieved
		if (Math.abs(newVel.getX()) > GameConf.playerWalkMaxSpeed) {
			newVel = newVel.setX(Math.signum(newVel.getX())
					* GameConf.playerWalkMaxSpeed);
		}
		
		// Save new velocity
		entity.setVel(newVel);
		
		// Update PlayerState
		if (!(entity.getEntityState() instanceof JumpState)) {
			entity.setEntityState(new WalkState());
		}
	}
}
