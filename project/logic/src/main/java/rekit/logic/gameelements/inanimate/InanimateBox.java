package rekit.logic.gameelements.inanimate;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;

/**
 *
 * This is the default implementation of an inanimate in the game (no ground).
 *
 */
public class InanimateBox extends Inanimate {
	/**
	 * Create an InanimateBox.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	protected InanimateBox(Vec pos, Vec size, RGBAColor color) {
		super(pos, size, color);
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawRectangle(this.getPos(), this.getSize(), this.color);

		RGBAColor darkColor = new RGBAColor(this.color.red - 30, this.color.green - 30, this.color.blue - 30, this.color.alpha);
		float plateThickness = 0.10f;

		f.drawRectangle(this.getPos().add(new Vec(0, this.getSize().y / 2f - plateThickness / 2f)), this.getSize().setY(plateThickness),
				darkColor);
		f.drawRectangle(this.getPos().add(new Vec(0, -this.getSize().y / 2f + plateThickness / 2f)), this.getSize().setY(plateThickness),
				darkColor);

		f.drawRectangle(this.getPos().add(new Vec(this.getSize().x / 2f - plateThickness / 2f, 0)), this.getSize().setX(plateThickness),
				darkColor);
		f.drawRectangle(this.getPos().add(new Vec(-this.getSize().x / 2f + plateThickness / 2f, 0)), this.getSize().setX(plateThickness),
				darkColor);
		f.drawRectangle(this.getPos(), this.getSize().scalar(0.5f), darkColor);
	}

	/**
	 * Create a new InanimateBox.
	 *
	 * @param pos
	 *            the position
	 * @return the new Inanimate
	 */
	public static Inanimate staticCreate(Vec pos) {
		int randCol = (int) (GameConf.PRNG.nextDouble() * 60 + 50);
		return new InanimateBox(pos, new Vec(1, 1), new RGBAColor(randCol, randCol, randCol, 255));
	}

}
