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

	void addGameElement(GameElement e);

	void removeGameElement(GameElement e);

	void addGuiElement(GuiElement e);

	void removeGuiElement(GuiElement e);

	void setCameraTarget(CameraTarget cameraTarget);

	float getCameraOffset();

	Object synchronize();

	Iterator<GameElement> getOrderedGameElementIterator();

	Iterator<GuiElement> getGuiElementIterator();

	long getTime();

	void end(boolean end);

	Model getModel();

	void logicLoop(float f);

	void init();

	void start();

	void stop();

	Iterator<GameElement> getGameElementIterator();

	Map<Class<?>, Long> getGameElementDurations();

	int getGameElementCount();

	MenuItem getMenu();

	void togglePause();

}
