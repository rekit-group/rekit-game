package edu.kit.informatik.ragnarok.logic.gameelements.enemies;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;
import edu.kit.informatik.ragnarok.logic.gameelements.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;

public class TryHardTriangle extends Entity {
	@Override
	public void render(Field f) {
		f.drawCircle(this.getPos(), this.getSize(), new RGB(50, 200, 50));
	}

	@Override
	public void logicLoop(float deltaTime) {
	}

	/**
	 * if(dir == Direction.UP) { element.damage(Infinity); } else {
	 * this.damage(Infinity); }
	 */
	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		System.out.println("ENEMY collision " + dir.toString());
		if (dir == Direction.DOWN) {
			element.addPoints(40);
			this.addDamage(1);
		} else {
			// Give player damage
			element.addDamage(1);
			this.addDamage(1);
		}
	}

	@Override
	public void addDamage(int damage) {
		this.destroy();
	}
	
	@Override
	public void addPoints(int points) {
		// Do nothing, blocks cannot get points
	}
	
	@Override
	public int getPoints() {
		return 0;
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing, blocks cannot collide
	}

}
