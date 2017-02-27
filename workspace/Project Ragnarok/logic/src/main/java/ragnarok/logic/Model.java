package ragnarok.logic;

import ragnarok.core.IScene;
import ragnarok.logic.gameelements.entities.Entity;
import ragnarok.logic.gui.menu.MenuItem;
import ragnarok.primitives.image.Filter;

/**
 * This Interface defines the Model for the MVC.
 *
 * @author Dominik Fuchss
 *
 */
public interface Model {
	/**
	 * Get the model.
	 *
	 * @return the model
	 */
	static Model getModel() {
		return new GameModel();
	}

	/**
	 * Get the Player.
	 *
	 * @return the player or {@code null} when not in {@link GameState#INGAME}
	 */
	Entity getPlayer();

	/**
	 * Get the Menu.
	 *
	 * @return the menu or {@code null} when not in {@link GameState#MENU}
	 */

	MenuItem getMenu();

	/**
	 * Start the model.
	 */
	void start();

	/**
	 * Get the current scene. The scene to be drawn.
	 *
	 * @return the currently active scene
	 */
	IScene getScene();

	/**
	 * Get the current state.
	 *
	 * @return the state
	 */
	GameState getState();

	/**
	 * Set a filter.
	 *
	 * @param f
	 *            the filter
	 */
	void setFilter(Filter f);

	/**
	 * Remove all filters.
	 */
	void removeFilter();

	/**
	 * Get the current filter & reset {@link #filterChanged()}.
	 *
	 * @return the filter or {@code null} if none set
	 */
	Filter getFilter();

	/**
	 * Indicates whether the filter has been changed.
	 *
	 * @return {@code true} if changed, {@code false} otherwise
	 */
	boolean filterChanged();

	/**
	 * This enum defines the different states of the game.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	public enum GameState {
		/**
		 * This state indicates that currently a menu is shown by the game.
		 */
		MENU,
		/**
		 * This state indicates that currently a level is shown by the game.
		 */
		INGAME
	}
}
