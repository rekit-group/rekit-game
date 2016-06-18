package edu.kit.informatik.ragnarok.logic.gameelements.entities.state;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

/**
 * The abstract state an entity can be in.
 * <i>EntityStates</i> determine the <i>Entities</i> jumping behavior.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public abstract class EntityState {
	
	protected Entity entity;
	
	public EntityState (Entity entity) {
		this.entity = entity;
	}
	
	/**
	 * Jumps if an <i>Entity</i> is currently able to jump.
	 * Is true in this case.
	 * @return true if an <i>Entity</i> can currently jump, false otherwise
	 */
	public boolean jump() {
		entity.setVel(entity.getVel().setY(GameConf.playerJumpBoost));
		return true;
	}

}
