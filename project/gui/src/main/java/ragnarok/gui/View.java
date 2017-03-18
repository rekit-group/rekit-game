package ragnarok.gui;

import ragnarok.logic.Model;
import ragnarok.util.InputHelper;

/**
 * This Interface defines the View for the MVC.
 *
 * @author Dominik Fuchss
 *
 */
public interface View {
	/**
	 * Get the view.
	 *
	 * @param model
	 *            the model
	 * @return the view
	 */
	static View getView(Model model) {
		return new GameView(model);
	}

	/**
	 * Start the view.
	 */
	void start();

	/**
	 * Attach a {@link InputHelper}.
	 *
	 * @param inputHelper
	 *            the inputHelper
	 */
	void attachMe(InputHelper inputHelper);

}
