package edu.kit.informatik.ragnarok.controller.commands;

import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuItem;

public class MenuCommand implements Command {
	public enum Dir {
		UP, DOWN, BACK, SELECT
	}

	private CommandSupervisor supervisor;

	private Dir dir;

	public MenuCommand(CommandSupervisor supervisor, Dir dir) {
		this.supervisor = supervisor;
		this.dir = dir;
	}

	@Override
	public void execute(InputMethod inputMethod) {
		if (inputMethod != InputMethod.RELEASE) {
			return;
		}
		MenuItem item = this.supervisor.getMenu(this);
		switch (this.dir) {
		case UP:
			item.up();
			break;
		case DOWN:
			item.down();
			break;
		case BACK:
			item.back();
			break;
		case SELECT:
			item.select();
			break;
		default:
			break;
		}
	}

}
