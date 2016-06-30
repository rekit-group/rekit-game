package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;
import edu.kit.informatik.ragnarok.util.TextOptions;

public abstract class Field {

	public abstract void setCurrentOffset(float cameraOffset);

	public void drawRectangle(Vec pos, Vec size, RGBColor color) {
		this.drawRectangle(pos, size, color, true);
	}

	public abstract void drawRectangle(Vec pos, Vec size, RGBColor color, boolean inGame);

	public void drawRectangle(Vec pos, Vec size, RGBAColor rgbaColor) {
		this.drawRectangle(pos, size, rgbaColor, true);
	}

	public abstract void drawRectangle(Vec pos, Vec size, RGBAColor rgbaColor, boolean inGame);

	public void drawCircle(Vec pos, Vec size, RGBAColor color) {
		this.drawCircle(pos, size, color, true);
	}

	public abstract void drawCircle(Vec pos, Vec size, RGBColor color, boolean inGame);

	public void drawCircle(Vec pos, Vec size, RGBColor color) {
		this.drawCircle(pos, size, color, true);
	}

	public abstract void drawCircle(Vec pos, Vec size, RGBAColor color, boolean inGame);

	public void drawImage(Vec pos, Vec size, String imagePath) {
		this.drawImage(pos, size, imagePath, true);
	}

	public abstract void drawImage(Vec pos, Vec size, String imagePath, boolean inGame);

	public void drawPolygon(Polygon polygon, RGBColor color, boolean fill) {
		this.drawPolygon(polygon, color, fill, true);
	}

	public abstract void drawPolygon(Polygon polygon, RGBColor color, boolean fill, boolean inGame);

	public void drawPolygon(Polygon polygon, RGBAColor color, boolean fill) {
		this.drawPolygon(polygon, color, fill, true);
	}

	public abstract void drawPolygon(Polygon polygon, RGBAColor color, boolean fill, boolean inGame);

	public abstract void drawText(Vec pos, String text, TextOptions options, boolean inGame);
}
