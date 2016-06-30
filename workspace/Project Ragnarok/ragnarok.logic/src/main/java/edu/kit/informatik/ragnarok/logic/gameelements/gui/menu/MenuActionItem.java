package edu.kit.informatik.ragnarok.logic.gameelements.gui.menu;

import edu.kit.informatik.ragnarok.logic.scene.Scene;

public class MenuActionItem extends MenuItem {

	private Runnable selectAction;

	public MenuActionItem(Scene scene, String text, Runnable selectAction) {
		super(scene, text);
		this.selectAction = selectAction;
	}

	@Override
	public void select() {
		this.selected = true;
		this.selectAction.run();
	}

}
