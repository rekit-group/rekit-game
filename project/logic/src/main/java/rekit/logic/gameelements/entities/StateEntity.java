package rekit.logic.gameelements.entities;

import rekit.core.Team;
import rekit.logic.gameelements.entities.state.DefaultState;
import rekit.logic.gameelements.entities.state.EntityState;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;
import rekit.primitives.geometry.Vec;
import rekit.util.state.State;

/**
 * This class represents a special kind of {@link Entity}, a {@link Entity} with
 * inner state. All will be managed by this {@link State}.
 *
 * @author Dominik Fuchss
 *
 */
public abstract class StateEntity extends Entity {

	/**
	 * The current State the Entity is in and determines the jump behavior.
	 */
	private EntityState entityState;

	/**
	 * Constructor that initializes attributes and takes a start position.
	 *
	 * @param startPos
	 *            the position this entity shall be in
	 * @param vel
	 *            the velocity
	 * @param size
	 *            the size
	 * @param team
	 *            the team
	 */
	protected StateEntity(Vec startPos, Vec vel, Vec size, Team team) {
		super(startPos, vel, size, team);
		// Set to default state
		this.setEntityState(new DefaultState(this));
	}

	/**
	 * Set the Entities <i>EntitiyState</i> that determines its jump behavior.
	 *
	 * @param value
	 *            the new EntityState
	 */
	public final void setEntityState(EntityState value) {
		this.entityState = value;
	}

	/**
	 * Return the Entities current <i>EntitiyState</i> that determines its jump
	 * behavior.
	 *
	 * @return the state
	 */
	public final EntityState getEntityState() {
		return this.entityState;
	}

	/**
	 * This method will calculate the next position of the Entity depending on
	 * the velocity.
	 */
	@Override
	protected final void innerLogicLoop() {
		super.innerLogicLoop();
		this.entityState.logicLoop();
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		super.collidedWith(collision, dir);
		if (dir == Direction.UP) {
			this.getEntityState().floorCollision();
		}
	}
}
