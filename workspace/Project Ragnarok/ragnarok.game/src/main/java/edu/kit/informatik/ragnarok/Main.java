package edu.kit.informatik.ragnarok;

import edu.kit.informatik.ragnarok.controller.Controller;
import edu.kit.informatik.ragnarok.controller.InputHelper;
import edu.kit.informatik.ragnarok.gui.GameView;
import edu.kit.informatik.ragnarok.logic.GameModel;

/**
 * Game class that instantiates all necessary classes that are required for a
 * game. implements a singleton to prevent multiple instantiation.
 *
 * @author Angelo Aracri
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
		GameModel model = new GameModel();
		GameView view = new GameView(model);
		Controller controller = new Controller(model);

		// Instantiate InputHelper that requires a shell
		InputHelper.attach(view.getCanvas());

		// Start MVC
		model.start();
		controller.start();
		view.start();

	}
}
