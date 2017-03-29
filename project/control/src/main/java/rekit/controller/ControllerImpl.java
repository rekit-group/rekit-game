package rekit.controller;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rekit.config.GameConf;
import rekit.controller.commands.Command;
import rekit.controller.commands.CommandSupervisor;
import rekit.controller.commands.FilterCommand;
import rekit.controller.commands.InputMethod;
import rekit.controller.commands.JumpCommand;
import rekit.controller.commands.MenuCommand;
import rekit.controller.commands.MenuDirection;
import rekit.controller.commands.PlayPauseCommand;
import rekit.controller.commands.WalkCommand;
import rekit.gui.InputHelper;
import rekit.gui.View;
import rekit.logic.IScene;
import rekit.logic.Model;
import rekit.logic.Model.GameState;
import rekit.logic.filters.GrayScaleMode;
import rekit.logic.filters.InvertedMode;
import rekit.logic.filters.RandomMode;
import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gui.menu.MenuItem;
import rekit.primitives.geometry.Direction;
import rekit.util.Tuple;

/**
 * This is an implementation of an {@link Controller} of the MVC <br>
 * It handles all input Events.
 *
 * @author Dominik Fuchss
 *
 */
final class ControllerImpl implements Observer, Controller, CommandSupervisor {
	/**
	 * Map State, Key-ID --&gt; Command.
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
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ESCAPE), new MenuCommand(this, MenuDirection.BACK));
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ENTER), new MenuCommand(this, MenuDirection.SELECT));
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ARROW_UP), new MenuCommand(this, MenuDirection.UP));
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ARROW_DOWN), new MenuCommand(this, MenuDirection.DOWN));
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ARROW_LEFT), new MenuCommand(this, MenuDirection.LEFT));
		this.mpCmd.put(Tuple.create(GameState.MENU, InputHelper.ARROW_RIGHT), new MenuCommand(this, MenuDirection.RIGHT));

		// Game
		this.mpCmd.put(Tuple.create(GameState.INGAME, InputHelper.ARROW_UP), new JumpCommand(this));
		this.mpCmd.put(Tuple.create(GameState.INGAME, InputHelper.ARROW_LEFT), new WalkCommand(this, Direction.LEFT));
		this.mpCmd.put(Tuple.create(GameState.INGAME, InputHelper.ARROW_RIGHT), new WalkCommand(this, Direction.RIGHT));
		this.mpCmd.put(Tuple.create(GameState.INGAME, InputHelper.ESCAPE), new PlayPauseCommand(this));

		// pause menu
		this.mpCmd.put(Tuple.create(GameState.INGAME_PAUSED, InputHelper.ENTER), new MenuCommand(this, MenuDirection.SELECT));
		this.mpCmd.put(Tuple.create(GameState.INGAME_PAUSED, InputHelper.ARROW_UP), new MenuCommand(this, MenuDirection.UP));
		this.mpCmd.put(Tuple.create(GameState.INGAME_PAUSED, InputHelper.ARROW_DOWN), new MenuCommand(this, MenuDirection.DOWN));
		this.mpCmd.put(Tuple.create(GameState.INGAME_PAUSED, InputHelper.ARROW_LEFT), new MenuCommand(this, MenuDirection.LEFT));
		this.mpCmd.put(Tuple.create(GameState.INGAME_PAUSED, InputHelper.ARROW_RIGHT), new MenuCommand(this, MenuDirection.RIGHT));
		this.mpCmd.put(Tuple.create(GameState.INGAME_PAUSED, InputHelper.ESCAPE), new PlayPauseCommand(this));

		// end menu
		this.mpCmd.put(Tuple.create(GameState.INGAME_END, InputHelper.ESCAPE), new MenuCommand(this, MenuDirection.BACK));
		this.mpCmd.put(Tuple.create(GameState.INGAME_END, InputHelper.ENTER), new MenuCommand(this, MenuDirection.SELECT));
		this.mpCmd.put(Tuple.create(GameState.INGAME_END, InputHelper.ARROW_UP), new MenuCommand(this, MenuDirection.UP));
		this.mpCmd.put(Tuple.create(GameState.INGAME_END, InputHelper.ARROW_DOWN), new MenuCommand(this, MenuDirection.DOWN));
		this.mpCmd.put(Tuple.create(GameState.INGAME_END, InputHelper.ARROW_LEFT), new MenuCommand(this, MenuDirection.LEFT));
		this.mpCmd.put(Tuple.create(GameState.INGAME_END, InputHelper.ARROW_RIGHT), new MenuCommand(this, MenuDirection.RIGHT));

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
		return this.model.getScene();
	}

	@Override
	public boolean entityCommandAllowed() {
		return !this.model.getScene().isPaused();
	}
}
