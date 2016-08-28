package edu.kit.informatik.ragnarok.logic.scene;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

import edu.kit.informatik.ragnarok.logic.GameModel;

public enum Scenes {
	MENU(MenuScene.class), INFINIT(InfiniteLevelScene.class), LOD(LevelOfTheDayScene.class), ARCADE(ArcadeLevelScene.class);

	private final Class<? extends Scene> sceneClass;

	private Scenes(Class<? extends Scene> sceneClass) {
		this.sceneClass = sceneClass;
		if (ConcurrentHelper.INSTANCES.put(this.sceneClass, this) != null) {
			System.err.println("Warning: Multiple Scenes for class " + this.sceneClass);
		}
	}

	public static Scenes getByInstance(Scene scene) {
		return ConcurrentHelper.INSTANCES.get(scene.getClass());
	}

	public boolean isMenu() {
		return this.sceneClass.isAssignableFrom(MenuScene.class);
	}

	public Scene getNewScene(GameModel model, String[] options) {
		try {
			return (Scene) this.sceneClass.getDeclaredMethod("create", GameModel.class, String[].class).invoke(null, model, options);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new UnsupportedOperationException("Cant create a " + this.sceneClass.getName());
		}

	}

	/**
	 * Static context in constructor of enums is quite difficult so we use a
	 * helper class
	 *
	 * @author Dominik Fuchß
	 *
	 */
	private static class ConcurrentHelper {
		private static final ConcurrentHashMap<Class<? extends Scene>, Scenes> INSTANCES = new ConcurrentHashMap<>();
	}
}
