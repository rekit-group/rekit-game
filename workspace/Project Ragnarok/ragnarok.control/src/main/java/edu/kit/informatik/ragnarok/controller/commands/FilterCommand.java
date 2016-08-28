package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.gui.View;
import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.primitives.image.Filter;

/**
 * This Command Type is used for attaching and/or detaching {@link Filter
 * Filters} to a {@link View}
 *
 * @author Dominik Fuchß
 *
 */
public final class FilterCommand implements Command {
	/**
	 * Indicates whether this command will enable a filter
	 */
	private final boolean enable;
	/**
	 * The model
	 */
	private final Model model;
	/**
	 * The filter
	 */
	private final Filter filter;

	/**
	 * Create a new FilterCommand
	 *
	 * @param enable
	 *            indicates whether this command will enable a filter
	 * @param view
	 *            the view
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
