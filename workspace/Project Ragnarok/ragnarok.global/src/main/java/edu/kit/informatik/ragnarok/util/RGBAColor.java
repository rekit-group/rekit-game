package edu.kit.informatik.ragnarok.util;

public class RGBAColor {
	public final int red, green, blue, alpha;

	public RGBAColor(int r, int g, int b, int a) {
		this.red = r;
		this.green = g;
		this.blue = b;
		this.alpha = a;
	}

	public RGBAColor darken(float p) {
		return new RGBAColor((int) (this.red * p), (int) (this.green * p), (int) (this.blue * p), (int) (this.alpha * p));
	}

}
