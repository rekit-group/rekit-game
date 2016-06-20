package edu.kit.informatik.ragnarok.logic.gameelements.gui;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GuiElement;
import edu.kit.informatik.ragnarok.util.TextOptions;

public class Text extends GuiElement {
	
	private String text;
	private TextOptions options;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public TextOptions getOptions() {
		return options;
	}

	public void setOptions(TextOptions options) {
		this.options = options;
	}

	public Text(GameModel model, TextOptions options) {
		super(model);
		this.text = "";
		this.options = options;
	}

	@Override
	public void render(Field f) {
		f.drawText(this.getPos(), this.text, this.getOptions());
	}
}
