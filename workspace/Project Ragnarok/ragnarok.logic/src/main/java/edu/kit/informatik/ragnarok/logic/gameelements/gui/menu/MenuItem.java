package edu.kit.informatik.ragnarok.logic.gameelements.gui.menu;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.GuiElement;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class MenuItem extends GuiElement {

	protected boolean selected = false;

	protected String text;

	protected MenuItem parent;

	private boolean hover;

	public MenuItem(Scene scene, String text) {
		super(scene);
		this.text = text;
		this.setSize(new Vec(400, 80));
	}

	public MenuItem(Scene scene, String text, Vec size) {
		this(scene, text);
		this.setSize(size);
	}

	public void setHover(boolean value) {
		this.hover = value;
	}

	public void select() {
		this.selected = true;
	}

	public void unselect() {
		this.selected = false;
		if (this.parent != null) {
			this.parent.focus();
		}
	}

	public void up() {

	}

	public void down() {

	}

	protected void focus() {

	}

	public void setParent(MenuItem i) {
		this.parent = i;
	}

	@Override
	protected void internalRender(Field f) {
		this.renderItem(f);
	}

	protected void renderItem(Field f) {
		f.drawRectangle(this.getPos(), this.getSize(), this.hover ? GameConf.MENU_BOX_SELECT_COLOR : GameConf.MENU_BOX_COLOR, false);
		f.drawText(this.getPos(), this.text, GameConf.MENU_TEXT, false);
	}

	public void left() {

	}

	public void right() {

	}

	@Override
	public String toString() {
		return this.text;
	}

}
