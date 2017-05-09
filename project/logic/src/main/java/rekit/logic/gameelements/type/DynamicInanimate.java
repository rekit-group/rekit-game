package rekit.logic.gameelements.type;

import java.util.Set;
import java.util.stream.Collectors;

import rekit.config.GameConf;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.inanimate.Inanimate;
import rekit.persistence.ModManager;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils;
import rekit.util.ReflectUtils.LoadMe;

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
	public static Set<? extends GameElement> getPrototypes() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, ModManager.SYSLOADER, DynamicInanimate.class).stream().filter(GameElement::isAddableToGroup)
				.collect(Collectors.toSet());

	}

}
