package edu.kit.informatik.ragnarok.logic.gameelements.inanimate.filters;

import edu.kit.informatik.ragnarok.logic.filters.RandomMode;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

@LoadMe
public class CrazyBox extends FilterBox {
	protected CrazyBox() {
		super(new RandomMode());
	}
}