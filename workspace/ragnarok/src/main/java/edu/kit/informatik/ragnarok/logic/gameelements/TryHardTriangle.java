package edu.kit.informatik.ragnarok.logic.gameelements;

import org.eclipse.swt.graphics.GC;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;

public class TryHardTriangle extends Entity {
	@Override
	public void render(Field f) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public void logicLoop(float deltaTime) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
	}

	/**
	 * if(dir == Direction.UP) { element.damage(Infinity); } else {
	 * this.damage(Infinity); }
	 */
	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public void damage(int damage) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");

	}

}
