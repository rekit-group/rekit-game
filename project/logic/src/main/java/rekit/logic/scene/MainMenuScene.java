package rekit.logic.scene;

import java.awt.Desktop;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import org.fuchss.tools.lambda.LambdaConvert;

import rekit.config.GameConf;
import rekit.core.CameraTarget;
import rekit.logic.GameModel;
import rekit.logic.ILevelScene;
import rekit.logic.gui.BackgroundElement;
import rekit.logic.gui.menu.ArcadeLevelItem;
import rekit.logic.gui.menu.BoolSetting;
import rekit.logic.gui.menu.MainMenuGrid;
import rekit.logic.gui.menu.MenuActionItem;
import rekit.logic.gui.menu.MenuGrid;
import rekit.logic.gui.menu.MenuItem;
import rekit.logic.gui.menu.MenuList;
import rekit.logic.gui.menu.SubMenu;
import rekit.logic.gui.menu.TextItem;
import rekit.persistence.DirFileDefinitions;
import rekit.persistence.level.LevelManager;
import rekit.primitives.geometry.Vec;

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

	private String selectOption;

	/**
	 * Create the main menu.
	 *
	 * @param model
	 *            the model
	 */
	private MainMenuScene(GameModel model, String selectedMenu) {
		super(model);
		this.selectOption = selectedMenu;
	}

	/**
	 * Create method of the scene.
	 *
	 * @param model
	 *            the model
	 * @param options
	 *            the options first option: menuSelection the indices which
	 *            should be selected separadet by a dot ('.'). default is 0 for
	 *            the main menu.
	 * @return a new arcade scene.
	 */
	public static Scene create(GameModel model, String... options) {
		return new MainMenuScene(model, ((options == null || options.length < 1) ? "0" : options[0]));
	}

	@Override
	public void init() {
		super.init();

		this.menu = new MainMenuGrid(this, "Main Menu", 2);
		this.menu.setPos(new Vec(GameConf.PIXEL_W / 2f, 0.855f * GameConf.PIXEL_H));

		// Create play menu
		MenuList play = new MenuList(this, "Play");
		MenuActionItem inf = new MenuActionItem(this, "Infinite Fun", () -> this.getModel().switchScene(Scenes.INFINITE_FUN));
		MenuActionItem lod = new MenuActionItem(this, "Level of the Day", () -> this.getModel().switchScene(Scenes.LOD));
		MenuActionItem bossRush = new MenuActionItem(this, "Boss Rush", () -> this.getModel().switchScene(Scenes.BOSS_RUSH));
		MenuList arcade = new MenuList(this, "Arcade");
		for (Entry<String, List<String>> group : LevelManager.getArcadeLevelGroups().entrySet()) {
			this.addGroup(arcade, group);
		}

		Function<GameModel, ILevelScene> constructor = this.getModel().getTestSceneConstructor();
		if (GameConf.DEBUG && constructor != null) {
			MenuActionItem debugScene = new MenuActionItem(this, constructor.apply(null).getLevel().getName(), () -> this.getModel().switchScene(constructor));
			play.addItem(debugScene);
		}

		if (arcade.hasChildren()) {
			play.addItem(arcade);
		}
		play.addItem(inf, lod, bossRush);

		// Create settings menu
		MenuList settings = new MenuList(this, "Settings");
		settings.addItem(//
				new BoolSetting(this, "Debug Mode", "DEBUG"), //
				new MenuActionItem(this, "Open Config", LambdaConvert.wrap(() -> Desktop.getDesktop().open(DirFileDefinitions.BASE), e -> GameConf.GAME_LOGGER.fatal(e.getMessage()))) //
		);

		// Create about text
		MenuList about = new MenuList(this, "About");
		about.addItem(new TextItem(this, GameConf.ABOUT));

		// Create exit button
		MenuActionItem exit = new MenuActionItem(this, "Exit", () -> System.exit(0));

		// Add all elements to menu and focus
		this.menu.addItem(play, settings, about, exit);

		this.addGuiElement(new BackgroundElement(this, "mainmenu.png"));
		this.addGuiElement(this.menu);

		// select the right menu
		String[] optionLevels = this.selectOption.split("\\.");
		for (String str : optionLevels) {
			int index = Integer.parseInt(str);
			this.menu.setIndex(index);
			this.menu.select();
		}
	}

	private void addGroup(MenuList arcade, Entry<String, List<String>> group) {
		MenuGrid groupGrid = new MenuGrid(this, group.getKey(), 6);
		int i = 1;
		for (String idx : group.getValue()) {
			MenuActionItem button = new ArcadeLevelItem(this, new Vec(80, 80), String.valueOf(i++), group.getValue(), idx, this.getModel());
			groupGrid.addItem(button);
		}
		arcade.addItem(groupGrid);
	}

	@Override
	public MenuItem getMenu() {
		return this.menu;
	}

	@Override
	public void setCameraTarget(CameraTarget cameraTarget) {
		return;
	}

	@Override
	public float getCameraOffset() {
		return 0;
	}

}
