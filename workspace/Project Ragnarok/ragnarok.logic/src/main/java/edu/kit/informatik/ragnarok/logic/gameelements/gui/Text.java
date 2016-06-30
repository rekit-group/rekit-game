package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.util.TextOptions;

public class Text extends GuiElement {

	private String text;
	private TextOptions options;

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TextOptions getOptions() {
		return this.options;
	}

	public void setOptions(TextOptions options) {
		this.options = options;
	}

	public Text(Scene scene, TextOptions options) {
		super(scene);
		this.text = "";
		this.options = options;
	}

	@Override
	public void internalRender(Field f) {
		f.drawText(this.getPos(), this.text, this.getOptions(), false);
	}
}
