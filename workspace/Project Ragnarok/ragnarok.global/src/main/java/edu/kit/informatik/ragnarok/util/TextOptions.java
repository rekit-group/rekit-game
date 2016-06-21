package edu.kit.informatik.ragnarok.util;

import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class TextOptions implements Cloneable {

	private Vec2D alignment;

	private int height;

	private RGBColor color;

	private String font;

	private int fontOptions;

	public Vec2D getAlignment() {
		return this.alignment;
	}

	public TextOptions setAlignmentLeft(Vec2D alignment) {
		this.alignment = alignment;
		return this;
	}

	public int getHeight() {
		return this.height;
	}

	public RGBColor getColor() {
		return this.color;
	}

	public TextOptions setColor(RGBColor color) {
		this.color = color;
		return this;
	}

	public TextOptions setHeight(int height) {
		this.height = height;
		return this;
	}

	public String getFont() {
		return this.font;
	}

	public TextOptions setFont(String font) {
		this.font = font;
		return this;
	}

	public int getFontOptions() {
		return this.fontOptions;
	}

	public TextOptions setFontOptions(int fontOptions) {
		this.fontOptions = fontOptions;
		return this;
	}

	public TextOptions(Vec2D alignment, int height, RGBColor color, String font, int fontOptions) {
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
