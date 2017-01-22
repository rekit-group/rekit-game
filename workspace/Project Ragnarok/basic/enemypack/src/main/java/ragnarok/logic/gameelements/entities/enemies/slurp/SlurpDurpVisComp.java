package ragnarok.logic.gameelements.entities.enemies.slurp;

import ragnarok.core.Field;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBColor;

/**
 * Simple data-holding class that represents a component of the visualization of
 * a SlurpDurp (Thus, the name).
 *
 * @author Angelo Aracri
 */
public final class SlurpDurpVisComp {
	/**
	 * The relative position.
	 */
	private Vec relativePos;
	/**
	 * The relative size.
	 */
	private Vec relativeSize;
	/**
	 * The color.
	 */
	private RGBColor col;

	/**
	 * Create a component.
	 *
	 * @param relativePos
	 *            the relative position
	 * @param relativeSize
	 *            the relative size
	 * @param col
	 *            the color
	 */
	public SlurpDurpVisComp(Vec relativePos, Vec relativeSize, RGBColor col) {
		this.relativePos = relativePos;
		this.relativeSize = relativeSize;
		this.col = col;
	}

	/**
	 * Render component.
	 * 
	 * @param f
	 *            the field
	 * @param pos
	 *            the parent position
	 * @param size
	 *            the parent size
	 */
	public void render(Field f, Vec pos, Vec size) {
		Vec absSize = size.multiply(this.relativeSize);
		f.drawCircle(pos.add(this.relativePos.multiply(absSize)), absSize, this.col);
	}
}
