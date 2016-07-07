package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.Scenes;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.ArcadeSelectionMenuItem;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuActionItem;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuSubMenu;
import edu.kit.informatik.ragnarok.logic.level.LevelManager;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class ArcadeMenuScene extends MenuScene {

	private MenuSubMenu menu;

	public ArcadeMenuScene(GameModel model) {
		super(model);
	}

	@Override
	public void init() {
		super.init();

		this.menu = new MenuSubMenu(this, "Arcade");
		this.menu.setPos(new Vec(GameConf.PIXEL_W / 2f, GameConf.PIXEL_H / 2f));

		for (int i = 0; i <= LevelManager.getLastArcadeLevelId(); i++) {

			final int id = i;
			MenuActionItem button = new ArcadeSelectionMenuItem(this, String.valueOf(id + 1), new Runnable() {
				@Override
				public void run() {
					ArcadeMenuScene.this.model.selectedArcadeId = id;
					ArcadeMenuScene.this.model.switchScene(Scenes.ARCADE);
				}

			});
			this.menu.addItem(button);
		}

		this.addGuiElement(this.menu);
		this.menu.select();
	}

	@Override
	public MenuItem getMenu() {
		return this.menu;
	}

}
