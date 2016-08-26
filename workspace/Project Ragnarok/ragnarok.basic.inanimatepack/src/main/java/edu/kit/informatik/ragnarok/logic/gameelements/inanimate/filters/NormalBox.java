package edu.kit.informatik.ragnarok.logic.gameelements.inanimate.filters;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateBox;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

@LoadMe
public class NormalBox extends DynamicInanimate {
	/**
	 * The inner inanimate box
	 */
	protected InanimateBox innerBox;
	/**
	 * The current time offset
	 */
	protected long offset = 0;

	/**
	 * Prototype Constructor
	 */
	public NormalBox() {
		super();
	}

	/**
	 * Creaze a BoostBox
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	protected NormalBox(Vec pos, Vec size, RGBAColor color) {
		super(pos, size, color);
		// create inner InanimateBox with given position
		this.innerBox = (InanimateBox) InanimateBox.staticCreate(pos);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		this.getScene().getModel().removeFilter();
		this.innerBox.reactToCollision(element, dir);
	}

	@Override
	public void internalRender(Field f) {
		this.innerBox.internalRender(f);
	}

	@Override
	public NormalBox create(Vec startPos, String[] options) {
		return new NormalBox(startPos, new Vec(1), new RGBAColor(80, 80, 255, 255));
	}
}
