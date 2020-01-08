package rekit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.Setter;
import org.fuchss.configuration.setters.ResourceBundleSetter;

import rekit.config.GameConf;
import rekit.gui.View;
import rekit.gui.controller.Controller;
import rekit.logic.Model;
import rekit.logic.develop.TestScene;
import rekit.persistence.ModManager;
import rekit.persistence.level.LevelManager;
import rekit.util.ReflectUtils;
import rekit.util.ThreadUtils;

/**
 * Game class that instantiates all necessary classes that are required for a
 * game.
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 */
public final class Main {
	/**
	 * Prevent instantiation.
	 */
	private Main() {
	}

	/**
	 * Launches the application by starting the game.
	 *
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		Thread.currentThread().setName("Startup");
		Configurator.setRootLevel(GameConf.DEBUG ? Level.ALL : Level.FATAL);
		GameConf.GAME_LOGGER.debug(ModManager.SYSLOADER.getClass().getSimpleName() + " (Sysloader loaded.)");
		Main.applyAllConfigs();
		LevelManager.init();

		// Create MVC
		// Set References:
		// V----> M <----C
		// ^-------------|
		Model model = Model.getModel();
		Main.applyDebug(model);
		View view = View.getView(model);
		Controller controller = Controller.getController(model, view);

		// Start MVC
		model.start();
		ThreadUtils.sleep(100);
		controller.start();
		ThreadUtils.sleep(100);
		view.start();

	}

	/**
	 * Apply debug stuff to model ..
	 *
	 * @param model
	 *            the model
	 */
	private static void applyDebug(Model model) {
		if (!GameConf.DEBUG) {
			return;
		}
		model.registerTestScene((m) -> TestScene.create(m));
	}

	/**
	 * Visit all Classes which shall be visited.
	 */
	private static void applyAllConfigs() {
		ResourceBundleSetter setter = new ResourceBundleSetter(ModManager.SYSLOADER);
		Main.applyAllConfigs(setter);
	}

	/**
	 * Visit all Classes which shall be set.
	 *
	 * @param setter
	 *            the setter
	 */
	private static void applyAllConfigs(Setter setter) {
		ReflectUtils.getClasses(GameConf.SEARCH_PATH, ModManager.SYSLOADER, Configurable.class).forEach(c -> setter.setAttributes(c));
	}
}
