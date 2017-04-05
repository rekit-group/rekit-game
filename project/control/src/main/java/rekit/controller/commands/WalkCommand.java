package rekit.controller.commands;

import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gameelements.entities.Player;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;

/**
 * This {@link EntityCommand} will cause a Walk of an {@link Entity}.
 *
 * @author Dominik Fuchss
 * @author Angelo Aracri
 *
 */
public class WalkCommand extends EntityCommand {
	/**
	 * The {@link Direction} of the walk.
	 */
	private Direction dir;

	/**
	 * Instantiate the WalkCommand.
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
		Vec newVel = entity.getVel().addX(this.dir.getVector().x * Player.WALK_ACCEL);

		// check if max speed achieved
		if (Math.abs(newVel.x) > Player.WALK_MAX_SPEED) {
			newVel = newVel.setX(Math.signum(newVel.x) * Player.WALK_MAX_SPEED);
		}

		// Save new velocity
		entity.setVel(newVel);
	}
}
