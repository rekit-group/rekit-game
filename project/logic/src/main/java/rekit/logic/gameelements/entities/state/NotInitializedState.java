package rekit.logic.gameelements.entities.state;

import rekit.config.GameConf;
import rekit.core.Team;
import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gameelements.entities.StateEntity;

/**
 * This state is used for all prototypes of {@link Entity}.<br>
 * This state will print to the syserr that the wrong constructor
 * {@link Entity#Entity(Team team)} was used to create this element
 *
 * @author Dominik Fuchss
 *
 */
public class NotInitializedState extends EntityState {
	/**
	 * Create the State.
	 *
	 * @param entity
	 *            the entity
	 */
	public NotInitializedState(StateEntity entity) {
		super(entity);
	}

	/**
	 * Indicates whether message was written.
	 */
	private boolean done = false;

	@Override
	public void logicLoop() {
		if (this.done) {
			return;
		}
		this.done = true;
		GameConf.GAME_LOGGER.error("Entity " + this.entity.getClass().getSimpleName() + ": State: NotInitializedState");
		GameConf.GAME_LOGGER.error(
				"Entity of type " + this.entity.getClass().getSimpleName() + " is not initialized; maybe you have used the wrong constructor of Entity!");
	}

	@Override
	public void floorCollision() {
		if (this.done) {
			return;
		}
		this.done = true;
		GameConf.GAME_LOGGER.error("Entity " + this.entity.getClass().getSimpleName() + ": State: NotInitializedState");
		GameConf.GAME_LOGGER.error(
				"Entity of type " + this.entity.getClass().getSimpleName() + " is not initialized; maybe you have used the wrong constructor of Entity!");

	}
}
