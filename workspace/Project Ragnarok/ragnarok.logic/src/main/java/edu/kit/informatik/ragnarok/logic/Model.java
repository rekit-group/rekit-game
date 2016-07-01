package edu.kit.informatik.ragnarok.logic;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.scene.Scene;

/**
 * This Interface defines the Model for the MVC
 *
 * @author Dominik Fuchß
 *
 */
public interface Model {
	/**
	 * Get the model
	 *
	 * @return the model
	 */
	static Model getModel() {
		return new GameModel();
	}

	/**
	 * Get the Player
	 *
	 * @return the player or {@code null} when not in GAME
	 */
	Entity getPlayer();

	/**
	 * Get the Menu
	 *
	 * @return the menu or {@code null} when not in MENU
	 */

	MenuItem getMenu();

	/**
	 * Start the model
	 */
	void start();

	/**
	 * Get the current scene. The scene to be drawn.
	 *
	 * @return the currently active scene
	 */
	Scene getScene();

	/**
	 * Get the current state
	 * 
	 * @return the state
	 */
	GameState getState();

}
