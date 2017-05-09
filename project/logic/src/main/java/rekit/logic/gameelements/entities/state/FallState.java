package rekit.logic.gameelements.entities.state;

import rekit.logic.gameelements.entities.StateEntity;

/**
 * The default state a entity is in after jumping. Represents the state where a
 * player can not jump
 *
 */
public class FallState extends EntityState {
	/**
	 * Create State.
	 *
	 * @param entity
	 *            the entity
	 */
	public FallState(StateEntity entity) {
		super(entity);
	}

	@Override
	public boolean canJump() {
		return false;
	}

	@Override
	public void floorCollision() {
		this.entity.setEntityState(new DefaultState(this.entity));
	}

}
