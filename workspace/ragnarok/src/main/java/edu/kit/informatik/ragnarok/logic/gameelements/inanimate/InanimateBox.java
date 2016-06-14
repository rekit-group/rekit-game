package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class InanimateBox extends Inanimate {
	public InanimateBox(Vec2D pos, Vec2D size, RGB color) {
		super(pos, size, color);
	}

	@Override
	public void render(Field f) {
		Vec2D pos = this.getPos();
		
		f.drawRectangle(pos, size, this.color);
		
		RGB darkColor = new RGB(color.red - 30, color.green - 30, color.blue - 30);
		float plateThickness = 0.10f;
		
		f.drawRectangle(pos.add(new Vec2D(0, size.getY() / 2f - plateThickness / 2f)), size.setY(plateThickness), darkColor);
		f.drawRectangle(pos.add(new Vec2D(0, -size.getY() / 2f + plateThickness / 2f)), size.setY(plateThickness), darkColor);
		
		f.drawRectangle(pos.add(new Vec2D(size.getX() / 2f - plateThickness / 2f, 0)), size.setX(plateThickness), darkColor);
		f.drawRectangle(pos.add(new Vec2D(-size.getX() / 2f + plateThickness / 2f, 0)), size.setX(plateThickness), darkColor);
		f.drawRectangle(pos, size.multiply(0.5f), darkColor);
		
	}
}
