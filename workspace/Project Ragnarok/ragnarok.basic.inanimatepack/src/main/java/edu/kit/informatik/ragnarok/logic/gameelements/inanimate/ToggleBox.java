package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class ToggleBox extends DynamicInanimate {

	protected ToggleBox(Vec pos, Vec size, RGBColor color) {
		super(pos, size, color);
	}

	@Override
	public int getID() {
		return 80;
	}

}
