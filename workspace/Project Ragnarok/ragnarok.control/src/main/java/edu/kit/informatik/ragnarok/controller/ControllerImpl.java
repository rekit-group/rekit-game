package edu.kit.informatik.ragnarok.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.kit.informatik.ragnarok.controller.commands.Command;
import edu.kit.informatik.ragnarok.controller.commands.InputMethod;
import edu.kit.informatik.ragnarok.controller.commands.JumpCommand;
import edu.kit.informatik.ragnarok.controller.commands.MenuCommand;
import edu.kit.informatik.ragnarok.controller.commands.WalkCommand;
import edu.kit.informatik.ragnarok.gui.View;
import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.logic.Scenes;
import edu.kit.informatik.ragnarok.logic.scene.LevelScene;
import edu.kit.informatik.ragnarok.logic.scene.MenuScene;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.util.InputHelper;

/**
 * This is an implementation of an {@link Controller} of the MVC <br>
 * It handles all input Events
 *
 * @author Dominik Fuchß
 *
 */
class ControllerImpl implements Observer, Controller {
	/**
	 * Map Key-ID, State --> Command
	 */
	private Map<Integer, Command> mpCmd;
	/**
	 * The input helper
	 */
	private final InputHelperImpl helper;
	/**
	 * The model
	 */
	private Model model;

	/**
	 * Instantiate the Controller
	 *
	 * @param model
	 *            the model
	 */
	public ControllerImpl(Model model) {
		this.mpCmd = new HashMap<>();
		this.helper = new InputHelperImpl();
		this.helper.register(this);
		this.model = model;
	}

	/**
	 * Handle one key input event
	 *
	 * @param id
	 *            the key's id
	 */
	public void handleEvent(int id, InputMethod inputMethod) {
		// return if we do not have a command defined for this key
		if (!this.mpCmd.containsKey(id)) {
			System.err.println("Warning: No Event defined for Key-ID: " + id);
			return;
		}

		this.mpCmd.get(id).execute(inputMethod);
	}

	@Override
	public void start(View view) {
		this.helper.initialize(view);
	}

	@Override
	public void update() {
		Iterator<Integer> it = this.helper.getPressedKeyIterator();
		while (it.hasNext()) {
			this.handleEvent(it.next(), InputMethod.PRESS);
		}
		it = this.helper.getReleasedKeyIterator();
		while (it.hasNext()) {
			this.handleEvent(it.next(), InputMethod.RELEASE);
			it.remove();
		}
	}

	@Override
	public void sceneChanged(Scene s) {
		Scenes sc = Scenes.getByInstance(s);
		if (sc.isMenu()) {
			MenuScene ms = (MenuScene) s;
			this.mpCmd = new HashMap<>();
			this.mpCmd.put(InputHelper.ESCAPE, new MenuCommand(ms.getMenu(), MenuCommand.Dir.BACK));
			this.mpCmd.put(InputHelper.ENTER, new MenuCommand(ms.getMenu(), MenuCommand.Dir.SELECT));
			this.mpCmd.put(InputHelper.ARROW_UP, new MenuCommand(ms.getMenu(), MenuCommand.Dir.UP));
			this.mpCmd.put(InputHelper.ARROW_DOWN, new MenuCommand(ms.getMenu(), MenuCommand.Dir.DOWN));
			this.mpCmd.put(InputHelper.ARROW_LEFT, new MenuCommand(ms.getMenu(), MenuCommand.Dir.BACK));
			this.mpCmd.put(InputHelper.ARROW_RIGHT, new MenuCommand(ms.getMenu(), MenuCommand.Dir.SELECT));
		} else {
			LevelScene ls = (LevelScene) s;
			this.mpCmd = new HashMap<>();
			this.mpCmd.put(InputHelper.ARROW_UP, new JumpCommand(ls.getPlayer()));
			this.mpCmd.put(InputHelper.ARROW_LEFT, new WalkCommand(ls.getPlayer(), Direction.LEFT));
			this.mpCmd.put(InputHelper.ARROW_RIGHT, new WalkCommand(ls.getPlayer(), Direction.RIGHT));

		}
	}

}
