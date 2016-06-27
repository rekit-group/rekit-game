package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;
import edu.kit.informatik.ragnarok.util.TextOptions;

public interface Field {
	
	void setCurrentOffset(float cameraOffset);
	
	void drawRectangle(Vec pos, Vec size, RGBColor color);
	void drawRectangle(Vec pos, Vec size, RGBAColor rgbaColor);
	
	void drawCircle(Vec pos, Vec size, RGBColor color);
	void drawCircle(Vec pos, Vec size, RGBAColor color);
	
	void drawImage(Vec pos, Vec size, String string);

	void drawPolygon(Polygon polygon, RGBAColor color);
	void drawPolygon(Polygon polygon, RGBColor color);
	
	//void drawGameText(Vec2D pos, String text);
	
	void drawGuiImage(Vec pos, Vec size, String string);
	
	void drawText(Vec pos, String text, TextOptions options);

	
	
}
