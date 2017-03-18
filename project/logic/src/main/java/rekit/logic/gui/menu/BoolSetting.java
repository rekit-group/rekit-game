package rekit.logic.gui.menu;

import java.lang.reflect.Field;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.IScene;

/**
 *
 * This class defines a menu to modify setting (boolean) of {@link GameConf}.
 *
 */
public final class BoolSetting extends MenuItem {
	/**
	 * The {@link Field} which shall be modified.
	 */
	private Field setting;
	/**
	 * The current value.
	 */
	private boolean curVal;

	/**
	 * Create entry.
	 *
	 * @param scene
	 *            the scene (menu)
	 * @param text
	 *            the text
	 * @param gameConfVal
	 *            the name of the value in {@link GameConf}
	 */
	public BoolSetting(IScene scene, String text, String gameConfVal) {
		super(scene, text);
		try {
			this.setting = GameConf.class.getDeclaredField(gameConfVal);
			this.curVal = this.setting.getBoolean(null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("no such setting in GameConf: " + gameConfVal);
		}
	}

	@Override
	public void right() {
		this.toggleSetting();
	}

	@Override
	public void left() {
		this.toggleSetting();
	}

	/**
	 * Toggle the setting.
	 */
	private void toggleSetting() {
		try {
			this.setting.setBoolean(null, !this.curVal);
			this.curVal = !this.curVal;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getText() {
		return super.getText() + " : " + (this.curVal ? "on" : "off");
	}

	@Override
	protected void renderItem(GameGrid f) {
		if (!this.selected) {
			super.renderItem(f);
		} else {
			f.drawRectangle(this.getPos(), this.getSize(), GameConf.MENU_BOX_OPTION_COLOR, false);
			f.drawText(this.getPos(), this.getText(), GameConf.MENU_TEXT, false);
		}
	}

}