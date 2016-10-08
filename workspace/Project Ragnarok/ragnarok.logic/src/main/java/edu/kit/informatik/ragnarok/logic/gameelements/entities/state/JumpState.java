package edu.kit.informatik.ragnarok.logic.gameelements.entities.state;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.GameTime;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

/**
 * The jumping state a entity is in upon jumping until landing Represents the
 * state where a player can not jump anymore.
 *
 * @author Angelo Aracri
 * @version 1.0
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

		if (this.timeLeft > 0 && this.entity.getVel().getY() > GameConf.PLAYER_JUMP_BOOST) {
			this.entity.setVel(new Vec(this.entity.getVel().getX(), GameConf.PLAYER_JUMP_BOOST));
		}
	}
}
