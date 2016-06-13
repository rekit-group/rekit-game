package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Vec2D;

public class InanimateFloor extends Inanimate {

	public InanimateFloor(Vec2D pos, Vec2D size, RGB color) {
		super(pos, size, color);
	}
	
	@Override
	public void render(Field f) {
		Vec2D pos = this.getPos();
		
		f.drawRectangle(pos, size, this.color);
		
		RGB darkColor = new RGB(color.red - 30, color.green - 30, color.blue - 30);
		
		float plateThickness = 0.1f;
		f.drawRectangle(
				pos.add(new Vec2D(0, -size.getY() / 2f + plateThickness/2f)),
				size.setY(plateThickness), darkColor);
		
	}
}
