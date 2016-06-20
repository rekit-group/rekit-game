package edu.kit.informatik.ragnarok.logic;

import java.util.Iterator;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

public interface Model {

	static Model getModel() {
		return new GameModel();
	}

	Iterator<GameElement> getGameElementIterator();

	Entity getPlayer();

	String getCurrentBossText();

	int getScore();

	int getHighScore();

	float getCameraOffset();

	void start();

}
