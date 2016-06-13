package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;

public class Coin extends Entity {

	public Coin(Vec2D startPos) {
		super(startPos);
	}
	
	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.isHostile(element)) {
			element.addPoints(10);
			this.addDamage(1);
		}
	}
	
	@Override
	public Vec2D getSize() {
		return new Vec2D(0.7f, 0.7f);
	}
	
	@Override
	public void render(Field f) {
		RGB color = new RGB(232, 214, 16);
		RGB darkColor = new RGB(192, 174, 6);
		
		double sin = Math.sin((System.currentTimeMillis() / 300.0));
		Vec2D size = this.getSize().multiply((float)sin, 1);
		
		for (float x = -0.025f; x <= 0.025f; x += 0.005f) {
			f.drawCircle(this.getPos().addX(x), size, color);	
		}
		if (sin < 0) {
			f.drawCircle(this.getPos().addX(-0.03f), size, darkColor);
		}
		if (sin > 0) {
			f.drawCircle(this.getPos().addX(0.03f), size, darkColor);
		}
		
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		// no logic
	}

}
