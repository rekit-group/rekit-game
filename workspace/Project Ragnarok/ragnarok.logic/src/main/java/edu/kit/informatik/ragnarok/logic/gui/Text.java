package edu.kit.informatik.ragnarok.logic.gui;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GuiElement;
import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.util.TextOptions;

/**
 *
 * This class realizes a simple {@link GuiElement}:<br>
 * A Text which can be visualized on the view
 *
 */
public final class Text extends GuiElement {
	/**
	 * The text
	 */
	private String text;
	/**
	 * The text options
	 */
	private TextOptions options;

	public Text(IScene scene, TextOptions options) {
		super(scene);
		this.text = "";
		this.options = options;
	}

	/**
	 * Get the text
	 *
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Set the text
	 *
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Get the text options
	 *
	 * @return the options
	 */
	public TextOptions getOptions() {
		return this.options;
	}

	/**
	 * Set the text options
	 *
	 * @param options
	 *            the new options
	 */
	public void setOptions(TextOptions options) {
		this.options = options;
	}

	@Override
	public void internalRender(Field f) {
		f.drawText(this.getPos(), this.text, this.getOptions(), false);
	}
}
