package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;

public interface Collidable {
	public void addPoints(int points);
	public int getPoints();
	
	public void addDamage(int damage);
	public int getLifes();
	
	public void collidedWith(Frame collision, Direction dir);

}
