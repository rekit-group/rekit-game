package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.Scenes;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuActionItem;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuGrid;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuList;
import edu.kit.informatik.ragnarok.logic.gui.menu.SettingToggle;
import edu.kit.informatik.ragnarok.logic.gui.menu.SubMenu;
import edu.kit.informatik.ragnarok.logic.level.LevelManager;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

public class MenuScene extends Scene {

	private SubMenu menu;

	public MenuScene(GameModel model) {
		super(model);
	}

	public static Scene create(GameModel model, String[] options) {
		return new MenuScene(model);
	}

	@Override
	public void init() {
		super.init();

		this.menu = new MenuList(this, "Main Menu");
		this.menu.setPos(new Vec(GameConf.PIXEL_W / 2f, GameConf.PIXEL_H / 2f));

		MenuList play = new MenuList(this, "Play");

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

		MenuList modPlay = new MenuList(this, "Mod Scenes");

		modPlay.addItem(new MenuActionItem(this, "no Mod Scenes :(", () -> {
		}));

		play.addItem(inf);
		play.addItem(lod);
		play.addItem(arcade);
		play.addItem(modPlay);

		MenuList settings = new MenuList(this, "Settings");

		settings.addItem(new SettingToggle(this, "Debug Mode", "DEBUG"));

		MenuList about = new MenuList(this, "About");

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
