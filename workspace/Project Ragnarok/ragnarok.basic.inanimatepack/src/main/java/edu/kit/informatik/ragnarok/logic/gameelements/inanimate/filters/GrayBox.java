package edu.kit.informatik.ragnarok.logic.gameelements.inanimate.filters;

import edu.kit.informatik.ragnarok.logic.filters.GrayScaleMode;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

@LoadMe
public class GrayBox extends FilterBox {
	protected GrayBox() {
		super(new GrayScaleMode());
	}
}
