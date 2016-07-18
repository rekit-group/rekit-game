package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.gui.View;
import edu.kit.informatik.ragnarok.gui.filters.Filter;

/**
 * This Command Type is used for attaching and/or detaching {@link Filter
 * Filters} to a {@link View}
 *
 * @author Dominik Fuchß
 *
 */
public class FilterCommand implements Command {
	/**
	 * Indicates whether this command will enable a filter
	 */
	private final boolean enable;
	/**
	 * The view
	 */
	private final View view;
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
	public FilterCommand(boolean enable, View view, Filter filter) {
		this.enable = enable;
		this.view = view;
		this.filter = filter;
	}

	@Override
	public void execute(InputMethod inputMethod) {
		if (this.enable) {
			this.view.injectFilter(this.filter);
		} else {
			this.view.removeFilter();
		}
	}

}
