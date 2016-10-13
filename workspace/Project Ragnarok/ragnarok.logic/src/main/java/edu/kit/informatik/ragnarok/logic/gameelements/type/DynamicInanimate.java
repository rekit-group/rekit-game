package edu.kit.informatik.ragnarok.logic.gameelements.type;

import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.visitor.Visitable;

/**
 *
 * This class is the parent class of all dynamically loaded Inanimate.
 *
 */
public abstract class DynamicInanimate extends Inanimate implements Visitable {

	/**
	 * Prototype Constructor.
	 */
	public DynamicInanimate() {
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
	public static final Set<DynamicInanimate> getInanimatePrototypes() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, DynamicInanimate.class);
	}

}
