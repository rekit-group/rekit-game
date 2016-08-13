package edu.kit.informatik.ragnarok.logic.scene;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.kit.informatik.ragnarok.logic.GameModel;

public enum Scenes {
	MENU(0, MenuScene.class), INFINIT(1, InfiniteLevelScene.class), LOD(2, LevelOfTheDayScene.class), ARCADE(3, ArcadeLevelScene.class);

	private final int id;
	private final Class<? extends Scene> sceneClass;

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

	public int getId() {
		return this.id;
	}

	public Scene getNewScene(GameModel model, String[] options) {
		try {
			Method test = this.sceneClass.getDeclaredMethod("create", GameModel.class, String[].class);
			return (Scene) test.invoke(null, model, options);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new UnsupportedOperationException("Cant create a " + this.sceneClass.getName());
		}

	}
}