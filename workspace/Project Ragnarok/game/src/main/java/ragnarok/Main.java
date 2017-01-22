package ragnarok;

import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import home.fox.visitors.Visitable;
import home.fox.visitors.Visitor;
import home.fox.visitors.parser.Parser;
import ragnarok.config.GameConf;
import ragnarok.controller.Controller;
import ragnarok.gui.View;
import ragnarok.logic.Model;
import ragnarok.util.ReflectUtils;
import ragnarok.util.ThreadUtils;

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
		Main.setLogLevel(GameConf.DEBUG ? Level.ALL : Level.INFO);
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
		Visitor.LOGGER.setLevel(level);
		Parser.LOGGER.setLevel(level);

		// This is needed as all loggers add an appender to the rootLogger.
		Logger.getRootLogger().removeAllAppenders();
		BasicConfigurator.configure();
	}

	/**
	 * Visit all Classes which shall be visited.
	 */
	private static final void visitAllStatic() {
		Main.visitAllStatic(Visitor.getNewVisitor());
	}

	/**
	 * Visit all Classes which shall be visited.
	 *
	 * @param visitor
	 *            the visitor
	 */
	private static final void visitAllStatic(Visitor visitor) {
		Set<Class<? extends Visitable>> toVisit = ReflectUtils.getClasses(GameConf.SEARCH_PATH, Visitable.class);
		for (Class<? extends Visitable> v : toVisit) {
			visitor.visit(v);
		}
	}
}
