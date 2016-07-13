package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;

/**
 * This {@link InputCommand} will cause a Walk of an {@link Entity}
 *
 * @author Dominik Fuchß
 * @author Angelo Aracri
 *
 */
public class WalkCommand extends InputCommand {
	/**
	 * The {@link Direction} of the walk
	 */
	private Direction dir;

	/**
	 * Instantiate the WalkCommand
	 *
	 * @param supervisor
	 *            the supervisor
	 * @param dir
	 *            the direction
	 */
	public WalkCommand(CommandSupervisor supervisor, Direction dir) {
		super(supervisor);
		this.dir = dir;
	}

	@Override
	public void execute(InputMethod inputMethod) {
		if (inputMethod == InputMethod.RELEASE) {
			return;
		}
		Entity entity = this.supervisor.getEntity(this);

		// Update x velocity with corresponding direction and acceleration
		Vec newVel = entity.getVel().addX(this.dir.getVector().getX() * GameConf.PLAYER_WALK_ACCEL);

		// check if max speed achieved
		if (Math.abs(newVel.getX()) > GameConf.PLAYER_WALK_MAX_SPEED) {
			newVel = newVel.setX(Math.signum(newVel.getX()) * GameConf.PLAYER_WALK_MAX_SPEED);
		}

		// Save new velocity
		entity.setVel(newVel);
	}
}
