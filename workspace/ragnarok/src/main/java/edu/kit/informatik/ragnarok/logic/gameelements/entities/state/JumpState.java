package edu.kit.informatik.ragnarok.logic.gameelements.entities.state;

/**
 * The jumping state a entity is in upon jumping until landing
 * Represents the state where a player can not jump anymore.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public class JumpState extends EntityState {
	@Override
	public boolean canJump() {
		return false;
	}
}
