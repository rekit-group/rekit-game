package rekit.logic.gui.parallax;

import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;
import rekit.primitives.geometry.Vec;

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
		super(pos.setZ(parent == null ? 1 : parent.perspectiveZ), new Vec(), new Vec(1), Team.BACKGROUND);
		this.parent = parent;
		this.backgroundZ = Team.BACKGROUND.zRange.min;
	}

	@Override
	public final Integer getZHint() {
		return (int) (this.backgroundZ - this.parent.fieldXtoLayerX(100));
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
	public void collidedWithSolid(Frame collision, Direction dir) {
		// Do nothing
	}

}
