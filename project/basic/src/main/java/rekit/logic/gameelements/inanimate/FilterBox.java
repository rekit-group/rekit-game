package rekit.logic.gameelements.inanimate;

import rekit.core.GameGrid;
import rekit.logic.filters.Filter;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.type.DynamicInanimate;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.ReflectUtils.LoadMe;

/**
 * This class realizes a box which applies {@link Filter Filters} to the game by
 * colliding with it.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
public final class FilterBox extends DynamicInanimate {

	/**
	 * The inner inanimate box.
	 */
	private Inanimate innerBox;

	/**
	 * The filter.
	 */
	private Filter filter;

	/**
	 * Prototype Constructor.
	 */
	public FilterBox() {
		super();
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
		Filter filter = null;
		if (options.length == 1 && !"none".equalsIgnoreCase(options[0])) {
			filter = this.searchFilterByName(options[0]);
		}
		return new FilterBox(startPos, new Vec(1), new RGBAColor(80, 80, 255, 255), filter);
	}

	private Filter searchFilterByName(String name) {
		for (Filter f : Filter.ALL_FILTERS) {
			if (f.getClass().getSimpleName().equals(name)) {
				return f;
			}
		}
		return null;
	}

}
