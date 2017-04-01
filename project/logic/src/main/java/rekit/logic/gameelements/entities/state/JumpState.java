package rekit.logic.gameelements.entities.state;

import rekit.config.GameConf;
import rekit.core.GameTime;
import rekit.logic.gameelements.entities.Entity;
import rekit.primitives.geometry.Vec;

/**
 * The jumping state a entity is in upon jumping until landing Represents the
 * state where a player can not jump anymore.
 *
 * @author Angelo Aracri
 */
public class JumpState extends EntityState {
	/**
	 * The time left for the jump.
	 */
	private long timeLeft = 0;
	/**
	 * Last time of invoking {@link #logicLoop()}.
	 */
	private long lastTime = GameTime.getTime();

	/**
	 * Create State.
	 *
	 * @param entity
	 *            the entity
	 */
	public JumpState(Entity entity) {
		super(entity);
		this.timeLeft = GameConf.PLAYER_JUMP_TIME;
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
		long deltaTime = GameTime.getTime() - this.lastTime;
		this.lastTime += deltaTime;
		this.timeLeft -= deltaTime;

		if (this.timeLeft > 0 && this.entity.getVel().y > GameConf.PLAYER_JUMP_BOOST) {
			this.entity.setVel(new Vec(this.entity.getVel().x, GameConf.PLAYER_JUMP_BOOST));
		}
	}
}
