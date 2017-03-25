package rekit.logic.scene;

import java.awt.Desktop;

import rekit.config.GameConf;
import rekit.logic.GameModel;
import rekit.logic.gui.menu.BoolSetting;
import rekit.logic.gui.menu.MenuActionItem;
import rekit.logic.gui.menu.MenuGrid;
import rekit.logic.gui.menu.MenuItem;
import rekit.logic.gui.menu.MenuList;
import rekit.logic.gui.menu.SubMenu;
import rekit.logic.gui.menu.TextMenu;
import rekit.persistence.DirFileDefinitions;
import rekit.persistence.level.LevelManager;
import rekit.primitives.geometry.Vec;
import rekit.util.LambdaTools;

/**
 *
 * This class realizes the static part of the main menu of the game.
 *
 */
final class MainMenuScene extends Scene {
	/**
	 * The menu.
	 */
	private SubMenu menu;

	/**
	 * Create the main menu.
	 *
	 * @param model
	 *            the model
	 */
	private MainMenuScene(GameModel model) {
		super(model);
	}

	/**
	 * Create method of the scene.
	 *
	 * @param model
	 *            the model
	 * @param options
	 *            the options
	 * @return a new arcade scene.
	 */
	public static Scene create(GameModel model, String[] options) {
		return new MainMenuScene(model);
	}

	@Override
	public void init() {
		super.init();

		this.menu = new MenuList(this, "Main Menu");
		this.menu.setPos(new Vec(GameConf.PIXEL_W / 2f, GameConf.PIXEL_H / 2f));

		MenuList play = new MenuList(this, "Play");

		MenuActionItem inf = new MenuActionItem(this, "Infinite Fun", () -> this.getModel().switchScene(Scenes.INFINITE));
		MenuActionItem lod = new MenuActionItem(this, "Level of the Day", () -> this.getModel().switchScene(Scenes.LOD));
		MenuActionItem bossRush = new MenuActionItem(this, "Boss Rush", () -> this.getModel().switchScene(Scenes.BOSS_RUSH));

		MenuList top3 = new MenuList(this, "TOP 3 Levels");
		top3.addItem(inf, lod, bossRush);

		MenuGrid arcade = new MenuGrid(this, "Arcade Mode", 6, 100, 100);
		int i = 1;
		for (String idx : LevelManager.getArcadeLevelIDs()) {
			final String id = "" + idx;
			MenuActionItem button = new MenuActionItem(this, new Vec(80, 80), String.valueOf(i++), () -> this.getModel().switchScene(Scenes.ARCADE, id));
			arcade.addItem(button);
		}

		play.addItem(top3, arcade);

		MenuList settings = new MenuList(this, "Settings");
		settings.addItem( //
				new BoolSetting(this, "Debug Mode", "DEBUG"), //
				new MenuActionItem(this, "Open Config", LambdaTools.tryCatch(() -> Desktop.getDesktop().open(DirFileDefinitions.SYS_CONF))) //
		);

		MenuList about = new MenuList(this, "About");
		about.addItem(new TextMenu(this, GameConf.ABOUT));

		MenuActionItem exit = new MenuActionItem(this, "Exit", () -> System.exit(0));

		this.menu.addItem(play, settings, about, exit);

		this.addGuiElement(this.menu);
		this.menu.select();
	}

	@Override
	public MenuItem getMenu() {
		return this.menu;
	}
}