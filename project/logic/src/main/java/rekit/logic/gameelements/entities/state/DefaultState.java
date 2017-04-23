package rekit.logic.gameelements.entities.state;

import rekit.logic.gameelements.entities.StateEntity;

/**
 * The default state a entity is in after initialization. Represents the state
 * where a player can jump
 *
 * @author Angelo Aracri
 */
public class DefaultState extends EntityState {
	/**
	 * Create State.
	 *
	 * @param entitiy
	 *            the entity
	 */
	public DefaultState(StateEntity entitiy) {
		super(entitiy);
	}

}
