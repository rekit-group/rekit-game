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

		f.drawRectangle(pos, this.size, this.color);

		RGBColor darkColor = new RGBColor(this.color.red - 30, this.color.green - 30, this.color.blue - 30);
		float plateThickness = 0.10f;

		f.drawRectangle(pos.add(new Vec2D(0, this.size.getY() / 2f - plateThickness / 2f)), this.size.setY(plateThickness), darkColor);
		f.drawRectangle(pos.add(new Vec2D(0, -this.size.getY() / 2f + plateThickness / 2f)), this.size.setY(plateThickness), darkColor);

		f.drawRectangle(pos.add(new Vec2D(this.size.getX() / 2f - plateThickness / 2f, 0)), this.size.setX(plateThickness), darkColor);
		f.drawRectangle(pos.add(new Vec2D(-this.size.getX() / 2f + plateThickness / 2f, 0)), this.size.setX(plateThickness), darkColor);
		f.drawRectangle(pos, this.size.multiply(0.5f), darkColor);

	}
}
