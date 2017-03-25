package rekit.logic.gameelements.inanimate;

import rekit.core.GameGrid;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;

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

		for (float x = start.x; x <= end.x; x += 0.2) {
			f.drawCircle(new Vec(x, start.y), new Vec(0.12f), new RGBAColor(80, 80, 80));
			f.drawCircle(new Vec(x, end.y), new Vec(0.12f), new RGBAColor(80, 80, 80));
		}

		for (float y = start.y; y <= end.y; y += 0.2) {
			f.drawCircle(new Vec(start.x, y), new Vec(0.12f), new RGBAColor(80, 80, 80));
			f.drawCircle(new Vec(end.x, y), new Vec(0.12f), new RGBAColor(80, 80, 80));
		}
	}

}
