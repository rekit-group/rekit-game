package ragnarok.logic.gameelements.inanimate.filters;

import ragnarok.core.GameGrid;
import ragnarok.logic.gameelements.GameElement;
import ragnarok.logic.gameelements.inanimate.Inanimate;
import ragnarok.logic.gameelements.type.DynamicInanimate;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.Filter;
import ragnarok.primitives.image.RGBAColor;

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
	public final void internalRender(GameGrid f) {
		this.innerBox.internalRender(f);
	}

	@Override
	public final FilterBox create(Vec startPos, String[] options) {
		return new FilterBox(startPos, new Vec(1), new RGBAColor(80, 80, 255, 255), this.filter);
	}

}
