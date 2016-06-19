package edu.kit.informatik.ragnarok.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;

import edu.kit.informatik.ragnarok.controller.commands.InputCommand;
import edu.kit.informatik.ragnarok.controller.commands.InputCommand.InputMethod;
import edu.kit.informatik.ragnarok.controller.commands.JumpCommand;
import edu.kit.informatik.ragnarok.controller.commands.WalkCommand;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.primitives.Direction;

/**
 * This is the Controller of the MVC <br>
 * It handles all input Events
 * 
 * @author Dominik Fuchß
 *
 */
public class Controller implements Observer {
	/**
	 * Map Key-ID --> Command
	 */
	private final Map<Integer, InputCommand> mpCmd;

	/**
	 * Instantiate the Controller
	 * 
	 * @param model
	 *            the model
	 */
	public Controller(GameModel model) {
		this.mpCmd = new HashMap<Integer, InputCommand>();
		this.init(model);
	}

	/**
	 * Create mapping for the {@link #mpCmd}
	 * 
	 * @param model
	 *            the model
	 */
	private void init(GameModel model) {
		this.mpCmd.put(SWT.ARROW_UP, new JumpCommand().setEntity(model.getPlayer()));
		this.mpCmd.put(SWT.ARROW_LEFT, new WalkCommand(Direction.LEFT).setEntity(model.getPlayer()));
		this.mpCmd.put(SWT.ARROW_RIGHT, new WalkCommand(Direction.RIGHT).setEntity(model.getPlayer()));
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
		
		this.mpCmd.get(id).apply(inputMethod);
	}

	/**
	 * Start the Controller
	 */
	public void start() {
		// Register me! --> update is called whenever something changes
		InputHelper.register(this);
	}

	@Override
	public void update() {
		Iterator<Integer> it = InputHelper.getPressedKeyIterator();
		while (it.hasNext()) {
			this.handleEvent(it.next(), InputMethod.PRESS);
		}
		
		it = InputHelper.getReleasedKeyIterator();
		while (it.hasNext()) {
			this.handleEvent(it.next(), InputMethod.RELEASE);
			it.remove();
		}
	}

}
