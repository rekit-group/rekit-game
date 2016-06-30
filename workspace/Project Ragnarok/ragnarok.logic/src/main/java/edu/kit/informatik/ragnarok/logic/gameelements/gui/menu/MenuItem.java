package edu.kit.informatik.ragnarok.logic.gameelements.gui.menu;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.GuiElement;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class MenuItem extends GuiElement {

	protected boolean selected = false;

	protected String text;

	private MenuItem parent;

	private boolean hover;

	public MenuItem(Scene scene, String text) {
		super(scene);
		this.text = text;
		this.setSize(new Vec(400, 80));
	}

	public void setHover(boolean value) {
		this.hover = value;
	}

	public void select() {
		this.selected = true;
	}

	public void unselect() {
		this.selected = false;
	}

	public void up() {

	}

	public void down() {

	}

	public void back() {
		if (this.parent != null) {
			this.unselect();
			this.parent.select();
		}
	}

	public void setParent(MenuItem i) {
		this.parent = i;
	}

	@Override
	protected void internalRender(Field f) {
		f.drawRectangle(this.getPos(), this.getSize(), this.hover ? GameConf.MENU_BOX_SELECT_COLOR : GameConf.MENU_BOX_COLOR, false);
		f.drawText(this.getPos(), this.text, GameConf.MENU_TEXT, false);
	}

}
