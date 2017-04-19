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
		float sizeDiff = -0.2f;
		
		f.drawRectangle(this.getPos(), this.getSize(), darkColor);
		f.drawRectangle(this.getPos(), this.getSize().add(new Vec(sizeDiff)), this.color);

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
