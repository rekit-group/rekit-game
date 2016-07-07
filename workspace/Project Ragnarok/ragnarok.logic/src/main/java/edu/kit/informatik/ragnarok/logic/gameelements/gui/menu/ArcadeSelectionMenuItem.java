package edu.kit.informatik.ragnarok.logic.gameelements.gui.menu;

import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class ArcadeSelectionMenuItem extends MenuActionItem {

	public ArcadeSelectionMenuItem(Scene scene, String text, Runnable selectAction) {
		super(scene, text, selectAction);
		this.setSize(new Vec(80, 80));
	}

}
