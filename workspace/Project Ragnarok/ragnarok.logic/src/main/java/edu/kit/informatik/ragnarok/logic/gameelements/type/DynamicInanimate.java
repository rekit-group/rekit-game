package edu.kit.informatik.ragnarok.logic.gameelements.type;

import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;

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

}
