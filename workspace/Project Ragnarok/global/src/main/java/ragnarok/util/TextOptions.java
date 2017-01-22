package ragnarok.util;

import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBColor;

/**
 *
 * This class combines several text options for formatting texts.
 *
 */
public final class TextOptions implements Cloneable {
	/**
	 * The alignment.
	 */
	private Vec alignment;
	/**
	 * The height.
	 */
	private int height;
	/**
	 * The color.
	 */
	private RGBColor color;
	/**
	 * The font.
	 */
	private String font;
	/**
	 * The font-options.
	 */
	private int fontOptions;

	/**
	 * Get the alignment.
	 *
	 * @return the alignment
	 */
	public Vec getAlignment() {
		return this.alignment;
	}

	/**
	 * Set alignment (anchor left).
	 *
	 * @param alignment
	 *            the alignment
	 * @return the modified text-options (this)
	 */
	public TextOptions setAlignmentLeft(Vec alignment) {
		this.alignment = alignment;
		return this;
	}

	/**
	 * Get the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Get the color.
	 *
	 * @return the color
	 */
	public RGBColor getColor() {
		return this.color;
	}

	/**
	 * Set the color.
	 *
	 * @param color
	 *            the color
	 * @return the modified text-options (this)
	 */
	public TextOptions setColor(RGBColor color) {
		this.color = color;
		return this;
	}

	/**
	 * Set the height.
	 *
	 * @param height
	 *            the height
	 * @return the modified text-options (this)
	 */
	public TextOptions setHeight(int height) {
		this.height = height;
		return this;
	}

	/**
	 * Get the font.
	 *
	 * @return the font
	 */
	public String getFont() {
		return this.font;
	}

	/**
	 * Set the font.
	 *
	 * @param font
	 *            the font
	 * @return the modified text-options (this)
	 */
	public TextOptions setFont(String font) {
		this.font = font;
		return this;
	}

	/**
	 * Get the font-options.
	 *
	 * @return the font-options
	 */
	public int getFontOptions() {
		return this.fontOptions;
	}

	/**
	 * Set the font-options.
	 *
	 * @param fontOptions
	 *            the font-options
	 * @return the modified text-options (this)
	 */
	public TextOptions setFontOptions(int fontOptions) {
		this.fontOptions = fontOptions;
		return this;
	}

	/**
	 * Create a TextOption container.
	 *
	 * @param alignment
	 *            the alignment (left anchor)
	 * @param height
	 *            the height
	 * @param color
	 *            the color
	 * @param font
	 *            the font
	 * @param fontOptions
	 *            the font-options
	 */
	public TextOptions(Vec alignment, int height, RGBColor color, String font, int fontOptions) {
		this.alignment = alignment;
		this.height = height;
		this.color = color;
		this.font = font;
		this.fontOptions = fontOptions;
	}

	@Override
	public TextOptions clone() {
		return new TextOptions(this.alignment.clone(), this.height, this.color.clone(), "" + this.font, this.fontOptions);

	}
}
