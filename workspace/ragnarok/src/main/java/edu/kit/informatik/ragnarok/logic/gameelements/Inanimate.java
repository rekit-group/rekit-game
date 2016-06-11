package edu.kit.informatik.ragnarok.logic.gameelements;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;

import config.c;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Vec2D;

public class Inanimate extends GameElement {
	
	private Vec2D size;
	
	private RGB color;
	
	public Inanimate(Vec2D pos, Vec2D size, RGB color) {
		this.setPos(pos);
		this.size = size;
		this.color = color;		
	}

	@Override
	public void render(GC gc) {
		Vec2D pos = this.getPos();
		
		gc.setBackground(new Color(gc.getDevice(), this.color));
		gc.fillRectangle(
				(int) ((pos.getX() - 0.5f + 0.01f) * c.pxPerUnit),
				(int) ((pos.getY() - 0.5f + 0.01f) * c.pxPerUnit),
				(int) ((this.size.getX() - 0.02f) * c.pxPerUnit),
				(int) ((this.size.getY() - 0.02f) * c.pxPerUnit) 
			);
	}

	public void reactToCollision(GameElement element, Direction dir) {
		element.resetPosition();
	}
	
	@Override
	public void damage(int damage) {
		// Do nothing, blocks cannot be damaged

	}
	
	

	@Override
	public void resetPosition() {
		// Do nothing, blocks cannot collide
	}

}
