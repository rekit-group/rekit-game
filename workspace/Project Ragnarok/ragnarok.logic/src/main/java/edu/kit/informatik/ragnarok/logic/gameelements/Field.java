package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;

public interface Field {

	void drawRectangle(Vec2D pos, Vec2D size, RGBColor color);

	void drawCircle(Vec2D pos, Vec2D size, RGBColor color);

	void drawImage(Vec2D pos, Vec2D size, String string);

	void drawPolygon(Polygon polygon, RGBAColor color);

	void drawPolygon(Polygon rotatedSpikes, RGBColor color);
}
