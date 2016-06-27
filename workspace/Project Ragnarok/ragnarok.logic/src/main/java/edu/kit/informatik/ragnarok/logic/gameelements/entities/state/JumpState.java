package edu.kit.informatik.ragnarok.logic.gameelements.entities.state;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Vec;

/**
 * The jumping state a entity is in upon jumping until landing
 * Represents the state where a player can not jump anymore.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public class JumpState extends EntityState {
	private float timeLeft = 0;
	
	public JumpState(Entity entity) {
		super(entity);
		timeLeft = GameConf.PLAYER_JUMP_TIME;
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
	public void logicLoop(float deltaTime) {
		timeLeft -= deltaTime;
		
		if (timeLeft > 0) {
			this.entity.setVel(new Vec(this.entity.getVel().getX(), GameConf.PLAYER_JUMP_BOOST));
		}
	}
}
