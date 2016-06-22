package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;
import edu.kit.informatik.ragnarok.util.TextOptions;

public interface Field {
	
	void setCurrentOffset(float cameraOffset);
	
	void drawRectangle(Vec2D pos, Vec2D size, RGBColor color);
	void drawRectangle(Vec2D pos, Vec2D size, RGBAColor rgbaColor);
	
	void drawCircle(Vec2D pos, Vec2D size, RGBColor color);

	void drawImage(Vec2D pos, Vec2D size, String string);

	void drawPolygon(Polygon polygon, RGBAColor color);
	void drawPolygon(Polygon polygon, RGBColor color);
	
	//void drawGameText(Vec2D pos, String text);
	
	void drawGuiImage(Vec2D pos, Vec2D size, String string);
	
	void drawText(Vec2D pos, String text, TextOptions options);

	
	
}
