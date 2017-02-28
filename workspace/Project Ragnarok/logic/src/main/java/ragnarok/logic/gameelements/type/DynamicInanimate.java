package ragnarok.logic.gameelements.type;

import java.util.Set;

import ragnarok.config.GameConf;
import ragnarok.logic.gameelements.GameElement;
import ragnarok.logic.gameelements.inanimate.Inanimate;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;
import ragnarok.util.ReflectUtils;
import ragnarok.util.ReflectUtils.LoadMe;

/**
 *
 * This class is the parent class of all dynamically loaded Inanimate.
 *
 */
public abstract class DynamicInanimate extends Inanimate {

	/**
	 * Prototype Constructor.
	 */
	protected DynamicInanimate() {
		super(new Vec(), new Vec(1), new RGBAColor(0, 0, 0, 255));
	}

	/**
	 * Create an inanimate.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	protected DynamicInanimate(Vec pos, Vec size, RGBAColor color) {
		super(pos, size, color);
	}

	/**
	 * Load all dynamic inanimate-prototypes.
	 *
	 * @return a set of dynamic inanimate-prototypes
	 * @see LoadMe
	 */
	public static final Set<? extends GameElement> getPrototypes() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, DynamicInanimate.class);
	}

}
