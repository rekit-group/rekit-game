package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.level.LevelManager;

public class LevelOfTheDayScene extends LevelScene {

	public LevelOfTheDayScene(GameModel model) {
		super(model, LevelManager.getLOTDLevel());
	}

	public static Scene create(GameModel model, String[] options) {
		return new LevelOfTheDayScene(model);
	}

}
