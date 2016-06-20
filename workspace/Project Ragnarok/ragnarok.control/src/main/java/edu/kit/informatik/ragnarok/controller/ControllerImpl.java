package edu.kit.informatik.ragnarok.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.kit.informatik.ragnarok.controller.commands.InputCommand;
import edu.kit.informatik.ragnarok.controller.commands.InputCommand.InputMethod;
import edu.kit.informatik.ragnarok.controller.commands.JumpCommand;
import edu.kit.informatik.ragnarok.controller.commands.WalkCommand;
import edu.kit.informatik.ragnarok.gui.View;
import edu.kit.informatik.ragnarok.logic.Model;
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
	 * Map Key-ID --> Command
	 */
	private final Map<Integer, InputCommand> mpCmd;
	/**
	 * The input helper
	 */
	private final InputHelperImpl helper;

	/**
	 * Instantiate the Controller
	 *
	 * @param model
	 *            the model
	 */
	public ControllerImpl(Model model) {
		this.mpCmd = new HashMap<Integer, InputCommand>();
		this.helper = new InputHelperImpl();
		this.helper.register(this);
		this.init(model);
	}

	/**
	 * Create mapping for the {@link #mpCmd}
	 *
	 * @param model
	 *            the model
	 */
	private void init(Model model) {
		this.mpCmd.put(InputHelper.ARROW_UP, new JumpCommand().setEntity(model.getPlayer()));
		this.mpCmd.put(InputHelper.ARROW_LEFT, new WalkCommand(Direction.LEFT).setEntity(model.getPlayer()));
		this.mpCmd.put(InputHelper.ARROW_RIGHT, new WalkCommand(Direction.RIGHT).setEntity(model.getPlayer()));
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

}
