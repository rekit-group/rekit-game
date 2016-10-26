package edu.kit.informatik.ragnarok.logic.gameelements.inanimate.filters;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.Filter;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;

/**
 * This class realizes a box which applies {@link Filter Filters} to the game by
 * colliding with it.
 *
 * @author Dominik Fuchss
 *
 */
class FilterBox extends DynamicInanimate {
	/**
	 * The inner inanimate box.
	 */
	private Inanimate innerBox;

	/**
	 * The filter.
	 */
	private final Filter filter;

	/**
	 * Prototype Constructor.
	 *
	 * @param filter
	 *            the filter
	 */
	protected FilterBox(Filter filter) {
		super();
		this.filter = filter;
	}

	/**
	 * Create a FilterBox.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param filter
	 *            the filter
	 */
	private FilterBox(Vec pos, Vec size, RGBAColor color, Filter filter) {
		super(pos, size, color);
		// create inner InanimateBox with given position
		this.innerBox = Inanimate.getPrototype().create(pos, null);
		this.filter = filter;
	}

	@Override
	public final void reactToCollision(GameElement element, Direction dir) {
		if (this.filter != null) {
			this.getScene().getModel().setFilter(this.filter);
		} else {
			this.getScene().getModel().removeFilter();
		}
		this.innerBox.setScene(this.getScene());
		this.innerBox.reactToCollision(element, dir);
	}

	@Override
	public final void internalRender(Field f) {
		this.innerBox.internalRender(f);
	}

	@Override
	public final FilterBox create(Vec startPos, String[] options) {
		return new FilterBox(startPos, new Vec(1), new RGBAColor(80, 80, 255, 255), this.filter);
	}

}
