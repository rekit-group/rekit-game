package edu.kit.informatik.ragnarok.logic.gui.menu;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.scene.Scene;

public class SettingToggle extends MenuItem {

	private java.lang.reflect.Field setting;
	private boolean curVal;

	public SettingToggle(Scene scene, String text, String gameConfVal) {
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
	protected void renderItem(Field f) {
		if (!this.selected) {
			super.renderItem(f);
		} else {
			f.drawRectangle(this.getPos(), this.getSize(), GameConf.MENU_BOX_OPTION_COLOR, false);
			f.drawText(this.getPos(), this.getText(), GameConf.MENU_TEXT, false);
		}
	}

}