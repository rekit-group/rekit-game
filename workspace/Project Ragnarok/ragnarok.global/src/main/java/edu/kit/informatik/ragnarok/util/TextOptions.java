package edu.kit.informatik.ragnarok.util;

public class TextOptions {
	
	//TODO: align more than left vs right
	private boolean alignmentLeft;
	
	private int height;
	
	private RGBColor color;
	
	private String font;
	
	private int fontOptions;

	public boolean isAlignmentLeft() {
		return alignmentLeft;
	}

	public void setAlignmentLeft(boolean alignmentLeft) {
		this.alignmentLeft = alignmentLeft;
	}

	public int getHeight() {
		return height;
	}

	public RGBColor getColor() {
		return color;
	}

	public void setColor(RGBColor color) {
		this.color = color;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public int getFontOptions() {
		return fontOptions;
	}

	public void setFontOptions(int fontOptions) {
		this.fontOptions = fontOptions;
	}

	public TextOptions(boolean alignmentLeft, int height, RGBColor color, String font, int fontOptions) {
		this.alignmentLeft = alignmentLeft;
		this.height = height;
		this.color = color;
		this.font = font;
		this.fontOptions = fontOptions;
	}
}
