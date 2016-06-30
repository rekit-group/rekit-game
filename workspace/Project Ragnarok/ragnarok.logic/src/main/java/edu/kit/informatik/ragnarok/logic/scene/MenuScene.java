package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.Scenes;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuActionItem;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuSubMenu;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class MenuScene extends Scene {

	private MenuSubMenu menu;

	public MenuScene(GameModel model) {
		super(model);
	}

	@Override
	public void init() {
		super.init();

		this.menu = new MenuSubMenu(this, "Main Menu");
		this.menu.setPos(new Vec(GameConf.PIXEL_W / 2f, GameConf.PIXEL_H / 2f));

		MenuActionItem play = new MenuActionItem(this, "Infinite", () -> MenuScene.this.model.switchScene(Scenes.INFINIT));

		MenuActionItem lod = new MenuActionItem(this, "Level of the Day", () -> MenuScene.this.model.switchScene(Scenes.LOD));

		MenuSubMenu settings = new MenuSubMenu(this, "Settings");

		MenuSubMenu about = new MenuSubMenu(this, "About");

		this.menu.addItem(play);
		this.menu.addItem(lod);
		this.menu.addItem(settings);
		this.menu.addItem(about);

		this.addGuiElement(this.menu);
		this.menu.select();
	}

	public MenuItem getMenu() {
		return this.menu;
	}

}
