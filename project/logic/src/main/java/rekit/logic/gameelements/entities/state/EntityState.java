package rekit.logic.gameelements.entities.state;

import rekit.logic.gameelements.entities.StateEntity;

/**
 * The abstract state an entity can be in. <i>EntityStates</i> determine the
 * <i>Entities</i> jumping behavior.
 *
 * @author Angelo Aracri
 */
public abstract class EntityState {
	/**
	 * The entity.
	 */
	protected final StateEntity entity;

	/**
	 * Create State.
	 *
	 * @param entity
	 *            the entity
	 */
	public EntityState(StateEntity entity) {
		this.entity = entity;
	}

	/**
	 * Indicates whether the entity can jump.
	 *
	 * @return {@code true} if jumping is allowed, {@code false} otherwise
	 */
	public boolean canJump() {
		return true;
	}

	/**
	 * Logic loop of state.
	 *
	 */
	public void logicLoop() {
		// Do nothing
	}

	/**
	 * This method will be invoked when this entity collides with the floor.
	 */
	public void floorCollision() {
		// Do nothing
	}

}
