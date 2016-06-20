package edu.kit.informatik.ragnarok;

import edu.kit.informatik.ragnarok.controller.Controller;
import edu.kit.informatik.ragnarok.gui.View;
import edu.kit.informatik.ragnarok.logic.Model;

/**
 * Game class that instantiates all necessary classes that are required for a
 * game. implements a singleton to prevent multiple instantiation.
 *
 * @author Angelo Aracri
 * @author Dominik Fuchß
 * @version 1.0
 */
public class Main {

	/**
	 * Launches the application by starting the game
	 *
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		// Create MVC
		// Set References:
		// V----> M <----C
		Model model = Model.getModel();
		View view = View.getView(model);
		Controller controller = Controller.getController(model);

		// Instantiate InputHelper that requires a shell
		controller.getInputHelper().initialize(view);

		// Start MVC
		model.start();
		controller.start();
		view.start();

	}
}
