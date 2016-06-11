package edu.kit.informatik.ragnarok;

import edu.kit.informatik.ragnarok.gui.GameView;
import edu.kit.informatik.ragnarok.gui.MainFrame;
import edu.kit.informatik.ragnarok.logic.GameModel;

public class Game {
	
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
	 * private constructor to prevent instantiation of this class from outside
	 */
	private Game() {
		try {
			GameView view = new GameView();
			GameModel model = new GameModel();
			
			view.setModel(model);
			view.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}