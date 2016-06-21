package edu.kit.informatik.ragnarok.util;

import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class TextOptions {

	private Vec2D alignment;

	private int height;

	private RGBColor color;

	private String font;

	private int fontOptions;

	public Vec2D getAlignment() {
		return this.alignment;
	}

	public void setAlignmentLeft(Vec2D alignment) {
		this.alignment = alignment;
	}

	public int getHeight() {
		return this.height;
	}

	public RGBColor getColor() {
		return this.color;
	}

	public void setColor(RGBColor color) {
		this.color = color;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getFont() {
		return this.font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public int getFontOptions() {
		return this.fontOptions;
	}

	public void setFontOptions(int fontOptions) {
		this.fontOptions = fontOptions;
	}

	public TextOptions(Vec2D alignment, int height, RGBColor color, String font, int fontOptions) {
		this.alignment = alignment;
		this.height = height;
		this.color = color;
		this.font = font;
		this.fontOptions = fontOptions;
	}
}
