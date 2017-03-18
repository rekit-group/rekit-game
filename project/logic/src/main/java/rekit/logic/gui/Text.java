package rekit.logic.gui;

import rekit.core.GameGrid;
import rekit.logic.IScene;
import rekit.util.TextOptions;

/**
 *
 * This class realizes a simple {@link GuiElement}:<br>
 * A Text which can be visualized on the view.
 *
 */
public final class Text extends GuiElement {
	/**
	 * The text.
	 */
	private String text;
	/**
	 * The text options.
	 */
	private TextOptions options;

	/**
	 * Create the text element.
	 *
	 * @param scene
	 *            the scene
	 * @param options
	 *            the textoptions
	 */
	public Text(IScene scene, TextOptions options) {
		super(scene);
		this.text = "";
		this.options = options;
	}

	/**
	 * Get the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Set the text.
	 *
	 * @param text
	 *            the new text
	 * @return {@code this}
	 */
	public Text setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * Get the text options.
	 *
	 * @return the options
	 */
	public TextOptions getOptions() {
		return this.options;
	}

	/**
	 * Set the text options.
	 *
	 * @param options
	 *            the new options
	 */
	public void setOptions(TextOptions options) {
		this.options = options;
	}

	@Override
	public final void internalRender(GameGrid f) {
		f.drawText(this.getPos(), this.text, this.getOptions(), false);
	}
}
