package edu.kit.informatik.ragnarok.controller;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.Player;

public abstract class InputCommand {
	private GameModel model;

	protected void setModel(GameModel value) {
		this.model = value;
	}

	protected GameModel getModel() {
		return this.model;
	}

	public abstract void apply(Player player);

}
