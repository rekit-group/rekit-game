package edu.kit.informatik.ragnarok.logic;

import java.lang.reflect.Method;

import edu.kit.informatik.ragnarok.logic.scene.ArcadeLevelScene;
import edu.kit.informatik.ragnarok.logic.scene.InfiniteLevelScene;
import edu.kit.informatik.ragnarok.logic.scene.LevelOfTheDayScene;
import edu.kit.informatik.ragnarok.logic.scene.MenuScene;
import edu.kit.informatik.ragnarok.logic.scene.Scene;

public enum Scenes {
	MENU(0, MenuScene.class), INFINIT(1, InfiniteLevelScene.class), LOD(2, LevelOfTheDayScene.class), ARCADE(3, ArcadeLevelScene.class);

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
		return this.sceneClass.isAssignableFrom(MenuScene.class);
	}

	public Scene getNewScene(GameModel model, String[] options) {
		try {
			Method test = this.sceneClass.getDeclaredMethod("create", GameModel.class, String[].class);
			return (Scene) test.invoke(null, model, options);
		} catch (Exception e) {
			throw new UnsupportedOperationException("Cant create a " + this.sceneClass.getName());
		}

	}
}
