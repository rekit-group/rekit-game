package edu.kit.informatik.ragnarok.logic;

import edu.kit.informatik.ragnarok.logic.scene.LevelScene;
import edu.kit.informatik.ragnarok.logic.scene.MenuScene;
import edu.kit.informatik.ragnarok.logic.scene.Scene;

public enum Scenes {
	MENU(0, MenuScene.class), INFINIT(1, LevelScene.class), LOD(2, LevelScene.class);

	protected final int id;
	protected final Class<? extends Scene> sceneClass;

	private Scenes(int id, Class<? extends Scene> sceneClass) {
		this.id = id;
		this.sceneClass = sceneClass;
	}

	public static Scenes getByInstance(Scene scene) {
		for (Scenes it : Scenes.values()) {
			if (it.sceneClass == scene.getClass()) {
				return it;
			}
		}
		return null;
	}

	public boolean isMenu() {
		return this == Scenes.MENU;
	}
}
