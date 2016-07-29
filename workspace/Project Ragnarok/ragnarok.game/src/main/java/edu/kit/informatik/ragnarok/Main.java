package edu.kit.informatik.ragnarok;

import edu.kit.informatik.ragnarok.controller.Controller;
import edu.kit.informatik.ragnarok.gui.View;
import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawnerOptionParser;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawnerParser;
import edu.kit.informatik.ragnarok.visitor.Visitor;

/**
 * Game class that instantiates all necessary classes that are required for a
 * game. implements a singleton to prevent multiple instantiation.
 *
 * @author Angelo Aracri
 * @author Dominik Fuchß
 * @version 1.0
 */
public class Main {

	/**
	 * Launches the application by starting the game
	 *
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		// Load All Configs
		Main.loadConfigs();
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
	 * Load all configs
	 */
	private static void loadConfigs() {
		Visitor visitor = Visitor.getNewVisitor();
		visitor.setParser(ParticleSpawnerOption.class, new ParticleSpawnerOptionParser());
		visitor.setParser(ParticleSpawner.class, new ParticleSpawnerParser());
		Visitor.visitAllStatic(visitor);
	}
}
