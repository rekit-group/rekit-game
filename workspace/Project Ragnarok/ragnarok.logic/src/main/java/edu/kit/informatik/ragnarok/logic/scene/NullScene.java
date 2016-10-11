package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;

final class NullScene extends Scene {

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

	@Override
	public MenuItem getMenu() {
		return null;
	}

	public static Scene create(GameModel model, String[] options) {
		return new NullScene(model);
	}

}
