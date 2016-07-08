package edu.kit.informatik.ragnarok.logic.gameelements.type;

import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public abstract class DynamicInanimate extends Inanimate {

	protected DynamicInanimate(Vec pos, Vec size, RGBColor color) {
		super(pos, size, color);
	}

	@Override
	public abstract int getID();

}
