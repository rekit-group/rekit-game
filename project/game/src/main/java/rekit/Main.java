package rekit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.Setter;
import org.fuchss.configuration.parser.Parser;
import org.fuchss.configuration.setters.ResourceBundleSetter;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import rekit.config.GameConf;
import rekit.controller.Controller;
import rekit.gui.View;
import rekit.logic.Model;
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
		Main.setLogLevel(GameConf.DEBUG ? Level.ALL : Level.FATAL);
		GameConf.GAME_LOGGER.debug(ModManager.SYSLOADER.getClass().getSimpleName() + " (Sysloader loaded.)");
		Main.applyAllConfigs();
		LevelManager.init();

		// Create MVC
		// Set References:
		// V----> M <----C
		// ^-------------|
		Model model = Model.getModel();
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
	 * Set log level for loggers.
	 *
	 * @param level
	 *            the level
	 */
	private static void setLogLevel(Level level) {
		// Initialize Loggers ...

		GameConf.GAME_LOGGER.setLevel(level);
		Setter.LOGGER.setLevel(level);
		Parser.LOGGER.setLevel(level);
		Logger.getLogger(PathMatchingResourcePatternResolver.class).setLevel(level);

		// This is needed as all loggers add an appender to the rootLogger.
		Logger.getRootLogger().removeAllAppenders();
		BasicConfigurator.configure();
	}

	/**
	 * Visit all Classes which shall be visited.
	 */
	private static void applyAllConfigs() {
		ResourceBundleSetter setter = new ResourceBundleSetter(ModManager.SYSLOADER);
		Main.applyAllConfigs(setter);
	}

	/**
	 * Visit all Classes which shall be setted.
	 *
	 * @param setter
	 *            the setter
	 */
	private static void applyAllConfigs(Setter setter) {
		ReflectUtils.getClasses(GameConf.SEARCH_PATH, ModManager.SYSLOADER, Configurable.class).forEach(c -> setter.setAttributes(c));
	}
}
