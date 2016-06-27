package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;


import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class InanimateBox extends Inanimate {
	public InanimateBox(Vec pos, Vec size, RGBColor color) {
		super(pos, size, color);
	}

	@Override
	public void render(Field f) {
		Vec pos = this.getPos();
		Vec size = this.getSize();
		
		f.drawRectangle(pos, size, this.color);
		
		RGBColor darkColor = new RGBColor(color.red - 30, color.green - 30, color.blue - 30);
		float plateThickness = 0.10f;
		
		f.drawRectangle(pos.add(new Vec(0, size.getY() / 2f - plateThickness / 2f)), size.setY(plateThickness), darkColor);
		f.drawRectangle(pos.add(new Vec(0, -size.getY() / 2f + plateThickness / 2f)), size.setY(plateThickness), darkColor);
		
		f.drawRectangle(pos.add(new Vec(size.getX() / 2f - plateThickness / 2f, 0)), size.setX(plateThickness), darkColor);
		f.drawRectangle(pos.add(new Vec(-size.getX() / 2f + plateThickness / 2f, 0)), size.setX(plateThickness), darkColor);
		f.drawRectangle(pos, size.multiply(0.5f), darkColor);
		
	}
}
