package rekit.logic.gameelements.entities.state;

import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.entities.StateEntity;
import rekit.primitives.geometry.Vec;
import rekit.primitives.time.Timer;

/**
 * The jumping state a entity is in upon jumping until landing Represents the
 * state where a player can not jump anymore.
 *
 * @author Angelo Aracri
 */
public class JumpState extends EntityState {
	private Timer timer;

	/**
	 * Create State.
	 *
	 * @param entity
	 *            the entity
	 */
	public JumpState(StateEntity entity) {
		super(entity);
		this.timer = new Timer(Player.JUMP_TIME);
	}

	@Override
	public boolean canJump() {
		return false;
	}

	@Override
	public void floorCollision() {
		this.entity.setEntityState(new DefaultState(this.entity));
	}

	@Override
	public void logicLoop() {
		this.timer.logicLoop();
		if (!this.timer.timeUp() && this.entity.getVel().y > Player.JUMP_BOOST) {
			this.entity.setVel(new Vec(this.entity.getVel().x, Player.JUMP_BOOST));
		} else if (this.timer.timeUp()) {
			this.entity.setEntityState(new FallState(this.entity));
		}
	}
}
