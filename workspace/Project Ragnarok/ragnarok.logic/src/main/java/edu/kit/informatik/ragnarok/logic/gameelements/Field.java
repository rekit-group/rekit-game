package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;
import edu.kit.informatik.ragnarok.util.TextOptions;

public abstract class Field {

	public abstract void setCurrentOffset(float cameraOffset);

	public abstract void drawRectangle(Vec pos, Vec size, RGBColor color);

	public abstract void drawRectangle(Vec pos, Vec size, RGBAColor rgbaColor);

	public abstract void drawCircle(Vec pos, Vec size, RGBColor color);

	public abstract void drawCircle(Vec pos, Vec size, RGBAColor color);

	public final void drawImage(Vec pos, Vec size, String imagePath) {
		this.drawImage(pos, size, imagePath, true);
	}

	public abstract void drawImage(Vec pos, Vec size, String imagePath, boolean inGame);

	public abstract void drawPolygon(Polygon polygon, RGBAColor color, boolean fill);

	public abstract void drawPolygon(Polygon polygon, RGBColor color, boolean fill);

	public abstract void drawText(Vec pos, String text, TextOptions options, boolean inGame);

}
