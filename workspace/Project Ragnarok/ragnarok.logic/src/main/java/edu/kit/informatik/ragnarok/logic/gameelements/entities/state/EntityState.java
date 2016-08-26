package edu.kit.informatik.ragnarok.logic.gameelements.entities.state;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

/**
 * The abstract state an entity can be in. <i>EntityStates</i> determine the
 * <i>Entities</i> jumping behavior.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public abstract class EntityState {
	/**
	 * The entity
	 */
	protected final Entity entity;

	/**
	 * Create State
	 *
	 * @param entitiy
	 *            the entity
	 */
	public EntityState(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Indicates whether the entity can jump
	 *
	 * @return {@code true} if jumping is allowed, {@code false} otherwise
	 */
	public boolean canJump() {
		return true;
	}

	/**
	 * Logic loop of state
	 *
	 * @param deltaTime
	 *            the delta time
	 * @see Entity#logicLoop(float)
	 */
	public void logicLoop(float deltaTime) {
		// Do nothing
	}

	/**
	 * This method will be invoked when this entity collides with the floor
	 */
	public void floorCollision() {
		// Do nothing
	}

}
