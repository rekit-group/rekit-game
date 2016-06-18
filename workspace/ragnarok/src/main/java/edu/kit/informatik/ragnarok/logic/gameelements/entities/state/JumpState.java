package edu.kit.informatik.ragnarok.logic.gameelements.entities.state;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

/**
 * The jumping state a entity is in upon jumping until landing
 * Represents the state where a player can not jump anymore.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public class JumpState extends EntityState {
	private long maxTime;
	
	public JumpState(Entity entity) {
		super(entity);
		maxTime = System.currentTimeMillis() + (long) (100 * GameConf.playerJumpTime);
	}

	@Override
	public boolean jump() {
		if (maxTime > System.currentTimeMillis()) {
			entity.setVel(entity.getVel().setY(GameConf.playerJumpBoost));
		}
		return false;
	}
}
