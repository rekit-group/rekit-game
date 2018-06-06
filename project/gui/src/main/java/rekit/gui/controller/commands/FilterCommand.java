package rekit.gui.controller.commands;

import rekit.gui.View;
import rekit.logic.Model;
import rekit.logic.filters.Filter;

/**
 * This Command Type is used for attaching and/or detaching {@link Filter
 * Filters} to a {@link View}. <br>
 * <b>Only for testing purposes</b>
 *
 * @author Dominik Fuchss
 *
 */
public final class FilterCommand implements Command {
	/**
	 * Indicates whether this command will enable a filter.
	 */
	private final boolean enable;
	/**
	 * The model.
	 */
	private final Model model;
	/**
	 * The filter.
	 */
	private final Filter filter;

	/**
	 * Create a new FilterCommand.
	 *
	 * @param enable
	 *            indicates whether this command will enable a filter
	 * @param model
	 *            the model
	 * @param filter
	 *            the filter or ignored if {@code enable == false}
	 */
	public FilterCommand(boolean enable, Model model, Filter filter) {
		this.enable = enable;
		this.model = model;
		this.filter = filter;
	}

	@Override
	public void execute(Object... params) {
		if (this.enable) {
			this.model.setFilter(this.filter);
		} else {
			this.model.removeFilter();
		}
	}

}
