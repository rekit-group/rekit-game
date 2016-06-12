package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;

/**
 * Defines the interface that every GameElement that can collide must have.  
 *
 * @author Angelo Aracri
 * @version 1.0
 */
public interface Collidable {
	/**
	 * Optional reaction to a collision.
	 * Adds points to the GameElements score
	 * @param points the Points to add
	 */
	public void addPoints(int points);
	
	/**
	 * Getter for the collected Points of the GameElement
	 * @return the points of the GameElement
	 */
	public int getPoints();
	
	/**
	 * Optional reaction to a collision.
	 * Removes lifes of the GameElement. 
	 * @param damage the amount of lifes to remove
	 */
	public void addDamage(int damage);
	
	/**
	 * Getter for the current lifes of the GameElement.
	 * @return the amount of lifes of the GameElement
	 */
	public int getLifes();
	
	/**
	 * Optional reaction to a collision that can be invoked in other GameElements reactToCollision().
	 * Notification that this GameElement has collided with another GameElement with the
	 * CollisionFrame <i>collision</i> from a given direction <i>dir</i>.
	 * Define optional reactions that the GameElement performs on itself upon collision. 
	 * @param collision the Frame of the GameElement that was collided on
	 * @param dir the direction of this GameElement where the collision took place 
	 */
	public void collidedWith(Frame collision, Direction dir);

	/**
	 * Notification that this GameElement has been collided with from another
	 * GameElement <i>element</i> from the given direction <i>dir</i>.
	 * Define optional reactions that the GameElement performs on the other GameElement
	 * upon collision 
	 * @param element the GameElement that collided with this GameElement
	 * @param dir the Direction this GameElement has been collided from.
	 */
	public void reactToCollision(GameElement element, Direction dir);
}
