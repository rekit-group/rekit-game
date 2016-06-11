package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.player.Player;

public abstract class InputCommand {
	private GameModel model;

	public void setModel(GameModel value) {
		this.model = value;
	}

	public GameModel getModel() {
		return this.model;
	}

	public abstract void apply();

}
