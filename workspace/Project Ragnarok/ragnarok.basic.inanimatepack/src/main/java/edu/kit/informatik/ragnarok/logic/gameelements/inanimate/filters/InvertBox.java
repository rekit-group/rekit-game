package edu.kit.informatik.ragnarok.logic.gameelements.inanimate.filters;

import edu.kit.informatik.ragnarok.logic.filters.InvertedMode;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

@LoadMe
public class InvertBox extends FilterBox {
	protected InvertBox() {
		super(new InvertedMode());
	}
}