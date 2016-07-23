package edu.kit.informatik.ragnarok.logic;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.image.Filter;

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

	/**
	 * Set a filter
	 *
	 * @param f
	 *            the filter
	 */
	void setFilter(Filter f);

	/**
	 * Remove all filters
	 */
	void removeFilter();

	/**
	 * Get the current filter & reset {@link #filterChanged()}
	 *
	 * @return the filter or {@code null} if none set
	 */
	Filter getFilter();

	/**
	 * Indicates whether the filter has been changed
	 *
	 * @return {@code true} if changed, {@code false} otherwise
	 */
	boolean filterChanged();
}
