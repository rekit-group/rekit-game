package edu.kit.informatik.ragnarok.logic;

import org.eclipse.swt.graphics.GC;

import edu.kit.informatik.ragnarok.logic.gameelements.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;

public class TryHardTriangle extends Entity {
	@Override
	public void render(GC gc) {
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
	public void resetPosition() {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");

	}

}