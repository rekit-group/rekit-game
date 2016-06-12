package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Vec2D;

public class InanimateBox extends Inanimate {
	public InanimateBox(Vec2D pos, Vec2D size, RGB color) {
		super(pos, size, color);
	}

	@Override
	public void render(Field f) {
		Vec2D pos = this.getPos();
		
		f.drawRectangle(pos, size, this.color);
		
		RGB darkColor = new RGB(color.red - 30, color.green - 30, color.blue - 30);
		
		f.drawRectangle(pos.add(new Vec2D(0, 0.9f)), size.setY(0.1f), darkColor);
		f.drawRectangle(pos.add(new Vec2D(0, 0)), size.setY(0.1f), darkColor);
		
		f.drawRectangle(pos.add(new Vec2D(0.9f, 0)), size.setX(0.1f), darkColor);
		f.drawRectangle(pos.add(new Vec2D(0, 0)), size.setX(0.1f), darkColor);
		
		f.drawRectangle(pos.add(new Vec2D(0.25f, 0.25f)), size.multiply(0.5f), darkColor);
		
	}
}
