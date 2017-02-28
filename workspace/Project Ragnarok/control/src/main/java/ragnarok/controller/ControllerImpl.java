package ragnarok.controller;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ragnarok.config.GameConf;
import ragnarok.controller.commands.Command;
import ragnarok.controller.commands.CommandSupervisor;
import ragnarok.controller.commands.FilterCommand;
import ragnarok.controller.commands.InputMethod;
import ragnarok.controller.commands.JumpCommand;
import ragnarok.controller.commands.MenuCommand;
import ragnarok.controller.commands.PlayPauseCommand;
import ragnarok.controller.commands.WalkCommand;
import ragnarok.gui.View;
import ragnarok.logic.IScene;
import ragnarok.logic.Model;
import ragnarok.logic.Model.GameState;
import ragnarok.logic.filters.GrayScaleMode;
import ragnarok.logic.filters.InvertedMode;
import ragnarok.logic.filters.RandomMode;
import ragnarok.logic.gameelements.entities.Entity;
import ragnarok.logic.gui.menu.MenuItem;
import ragnarok.primitives.geometry.Direction;
import ragnarok.util.InputHelper;
import ragnarok.util.Tuple;

/**
 * This is an implementation of an {@link Controller} of the MVC <br>
 * It handles all input Events.
 *
 * @author Dominik Fuchss
 *
 */
final class ControllerImpl implements Observer, Controller, CommandSupervisor {
	/**
	 * Map State, Key-ID --> Command.
	 */
	private Map<Tuple<GameState, Integer>, Command> mpCmd;
	/**
	 * The input helper.
	 */
	private final InputHelperImpl helper;
	/**
	 * The model.
	 */
	private final Model model;
	/**
	 * The view.
	 */
	private final View view;

	/**
	 * Instantiate the Controller.
	 *
	 * @param model
	 *            the model
	 * @param view
	 *            the view
	 */
	public ControllerImpl(Model model, View view) {
		this.mpCmd = new HashMap<>();
		this.helper = new InputHelperImpl();
		this.model = model;
		this.view = view;
		this.init();
	}

	/**
	 * Initialize all commands.
	 */
	private void init() {
		// Menu
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ESCAPE), new MenuCommand(this, MenuCommand.Dir.BACK));
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ENTER), new MenuCommand(this, MenuCommand.Dir.SELECT));
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ARROW_UP), new MenuCommand(this, MenuCommand.Dir.UP));
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ARROW_DOWN), new MenuCommand(this, MenuCommand.Dir.DOWN));
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ARROW_LEFT), new MenuCommand(this, MenuCommand.Dir.LEFT));
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ARROW_RIGHT), new MenuCommand(this, MenuCommand.Dir.RIGHT));

		// Game
		this.mpCmd.put(Tuple.create(GameState.INGAME, InputHelper.ARROW_UP), new JumpCommand(this));
		this.mpCmd.put(Tuple.create(GameState.INGAME, InputHelper.ARROW_LEFT), new WalkCommand(this, Direction.LEFT));
		this.mpCmd.put(Tuple.create(GameState.INGAME, InputHelper.ARROW_RIGHT), new WalkCommand(this, Direction.RIGHT));
		this.mpCmd.put(Tuple.create(GameState.INGAME, InputHelper.ESCAPE), new PlayPauseCommand(this));

		// Filter Commands ... a test ('u', 'i', 'o' and 'p' key)
		this.mpCmd.put(Tuple.create(null, KeyEvent.VK_U), new FilterCommand(true, this.model, RandomMode.INSTANCE));
		this.mpCmd.put(Tuple.create(null, KeyEvent.VK_I), new FilterCommand(true, this.model, new InvertedMode()));
		this.mpCmd.put(Tuple.create(null, KeyEvent.VK_O), new FilterCommand(true, this.model, new GrayScaleMode()));
		this.mpCmd.put(Tuple.create(null, KeyEvent.VK_P), new FilterCommand(false, this.model, null));

	}

	/**
	 * Handle one key input event.
	 *
	 * @param id
	 *            the key's id
	 * @param inputMethod
	 *            {@link InputMethod#RELEASE} or {@link InputMethod#PRESS} or
	 *            {@code null}
	 */
	public void handleEvent(int id, InputMethod inputMethod) {
		Tuple<GameState, Integer> key = Tuple.create(this.model.getState(), id);
		Tuple<GameState, Integer> idKey = Tuple.create(null, id);
		// return if we do not have a command defined for this key
		if (this.mpCmd.containsKey(key)) {
			this.mpCmd.get(key).execute(inputMethod);
			return;
		}
		if (this.mpCmd.containsKey(idKey)) {
			this.mpCmd.get(idKey).execute(inputMethod);
			return;
		}
		GameConf.GAME_LOGGER.debug("No Event defined for Key-ID: " + id + " State: " + this.model.getState());

	}

	@Override
	public void start() {
		this.helper.initialize(this.view);
		this.helper.register(this);
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
	public Entity getEntity(Command command) {
		return this.model.getPlayer();
	}

	@Override
	public MenuItem getMenu(Command command) {
		return this.model.getMenu();
	}

	@Override
	public IScene getScene() {
		return this.model.getState() == GameState.INGAME ? this.model.getScene() : null;
	}

	@Override
	public boolean entityCommandAllowed() {
		return !this.model.getScene().isPaused();
	}
}
