package rekit.logic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import rekit.logic.gameelements.GameElement;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;

/**
 * Defines the interface that every GameElement that can collide must have.
 *
 * @author Angelo Aracri
 */
public interface Collidable {

	/**
	 * Optional reaction to a collision. Removes lifes of the GameElement.
	 *
	 * @param damage
	 *            the amount of lifes to remove
	 */
	default void addDamage(int damage) {
	}

	/**
	 * Getter for the current lifes of the GameElement.
	 *
	 * @return the amount of lifes of the GameElement
	 */
	default int getLives() {
		return 0;
	}

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
	@Optional
	default void collidedWithSolid(Frame collision, Direction dir) {
	}

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
	}

	/**
	 * Methods which are annotated with this annotation, are optional methods
	 * for special behavior.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	@Retention(RetentionPolicy.SOURCE)
	@Target(ElementType.METHOD)
	@Documented
	public @interface Optional {
	}
}
