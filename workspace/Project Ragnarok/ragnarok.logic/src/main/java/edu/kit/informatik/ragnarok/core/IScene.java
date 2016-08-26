package edu.kit.informatik.ragnarok.core;

import java.util.Iterator;

import edu.kit.informatik.ragnarok.logic.Model;

public interface IScene {

	GameElement getPlayer();

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

}
