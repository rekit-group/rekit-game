package edu.kit.informatik.ragnarok.core;

import java.util.Iterator;
import java.util.Map;

import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;

public interface IScene {

	Entity getPlayer();

	int getScore();

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

	void removeGuiElement(GuiElement e);

	void setCameraTarget(CameraTarget cameraTarget);

	float getCameraOffset();

	Object synchronize();

	Iterator<GameElement> getGameElementIterator();

	Iterator<GuiElement> getGuiElementIterator();

	long getTime();

	void end(boolean won);

	Model getModel();

	void logicLoop(float f);

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

	default void stop() {
	}

	default void restart() {
		this.init();
		this.start();
	}

	Map<Class<?>, Long> getGameElementDurations();

	int getGameElementCount();

	MenuItem getMenu();

	void togglePause();

	Iterator<GameElement> getOrderedGameElementIterator();

}
