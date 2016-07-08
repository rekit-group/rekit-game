package edu.kit.informatik.ragnarok.logic.gameelements.type;

import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public abstract class DynamicInanimate extends Inanimate {

	/**
	 * Prototype Constructor
	 */
	public DynamicInanimate() {
		super(new Vec(), new Vec(1), new RGBColor(0, 0, 0));
	}

	protected DynamicInanimate(Vec pos, Vec size, RGBColor color) {
		super(pos, size, color);
	}

	@Override
	public abstract int getID();

}
