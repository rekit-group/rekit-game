package edu.kit.informatik.ragnarok;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import edu.kit.informatik.ragnarok.controller.Controller;
import edu.kit.informatik.ragnarok.controller.InputHelper;
import edu.kit.informatik.ragnarok.gui.GameView;
import edu.kit.informatik.ragnarok.logic.GameModel;

/**
 * Game class that instantiates all necessary classes that are required for a game.
 * implements a singleton to prevent multiple instantiation.
 * @author Angelo Aracri
 * @version 1.0
 */
public class Game {
	
	/**
	 * The (only) saved instance of Game
	 */
	private static Game instance = null;
	
	/**
	 * Singleton method that always return the same instance of Game
	 * @return an instance of Game
	 */
	public static Game getGame() {
		if (Game.instance == null) {
			Game.instance = new Game();
		}
		return Game.instance;
	}
	
	/**
	 * Private constructor to prevent instantiation of this class from outside.
	 * Instantiates the View, the Model and the Controller, sets required references and starts the game
	 */
	private Game() {
		try {
			// Create MVC
			GameView view = new GameView();
			GameModel model = new GameModel();
			Controller controller = new Controller();

			// Instantiate InputHelper that requires a shell
			InputHelper.init(view.getCanvas());
			
			// Set References:
			// V----> M <----C
			view.setModel(model);
			controller.setModel(model);
			
			// Start MVC
			model.start();
			controller.start();
			view.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
