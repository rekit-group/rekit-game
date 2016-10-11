package edu.kit.informatik.ragnarok.logic.gameelements.type;

import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils;

public abstract class DynamicInanimate extends Inanimate {

	/**
	 * Prototype Constructor
	 */
	public DynamicInanimate() {
		super(new Vec(), new Vec(1), new RGBAColor(0, 0, 0, 255));
	}

	protected DynamicInanimate(Vec pos, Vec size, RGBAColor color) {
		super(pos, size, color);
	}

	public static final Set<DynamicInanimate> getInanimatePrototypes() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, DynamicInanimate.class);
	}

}
