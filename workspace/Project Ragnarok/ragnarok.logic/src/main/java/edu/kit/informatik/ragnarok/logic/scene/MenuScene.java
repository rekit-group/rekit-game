package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.Scenes;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuActionItem;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuGrid;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuSubMenu;
import edu.kit.informatik.ragnarok.logic.level.LevelManager;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

public class MenuScene extends Scene {

	private MenuSubMenu menu;

	public MenuScene(GameModel model) {
		super(model);
	}

	public static Scene create(GameModel model, String[] options) {
		return new MenuScene(model);
	}

	@Override
	public void init() {
		super.init();

		this.menu = new MenuSubMenu(this, "Main Menu");
		this.menu.setPos(new Vec(GameConf.PIXEL_W / 2f, GameConf.PIXEL_H / 2f));

		MenuSubMenu play = new MenuSubMenu(this, "Play");

		MenuSubMenu settings = new MenuSubMenu(this, "Settings");

		MenuSubMenu about = new MenuSubMenu(this, "About");

		MenuActionItem inf = new MenuActionItem(this, "Infinite Fun", () -> MenuScene.this.model.switchScene(Scenes.INFINIT));

		MenuActionItem lod = new MenuActionItem(this, "Level of the Day", () -> MenuScene.this.model.switchScene(Scenes.LOD));

		MenuGrid arcade = new MenuGrid(this, "Arcade Mode", 6);
		arcade.setItemSize(new Vec(100, 100));

		for (int i = 0; i < LevelManager.getNumberOfArcadeLevels(); i++) {
			final int id = i;
			MenuActionItem button = new MenuActionItem(this, new Vec(80, 80), String.valueOf(id + 1), () -> {
				this.model.switchScene(Scenes.ARCADE, new String[] { "" + id });
			});
			arcade.addItem(button);
		}

		MenuSubMenu modPlay = new MenuSubMenu(this, "Mod Scenes");

		modPlay.addItem(new MenuActionItem(this, "no Mod Scenes :(", () -> {
		}));

		play.addItem(inf);
		play.addItem(lod);
		play.addItem(arcade);
		play.addItem(modPlay);

		settings.addItem(new MenuActionItem(this, "no Settings :/", () -> {
		}));

		about.addItem(new MenuActionItem(this, "under construction", () -> {
		}));

		this.menu.addItem(play);
		this.menu.addItem(settings);
		this.menu.addItem(about);

		this.addGuiElement(this.menu);
		this.menu.select();
	}

	public MenuItem getMenu() {
		return this.menu;
	}

}
