package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuItem;

public class MenuCommand implements Command {
	public enum Dir {
		UP, DOWN, BACK, SELECT
	}

	private MenuItem item;

	private Dir dir;

	public MenuCommand(MenuItem item, Dir dir) {
		this.item = item;
		this.dir = dir;
	}

	@Override
	public void execute(InputMethod inputMethod) {
		if (inputMethod != InputMethod.RELEASE) {
			return;
		}

		switch (this.dir) {
		case UP:
			this.item.up();
			break;

		case DOWN:
			this.item.down();
			break;

		case BACK:
			this.item.back();
			break;

		case SELECT:
			this.item.select();
			break;

		default:
			break;
		}
	}

}
