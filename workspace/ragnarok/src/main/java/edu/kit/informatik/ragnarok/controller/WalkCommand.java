package edu.kit.informatik.ragnarok.controller;

import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.gameelements.Player;

public class WalkCommand extends InputCommand {
	private Direction dir;

	private void setDir(Direction value) {
		this.dir = value;
	}

	private Direction getDir() {
		return this.dir;
	}

	public WalkCommand(Direction dir) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public void apply(Player player) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
	}

}