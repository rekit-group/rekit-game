package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.gui.Filter;
import edu.kit.informatik.ragnarok.gui.View;

public class FilterCommand implements Command {
	private boolean enable;
	private View view;
	private Filter filter;

	public FilterCommand(boolean enable) {
		this.enable = enable;
	}

	public void init(View v, Filter f) {
		this.view = v;
		this.filter = f;

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
