package edu.kit.informatik.ragnarok;

import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.controller.Controller;
import edu.kit.informatik.ragnarok.gui.View;
import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.util.ReflectUtils;
import home.fox.visitors.Visitable;
import home.fox.visitors.Visitor;

/**
 * Game class that instantiates all necessary classes that are required for a
 * game. implements a singleton to prevent multiple instantiation.
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
		controller.start();
		view.start();

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
