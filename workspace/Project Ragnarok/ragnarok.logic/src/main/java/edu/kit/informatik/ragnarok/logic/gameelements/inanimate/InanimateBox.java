package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;


import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class InanimateBox extends Inanimate {
	public InanimateBox(Vec2D pos, Vec2D size, RGBColor color) {
		super(pos, size, color);
	}

	@Override
	public void render(Field f) {
		Vec2D pos = this.getPos();
		Vec2D size = this.getSize();
		
		f.drawRectangle(pos, size, this.color);
		
		RGBColor darkColor = new RGBColor(color.red - 30, color.green - 30, color.blue - 30);
		float plateThickness = 0.10f;
		
		f.drawRectangle(pos.add(new Vec2D(0, size.getY() / 2f - plateThickness / 2f)), size.setY(plateThickness), darkColor);
		f.drawRectangle(pos.add(new Vec2D(0, -size.getY() / 2f + plateThickness / 2f)), size.setY(plateThickness), darkColor);
		
		f.drawRectangle(pos.add(new Vec2D(size.getX() / 2f - plateThickness / 2f, 0)), size.setX(plateThickness), darkColor);
		f.drawRectangle(pos.add(new Vec2D(-size.getX() / 2f + plateThickness / 2f, 0)), size.setX(plateThickness), darkColor);
		f.drawRectangle(pos, size.multiply(0.5f), darkColor);
		
	}
}
