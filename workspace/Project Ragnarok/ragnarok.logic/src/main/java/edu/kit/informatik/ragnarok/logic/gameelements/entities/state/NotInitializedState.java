package edu.kit.informatik.ragnarok.logic.gameelements.entities.state;

import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

/**
 * This state is used for all prototypes of {@link Entity}<br>
 * This state will print to the syserr that the wrong constructor
 * {@link Entity#Entity(Team team)} was used to create this element
 *
 * @author Dominik Fuchß
 *
 */
public class NotInitializedState extends EntityState {
	/**
	 * Create the State
	 * 
	 * @param entity
	 *            the entity
	 */
	public NotInitializedState(Entity entity) {
		super(entity);
	}

	/**
	 * Indicates whether message was written
	 */
	private boolean done = false;

	@Override
	public void logicLoop(float deltaTime) {
		if (this.done) {
			return;
		}
		this.done = true;
		System.err.println("Entity " + this.entity.getClass().getSimpleName() + ": State: NotInitializedState");
		System.err.println("Entity of type " + this.entity.getClass().getSimpleName()
				+ " is not initialized; maybe you have used the wrong constructor of Entity!");
	}

	@Override
	public void floorCollision() {
		if (this.done) {
			return;
		}
		this.done = true;
		System.err.println("Entity " + this.entity.getClass().getSimpleName() + ": State: NotInitializedState");
		System.err.println("Entity of type " + this.entity.getClass().getSimpleName()
				+ " is not initialized; maybe you have used the wrong constructor of Entity!");

	}
}
