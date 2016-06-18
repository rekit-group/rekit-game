package edu.kit.informatik.ragnarok.logic.gameelements.entities.state;

/**
 * The abstract state an entity can be in.
 * <i>EntityStates</i> determine the <i>Entities</i> jumping behavior.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public abstract class EntityState {
	
	/**
	 * Tells if an <i>Entity</i> is currently able to jump.
	 * Is true in default case.
	 * @return true if an <i>Entity</i> can currently jump, false otherwise
	 */
	public boolean canJump() {
		return true;
	}

}
