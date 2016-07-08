package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class ToggleBoxAlt extends ToggleBox {
	/**
	 * Prototype Constructor
	 */
	public ToggleBoxAlt() {
		super();
	}

	protected ToggleBoxAlt(Vec pos, Vec size, RGBColor color) {
		super(pos, size, color);
		this.offset = ToggleBox.PERIOD / 2;
	}

	@Override
	public int getID() {
		return 81;
	}

	@Override
	public ToggleBox create(Vec startPos) {
		return new ToggleBoxAlt(startPos, new Vec(1), new RGBColor(80, 80, 255));
	}
}
