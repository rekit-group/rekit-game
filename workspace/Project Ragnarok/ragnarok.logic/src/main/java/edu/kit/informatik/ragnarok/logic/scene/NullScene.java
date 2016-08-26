package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.logic.GameModel;

public class NullScene extends Scene {

	public NullScene(GameModel model) {
		super(model);
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public int getHighScore() {
		return 0;
	}

}
