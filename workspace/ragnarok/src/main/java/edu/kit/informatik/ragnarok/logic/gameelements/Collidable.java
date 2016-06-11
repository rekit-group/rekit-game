package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;

public interface Collidable {
	public void damage(int damage);

	public void collidedWith(Frame collision, Direction dir);

}
