package rekit.core;

import java.util.List;

import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.TextOptions;

/**
 * This class defines all necessary methods for drawing GameElements etc. on a
 * game grid
 *
 * @author Dominik Fuchss
 * @author Angelo Aracri
 *
 */
public abstract class GameGrid {
	/**
	 * Set the current camera offset (e.g. for Bosses)
	 *
	 * @param cameraOffsetUnits
	 *            the camera offset in units
	 */
	public abstract void setCurrentOffset(float cameraOffsetUnits);

	/////////////////////////////////////////////////////////
	// Draw Rectangle
	////////////////////////////////////////////////////////
	public final void drawRectangle(Vec pos, Vec size, RGBAColor rgbaColor) {
		this.drawRectangle(pos, size, rgbaColor, true, true);
	}

	public abstract void drawRectangle(Vec pos, Vec size, RGBAColor rgbaColor, boolean inGame, boolean usefilter);

	/////////////////////////////////////////////////////////
	// Draw Circle
	////////////////////////////////////////////////////////
	public final void drawCircle(Vec pos, Vec size, RGBAColor color) {
		this.drawCircle(pos, size, color, true, true);
	}

	public final void drawCircle(Vec pos, Vec size, RGBAColor color, boolean inGame) {
		this.drawCircle(pos, size, color, inGame, true);
	}

	public abstract void drawCircle(Vec pos, Vec size, RGBAColor color, boolean inGame, boolean usefilter);

	/////////////////////////////////////////////////////////
	// Draw Image
	////////////////////////////////////////////////////////
	public final void drawImage(Vec pos, Vec size, String imagePath) {
		this.drawImage(pos, size, imagePath, true, true);
	}

	public abstract void drawImage(Vec pos, Vec size, String imagePath, boolean inGame, boolean usefilter);

	/////////////////////////////////////////////////////////
	// Draw Polygon
	////////////////////////////////////////////////////////
	public final void drawPolygon(Polygon polygon, RGBAColor color, boolean fill) {
		this.drawPolygon(polygon, color, fill, true, true);
	}

	public final void drawPolygon(Polygon polygon, RGBAColor color, boolean fill, boolean inGame) {
		this.drawPolygon(polygon, color, fill, inGame, true);
	}

	public abstract void drawPolygon(Polygon polygon, RGBAColor color, boolean fill, boolean inGame, boolean usefilter);

	/////////////////////////////////////////////////////////
	// Draw Text
	////////////////////////////////////////////////////////
	public abstract void drawText(Vec pos, String text, TextOptions options, boolean inGame);

	/////////////////////////////////////////////////////////
	// Draw Round Rectangle
	////////////////////////////////////////////////////////

	public final void drawRoundRectangle(Vec pos, Vec size, RGBAColor rgbaColor, float arcWidth, float arcHeight) {
		this.drawRoundRectangle(pos, size, rgbaColor, arcWidth, arcHeight, true, true);
	}

	public abstract void drawRoundRectangle(Vec pos, Vec size, RGBAColor rgbaColor, float arcWidth, float arcHeight, boolean inGame, boolean usefilter);

	/////////////////////////////////////////////////////////
	// Draw Path
	////////////////////////////////////////////////////////
	public final void drawPath(Vec startPos, List<Vec> pts, RGBAColor in, boolean usefilter) {
		this.drawPath(startPos, pts, in, 1, usefilter);
	}

	public abstract void drawPath(Vec startPos, List<Vec> pts, RGBAColor in, int lineWidth, boolean usefilter);

}
