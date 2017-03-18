package rekit.logic.gameelements.entities.state;

import rekit.logic.gameelements.entities.Entity;

/**
 * The default state a entity is in after initialization. Represents the state
 * where a player can jump
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public class DefaultState extends EntityState {
	/**
	 * Create State.
	 *
	 * @param entitiy
	 *            the entity
	 */
	public DefaultState(Entity entitiy) {
		super(entitiy);
	}

}
