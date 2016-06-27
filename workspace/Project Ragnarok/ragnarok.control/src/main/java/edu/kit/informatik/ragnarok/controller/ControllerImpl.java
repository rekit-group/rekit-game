package edu.kit.informatik.ragnarok.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.kit.informatik.ragnarok.controller.commands.Command;
import edu.kit.informatik.ragnarok.controller.commands.InputMethod;
import edu.kit.informatik.ragnarok.controller.commands.JumpCommand;
import edu.kit.informatik.ragnarok.controller.commands.WalkCommand;
import edu.kit.informatik.ragnarok.gui.View;
import edu.kit.informatik.ragnarok.logic.GameState;
import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.util.InputHelper;
import edu.kit.informatik.ragnarok.util.Tuple;

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
	private final Map<Tuple<Integer, GameState>, Command> mpCmd;
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
		this.init();
	}

	/**
	 * Create mapping for the {@link #mpCmd}
	 *
	 *
	 */
	private void init() {
		this.mpCmd.put(Tuple.create(InputHelper.ARROW_UP, GameState.INGAME), new JumpCommand(this.model.getScene().getPlayer()));
		this.mpCmd.put(Tuple.create(InputHelper.ARROW_LEFT, GameState.INGAME), new WalkCommand(this.model.getScene().getPlayer(), Direction.LEFT));
		this.mpCmd.put(Tuple.create(InputHelper.ARROW_RIGHT, GameState.INGAME), new WalkCommand(this.model.getScene().getPlayer(), Direction.RIGHT));
	}

	/**
	 * Handle one key input event
	 *
	 * @param id
	 *            the key's id
	 */
	public void handleEvent(int id, InputMethod inputMethod) {
		// return if we do not have a command defined for this key
		Tuple<Integer, GameState> key = Tuple.create(id, this.model.getState());
		if (!this.mpCmd.containsKey(key)) {
			System.err.println("Warning: No Event defined for Key-ID: " + id + ", State: " + this.model.getState());
			return;
		}

		this.mpCmd.get(key).execute(inputMethod);
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
