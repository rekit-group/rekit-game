package ragnarok.logic.gui.parallax;

import ragnarok.core.GameElement;
import ragnarok.core.Team;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Frame;
import ragnarok.primitives.geometry.Vec;

/**
 *
 * This is the parent class which all background elements shall extend.
 *
 */
public abstract class BackgroundElement extends GameElement {
	/**
	 * The parent parallax.
	 */
	protected ParallaxLayer parent;
	/**
	 * The z-pos of this element.
	 */
	protected int backgroundZ;

	/**
	 * Create a BG-Element by parent and pos.
	 *
	 * @param parent
	 *            the parent
	 * @param pos
	 *            the positon
	 */
	public BackgroundElement(ParallaxLayer parent, Vec pos) {
		super(pos.setZ(parent == null ? 1 : parent.perspectiveZ), new Vec(), new Vec(1), Team.NEUTRAL);
		this.parent = parent;
		this.backgroundZ = -10;
	}

	@Override
	public final byte getZ() {
		int calculatedPos = (int) (this.backgroundZ - this.parent.fieldXtoLayerX(100));
		if (calculatedPos < -10 && calculatedPos >= -128) {
			return (byte) calculatedPos;
		}
		return -10;
	}

	@Override
	public void addPoints(int points) {
		// Do nothing
	}

	@Override
	public int getPoints() {
		// Do nothing
		return 0;
	}

	@Override
	public void addDamage(int damage) {
		// Do nothing
	}

	@Override
	public int getLives() {
		// Do nothing
		return 0;
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing
	}

}
