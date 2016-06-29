package edu.kit.informatik.ragnarok.util;

public class RGBColor implements Cloneable {
	public final int red, green, blue;

	public RGBColor(int r, int g, int b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	@Override
	public RGBColor clone() {
		return new RGBColor(this.red, this.green, this.blue);
	}

	public RGBColor darken(float p) {
		return new RGBColor((int) (this.red * p), (int) (this.green * p), (int) (this.blue * p));
	}

}
