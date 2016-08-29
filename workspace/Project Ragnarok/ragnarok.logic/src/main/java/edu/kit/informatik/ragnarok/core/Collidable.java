package edu.kit.informatik.ragnarok.core;

import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Frame;

/**
 * Defines the interface that every GameElement that can collide must have.
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public interface Collidable {
	/**
	 * Optional reaction to a collision. Adds points to the GameElements score
	 *
	 * @param points
	 *            the Points to add
	 */
	default void addPoints(int points) {
	};

	/**
	 * Getter for the collected Points of the GameElement
	 *
	 * @return the points of the GameElement
	 */
	default int getPoints() {
		return 0;
	};

	/**
	 * Optional reaction to a collision. Removes lifes of the GameElement.
	 *
	 * @param damage
	 *            the amount of lifes to remove
	 */
	default void addDamage(int damage) {
	};

	/**
	 * Getter for the current lifes of the GameElement.
	 *
	 * @return the amount of lifes of the GameElement
	 */
	default int getLives() {
		return 0;
	};

	/**
	 * Optional reaction to a collision that can be invoked in other
	 * GameElements reactToCollision(). Notification that this GameElement has
	 * collided with another GameElement with the CollisionFrame
	 * <i>collision</i> from a given direction <i>dir</i>. Define optional
	 * reactions that the GameElement performs on itself upon collision.
	 *
	 * @param collision
	 *            the Frame of the GameElement that was collided on
	 * @param dir
	 *            the direction of this GameElement where the collision took
	 *            place
	 */
	default void collidedWith(Frame collision, Direction dir) {
	};

	/**
	 * <p>
	 * Notification that this GameElement has been collided with from another
	 * GameElement <i>element</i> from the given direction <i>dir</i>.
	 * </p>
	 * <p>
	 * Can be overwritten for defining an optional reaction that the GameElement
	 * performs on the other GameElement and/or itself upon collision.
	 * </p>
	 *
	 * @param element
	 *            the GameElement that collided with this GameElement
	 * @param dir
	 *            the Direction this GameElement has been collided from.
	 */
	default void reactToCollision(GameElement element, Direction dir) {
	};

}
