package rekit.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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

	/**
	 * Draw a rectangle.<br>
	 * invokes {@link #drawRectangle(Vec, Vec, RGBAColor, boolean, boolean)} and
	 * set ingame and usefilter to {@code true}
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	@InGameMethod
	public final void drawRectangle(Vec pos, Vec size, RGBAColor color) {
		this.drawRectangle(pos, size, color, true, true);
	}

	/**
	 * Draw a rectangle.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param ingame
	 *            indicates whether it shall drawn as entity of the game
	 *            (relative to current game progress) or relative to the
	 *            surrounding frame
	 * @param usefilter
	 *            indicates whether a filter (if set) shall applied before
	 *            drawing
	 */
	@InGameMethod
	@NonInGameMethod
	public abstract void drawRectangle(Vec pos, Vec size, RGBAColor color, boolean ingame, boolean usefilter);

	/////////////////////////////////////////////////////////
	// Draw Circle
	////////////////////////////////////////////////////////
	/**
	 * Draw a circle.<br>
	 * invokes {@link #drawCircle(Vec, Vec, RGBAColor, boolean, boolean)} and
	 * set ingame and usefilter to {@code true}
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size (diameter)
	 * @param color
	 *            the color
	 */
	@InGameMethod
	public final void drawCircle(Vec pos, Vec size, RGBAColor color) {
		this.drawCircle(pos, size, color, true, true);
	}

	/**
	 * Draw a circle.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size (diameter)
	 * @param color
	 *            the color
	 * @param ingame
	 *            indicates whether it shall drawn as entity of the game
	 *            (relative to current game progress) or relative to the
	 *            surrounding frame
	 * @param usefilter
	 *            indicates whether a filter (if set) shall applied before
	 *            drawing
	 */
	@InGameMethod
	@NonInGameMethod
	public abstract void drawCircle(Vec pos, Vec size, RGBAColor color, boolean ingame, boolean usefilter);

	/////////////////////////////////////////////////////////
	// Draw Image
	////////////////////////////////////////////////////////
	/**
	 * Draw an image. <br>
	 * invokes {@link #drawImage(Vec, Vec, String, boolean, boolean)} and set
	 * ingame and usefilter to {@code true}
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param imagePath
	 *            the path to the image
	 */
	@InGameMethod
	public final void drawImage(Vec pos, Vec size, String imagePath) {
		this.drawImage(pos, size, imagePath, true, true);
	}

	/**
	 * Draw an image.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param imagePath
	 *            the path to the image
	 * @param ingame
	 *            indicates whether it shall drawn as entity of the game
	 *            (relative to current game progress) or relative to the
	 *            surrounding frame
	 * @param usefilter
	 *            indicates whether a filter (if set) shall applied before
	 *            drawing
	 */
	@InGameMethod
	@NonInGameMethod
	public abstract void drawImage(Vec pos, Vec size, String imagePath, boolean ingame, boolean usefilter);

	/////////////////////////////////////////////////////////
	// Draw Polygon
	////////////////////////////////////////////////////////
	/**
	 * Draw a polygon.<br>
	 * invokes {@link #drawPolygon(Polygon, RGBAColor, boolean, boolean)} and
	 * set usefilter to {@code true}
	 *
	 * @param polygon
	 *            the polygon
	 * @param color
	 *            the color
	 * @param fill
	 *            indicates whether the polygon shall be filled
	 */
	@InGameMethod
	public final void drawPolygon(Polygon polygon, RGBAColor color, boolean fill) {
		this.drawPolygon(polygon, color, fill, true);
	}

	/**
	 * Draw a polygon.
	 *
	 * @param polygon
	 *            the polygon
	 * @param color
	 *            the color
	 * @param fill
	 *            indicates whether the polygon shall be filled
	 * @param usefilter
	 *            indicates whether a filter (if set) shall applied before
	 *            drawing
	 */
	@InGameMethod
	public abstract void drawPolygon(Polygon polygon, RGBAColor color, boolean fill, boolean usefilter);

	/////////////////////////////////////////////////////////
	// Draw Text
	////////////////////////////////////////////////////////
	/**
	 * Draw a text element.
	 *
	 * @param pos
	 *            the position
	 * @param text
	 *            the text
	 * @param options
	 *            options to customize appereance
	 * @param ingame
	 *            indicates whether it shall drawn as entity of the game
	 *            (relative to current game progress) or relative to the
	 *            surrounding frame
	 */
	@InGameMethod
	@NonInGameMethod
	public abstract void drawText(Vec pos, String text, TextOptions options, boolean ingame);

	/////////////////////////////////////////////////////////
	// Draw Round Rectangle
	////////////////////////////////////////////////////////
	/**
	 * Draw a rectangle with round corners.<br>
	 * invokes
	 * {@link #drawRoundRectangle(Vec, Vec, RGBAColor, float, float, boolean, boolean)}
	 * and set ingame and usefilter to {@code true}
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param arcWidth
	 *            the horizontal diameter of the arc at the four corners
	 * @param arcHeight
	 *            the vertical diameter of the arc at the four corners
	 */
	@InGameMethod
	public final void drawRoundRectangle(Vec pos, Vec size, RGBAColor color, float arcWidth, float arcHeight) {
		this.drawRoundRectangle(pos, size, color, arcWidth, arcHeight, true, true);
	}

	/**
	 * Draw a rectangle with round corners.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param arcWidth
	 *            the horizontal diameter of the arc at the four corners
	 * @param arcHeight
	 *            the vertical diameter of the arc at the four corners
	 * @param ingame
	 *            indicates whether it shall drawn as entity of the game
	 *            (relative to current game progress) or relative to the
	 *            surrounding frame
	 * @param usefilter
	 *            indicates whether a filter (if set) shall applied before
	 *            drawing
	 */
	@InGameMethod
	@NonInGameMethod
	public abstract void drawRoundRectangle(Vec pos, Vec size, RGBAColor color, float arcWidth, float arcHeight, boolean ingame, boolean usefilter);

	/////////////////////////////////////////////////////////
	// Draw Path
	////////////////////////////////////////////////////////
	/**
	 * Draw a path.<br>
	 * invokes {@link #drawPath(Vec, List, RGBAColor, int, boolean)} and set
	 * lineWidth to {@code 1}
	 *
	 * @param startPos
	 *            the start position
	 * @param pts
	 *            the following points (relative to predecessor)
	 * @param color
	 *            the color
	 * @param usefilter
	 *            indicates whether a filter (if set) shall applied before
	 *            drawing
	 */
	@InGameMethod
	public final void drawPath(Vec startPos, List<Vec> pts, RGBAColor color, boolean usefilter) {
		this.drawPath(startPos, pts, color, 1, usefilter);
	}

	/**
	 * Draw a path.
	 *
	 * @param startPos
	 *            the start position
	 * @param pts
	 *            the following points (relative to predecessor)
	 * @param color
	 *            the color
	 * @param lineWidth
	 *            the line width
	 * @param usefilter
	 *            indicates whether a filter (if set) shall applied before
	 *            drawing
	 */
	@InGameMethod
	public abstract void drawPath(Vec startPos, List<Vec> pts, RGBAColor color, int lineWidth, boolean usefilter);

	/**
	 * This annotation indicates that this method can be used to draw InGame
	 * Elements.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	@Retention(value = RetentionPolicy.SOURCE)
	@Target(value = ElementType.METHOD)
	@Documented
	public @interface InGameMethod {
	}

	/**
	 * This annotation indicates that this method can be used to draw Non-InGame
	 * Elements.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	@Retention(value = RetentionPolicy.SOURCE)
	@Target(value = ElementType.METHOD)
	@Documented
	public @interface NonInGameMethod {
	}

}
