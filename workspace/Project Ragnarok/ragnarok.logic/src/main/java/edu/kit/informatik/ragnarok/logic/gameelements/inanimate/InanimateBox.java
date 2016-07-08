package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class InanimateBox extends Inanimate {
	protected InanimateBox(Vec pos, Vec size, RGBColor color) {
		super(pos, size, color);
	}

	@Override
	public void internalRender(Field f) {
		f.drawRectangle(this.pos, this.size, this.color);

		RGBColor darkColor = new RGBColor(this.color.red - 30, this.color.green - 30, this.color.blue - 30);
		float plateThickness = 0.10f;

		f.drawRectangle(this.pos.add(new Vec(0, this.size.getY() / 2f - plateThickness / 2f)), this.size.setY(plateThickness), darkColor);
		f.drawRectangle(this.pos.add(new Vec(0, -this.size.getY() / 2f + plateThickness / 2f)), this.size.setY(plateThickness), darkColor);

		f.drawRectangle(this.pos.add(new Vec(this.size.getX() / 2f - plateThickness / 2f, 0)), this.size.setX(plateThickness), darkColor);
		f.drawRectangle(this.pos.add(new Vec(-this.size.getX() / 2f + plateThickness / 2f, 0)), this.size.setX(plateThickness), darkColor);
		f.drawRectangle(this.pos, this.size.multiply(0.5f), darkColor);
	}

	public static Inanimate staticCreate(Vec pos) {
		int randCol = (int) (Math.random() * 60 + 50);
		return new InanimateBox(pos, new Vec(1, 1), new RGBColor(randCol, randCol, randCol));
	}

	@Override
	public int getID() {
		return 50;
	}
}
