package edu.kit.informatik.ragnarok.core;

import java.util.Iterator;
import java.util.Map;

import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.scene.Scenes;

/**
 * This is the public interface of all Scenes.
 *
 * @author Dominik Fuchß
 *
 */
public interface IScene {
	/**
	 * Get the current player of {@code null} if none set.
	 *
	 * @return the current player of {@code null}
	 */
	Entity getPlayer();

	/**
	 * Get the current score of the player.
	 *
	 * @return the current score or 0 if none available
	 */
	int getScore();

	/**
	 * Get the current high score for the current level.
	 *
	 * @return the current high score or 0 if none available
	 */
	int getHighScore();

	/**
	 * Adds a GameElement to the Model. The elements will not directly be added
	 * to the internal data structure to prevent concurrency errors. Instead
	 * there is an internal list to hold all waiting GameElements that will be
	 * added in the next call of logicLoop
	 *
	 * @param e
	 *            the GameElement to add
	 */
	void addGameElement(GameElement e);

	/**
	 * Removes a GameElement from the Model The elements will not directly be
	 * removed from the internal data structure to prevent concurrency errors.
	 * Instead there is an internal list to hold all waiting GameElements that
	 * will be removed in the next call of logicLoop
	 *
	 * @param e
	 *            the GameElement to remove
	 */
	void removeGameElement(GameElement e);

	/**
	 * Adds a GuiElement to the GameModel.
	 *
	 * @param e
	 *            the GuiElement to add
	 */
	void addGuiElement(GuiElement e);

	/**
	 * Removes a GuiElement to the GameModel.
	 *
	 * @param e
	 *            the GuiElement to remove
	 */
	void removeGuiElement(GuiElement e);

	/**
	 * Set the camera target.
	 *
	 * @param cameraTarget
	 *            the camera target
	 */
	void setCameraTarget(CameraTarget cameraTarget);

	/**
	 * Get the current camera offset.
	 *
	 * @return the current camera offset
	 */
	float getCameraOffset();

	/**
	 * The Object to synchronize with the scene (threading).
	 *
	 * @return the sync-object
	 */
	Object synchronize();

	/**
	 * Get a game-element iterator.
	 *
	 * @return a game-element iterator
	 */
	Iterator<GameElement> getGameElementIterator();

	/**
	 * Get a gui-element iterator.
	 *
	 * @return a gui-element iterator
	 */
	Iterator<GuiElement> getGuiElementIterator();

	/**
	 * Get the current time in millis.
	 *
	 * @return the current time in millis
	 */
	// TODO Check millis
	long getTime();

	/**
	 * End a Game/Scene.
	 *
	 * @param won
	 *            indicates whether successfull or died
	 */
	void end(boolean won);

	/**
	 * Get the model of the MVC.
	 *
	 * @return the model
	 */
	Model getModel();

	/**
	 * Invoke logic.
	 *
	 * @param lastTime
	 *            the last time in millis
	 */
	// TODO Check whether millis
	void logicLoop(long lastTime);

	/**
	 * Initialize the scene. e.g. build Level/GUI so Scene is ready to be drawn
	 * Must be called on restart.
	 */
	void init();

	/**
	 * Start the scene. Begin drawing and Player/Enemies will begin to move.
	 */
	default void start() {
	}

	/**
	 * Stop the scene. End drawing and Player/Enemies will end to move.
	 */
	default void stop() {
	}

	/**
	 * Restart the scene.
	 */
	default void restart() {
		this.init();
		this.start();
	}

	/**
	 * Get a map of duration-time of elements.
	 *
	 * @return the duration-time of elements by class
	 */
	Map<Class<?>, Long> getGameElementDurations();

	/**
	 * Get the amount of elements in the scene.
	 *
	 * @return the amount of elements
	 */
	int getGameElementCount();

	/**
	 * Get the associated Root-MenuItem.
	 *
	 * @return the root-menuItem or {@code null} if no {@link Scenes#MENU}
	 */
	MenuItem getMenu();

	/**
	 * Toggle pause.
	 */
	void togglePause();

	/**
	 * Get an iterator of ordered GameElements (Z-Idx).
	 *
	 * @return the ordered GameElement-iterator
	 */
	Iterator<GameElement> getOrderedGameElementIterator();

}
