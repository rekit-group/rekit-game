package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;

public class Inanimate extends GameElement {
	
	protected Vec2D size;
	
	protected RGB color;
	
	public Inanimate(Vec2D pos, Vec2D size, RGB color) {
		this.setPos(pos);
		this.size = size;
		this.color = color;		
	}

	@Override
	public void render(Field f) {
		Vec2D pos = this.getPos();
		
		f.drawRectangle(pos, size.multiply(0.95f), this.color);
	}

	public void reactToCollision(GameElement element, Direction dir) {
		element.collidedWith(this.getCollisionFrame(), dir);
	}
	
	@Override
	public void addDamage(int damage) {
		// Do nothing, blocks cannot be damaged
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

	@Override
	public int getLifes() {
		return 0;
	}

}
