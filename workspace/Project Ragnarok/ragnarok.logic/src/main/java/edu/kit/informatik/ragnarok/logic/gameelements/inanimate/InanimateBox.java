package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;

public class InanimateBox extends Inanimate {
	protected InanimateBox(Vec pos, Vec size, RGBAColor color) {
		super(pos, size, color);
	}

	@Override
	public void internalRender(Field f) {
		f.drawRectangle(this.getPos(), this.getSize(), this.color);

		RGBAColor darkColor = new RGBAColor(this.color.red - 30, this.color.green - 30, this.color.blue - 30, this.color.alpha);
		float plateThickness = 0.10f;

		f.drawRectangle(this.getPos().add(new Vec(0, this.getSize().getY() / 2f - plateThickness / 2f)), this.getSize().setY(plateThickness),
				darkColor);
		f.drawRectangle(this.getPos().add(new Vec(0, -this.getSize().getY() / 2f + plateThickness / 2f)), this.getSize().setY(plateThickness),
				darkColor);

		f.drawRectangle(this.getPos().add(new Vec(this.getSize().getX() / 2f - plateThickness / 2f, 0)), this.getSize().setX(plateThickness),
				darkColor);
		f.drawRectangle(this.getPos().add(new Vec(-this.getSize().getX() / 2f + plateThickness / 2f, 0)), this.getSize().setX(plateThickness),
				darkColor);
		f.drawRectangle(this.getPos(), this.getSize().scalar(0.5f), darkColor);
	}

	public static Inanimate staticCreate(Vec pos) {
		int randCol = (int) (GameConf.PRNG.nextDouble() * 60 + 50);
		return new InanimateBox(pos, new Vec(1, 1), new RGBAColor(randCol, randCol, randCol, 255));
	}

}
