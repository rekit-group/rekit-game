package rekit.logic.scene;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

import rekit.config.GameConf;
import rekit.logic.GameModel;
import rekit.logic.IScene;

/**
 *
 * This enum manages all different types of Scenes in the game.
 *
 */
public enum Scenes {
	/**
	 * A placeholder scene.
	 */
	NULL(NullScene.class),
	/**
	 * A Menu.
	 */
	MENU(MainMenuScene.class),
	/**
	 * An infinite level.
	 */
	INFINITE(InfiniteLevelScene.class),
	/**
	 * A level of the day.
	 */
	LOD(LevelOfTheDayScene.class),
	/**
	 * An arcade level.
	 */
	ARCADE(ArcadeLevelScene.class),
	/**
	 * An boss rush level.
	 */
	BOSS_RUSH(BossRushScene.class);
	/**
	 * The scene class.
	 */
	private final Class<? extends IScene> sceneClass;

	/**
	 * Create a scene type by class.
	 *
	 * @param sceneClass
	 *            the scene class
	 */
	private Scenes(Class<? extends IScene> sceneClass) {
		this.sceneClass = sceneClass;
		if (ConcurrentHelper.INSTANCES.put(this.sceneClass, this) != null) {
			GameConf.GAME_LOGGER.warn("Multiple Scenes for class " + this.sceneClass);
		}
	}

	/**
	 * Get the corresponding instance of the scene.
	 *
	 * @param scene
	 *            the scene
	 * @return the Scenes Type
	 */
	public static Scenes getByInstance(IScene scene) {
		return ConcurrentHelper.INSTANCES.get(scene.getClass());
	}

	/**
	 * Indicates whether this scene is a menu.
	 *
	 * @return {@code true} if it is a menu, {@code false} otherwise
	 */
	public boolean isMenu() {
		return this.sceneClass.isAssignableFrom(MainMenuScene.class);
	}

	/**
	 * Get a new scene of a type.
	 *
	 * @param model
	 *            the model
	 * @param options
	 *            the options
	 * @return the scene
	 */
	public Scene getNewScene(GameModel model, String... options) {
		try {
			return (Scene) this.sceneClass.getDeclaredMethod("create", GameModel.class, String[].class).invoke(null, model, options);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			GameConf.GAME_LOGGER.fatal("Cannot load Scene for Level. See log for more info.");
			return null;
		}

	}

	/**
	 * Static context in constructor of enums is quite difficult so we use a
	 * helper class.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	private static class ConcurrentHelper {
		/**
		 * The mapping of IScene --&gt; Scenes.
		 */
		private static final ConcurrentHashMap<Class<? extends IScene>, Scenes> INSTANCES = new ConcurrentHashMap<>();
	}
}
