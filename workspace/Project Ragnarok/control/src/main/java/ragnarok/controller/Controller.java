package ragnarok.controller;

import ragnarok.gui.View;
import ragnarok.logic.Model;

/**
 * This Interface defines the Controller for the MVC.
 *
 * @author Dominik Fuchss
 *
 */
public interface Controller {
	/**
	 * Get the controller.
	 *
	 * @param model
	 *            the model
	 * @param view
	 *            the view to attach all controls
	 * @return the controller
	 */
	static Controller getController(Model model, View view) {
		return new ControllerImpl(model, view);
	}

	/**
	 * Start the Controller.
	 */
	void start();

}
