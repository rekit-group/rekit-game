package rekit.logic.gameelements.inanimate;

import rekit.core.GameGrid;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.image.RGBColor;

/**
 *
 * This is the default implementation of an inanimate door in the game.
 *
 */
public class InanimateDoor extends InanimateBox {
	/**
	 * Create a new door.
	 *
	 * @param pos
	 *            the position
	 */
	public InanimateDoor(Vec pos) {
		super(pos, new Vec(1, 7), new RGBAColor(130, 130, 130, 255));
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawRectangle(this.getPos(), this.getSize(), this.color);

		Vec start = this.getPos().add(this.getSize().scalar(-(1 / 2f))).add(new Vec(0.1f, 0.1f));
		Vec end = start.add(this.getSize()).add(new Vec(-0.2f, -0.2f));

		for (float x = start.getX(); x <= end.getX(); x += 0.2) {
			f.drawCircle(new Vec(x, start.getY()), new Vec(0.12f), new RGBColor(80, 80, 80).toRGBA());
			f.drawCircle(new Vec(x, end.getY()), new Vec(0.12f), new RGBColor(80, 80, 80).toRGBA());
		}

		for (float y = start.getY(); y <= end.getY(); y += 0.2) {
			f.drawCircle(new Vec(start.getX(), y), new Vec(0.12f), new RGBColor(80, 80, 80).toRGBA());
			f.drawCircle(new Vec(end.getX(), y), new Vec(0.12f), new RGBColor(80, 80, 80).toRGBA());
		}
	}

}
