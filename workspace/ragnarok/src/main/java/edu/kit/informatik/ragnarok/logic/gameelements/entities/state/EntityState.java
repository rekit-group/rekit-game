package edu.kit.informatik.ragnarok.logic.gameelements.entities.state;

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
	
	public boolean canJump() {
		return true;
	}
	
	public void logicLoop(float deltaTime) {
		// Do nothing
	}
	
	public void floorCollision() {
		// Do nothing
	}

}
