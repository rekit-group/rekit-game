package rekit;

import java.util.Set;

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
import rekit.util.ReflectUtils;
import rekit.util.ThreadUtils;

/**
 * Game class that instantiates all necessary classes that are required for a
 * game.
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 * @version 1.0
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
		// Load All Configs
		Main.visitAllStatic();
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
	private static final void setLogLevel(Level level) {
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
	private static final void visitAllStatic() {
		Main.visitAllStatic(new ResourceBundleSetter());
	}

	/**
	 * Visit all Classes which shall be setted.
	 *
	 * @param setter
	 *            the setter
	 */
	private static final void visitAllStatic(Setter setter) {
		Set<Class<? extends Configurable>> toSet = ReflectUtils.getClasses(GameConf.SEARCH_PATH, Configurable.class);
		for (Class<? extends Configurable> s : toSet) {
			setter.setAttributes(s);
		}
	}
}
