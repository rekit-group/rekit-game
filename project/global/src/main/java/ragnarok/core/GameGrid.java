package ragnarok.core;

import java.util.List;

import ragnarok.primitives.geometry.Polygon;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;
import ragnarok.primitives.image.RGBColor;
import ragnarok.util.TextOptions;

/**
 * This class defines all necessary methods for drawing {@link GameElement
 * GameElements} etc. on a game grid
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

	/**
	 * Draw a rectangle.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	public final void drawRectangle(Vec pos, Vec size, RGBColor color) {
		this.drawRectangle(pos, size, color, true);
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
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 *
	 */
	public final void drawRectangle(Vec pos, Vec size, RGBColor color, boolean inGame) {
		this.drawRectangle(pos, size, color.toRGBA(), inGame, true);
	}

	/**
	 * Draw a rectangle.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param rgbaColor
	 *            the color
	 */
	public final void drawRectangle(Vec pos, Vec size, RGBAColor rgbaColor) {
		this.drawRectangle(pos, size, rgbaColor, true, true);
	}

	/**
	 * Draw a rectangle.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param rgbaColor
	 *            the color
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 * @param usefilter
	 *            indicates whether a filter shall be used if set
	 *
	 */
	public abstract void drawRectangle(Vec pos, Vec size, RGBAColor rgbaColor, boolean inGame, boolean usefilter);

	/**
	 * Draw a circle.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	public final void drawCircle(Vec pos, Vec size, RGBAColor color) {
		this.drawCircle(pos, size, color, true, true);
	}

	/**
	 * Draw a circle.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 */
	public final void drawCircle(Vec pos, Vec size, RGBColor color, boolean inGame) {
		this.drawCircle(pos, size, color.toRGBA(), inGame, true);
	}

	/**
	 * Draw a circle.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	public final void drawCircle(Vec pos, Vec size, RGBColor color) {
		this.drawCircle(pos, size, color, true);
	}

	/**
	 * Draw a circle.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 */
	public final void drawCircle(Vec pos, Vec size, RGBAColor color, boolean inGame) {
		this.drawCircle(pos, size, color, inGame, true);
	}

	/**
	 * Draw a circle.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 * @param usefilter
	 *            indicates whether a filter shall be used if set
	 *
	 */
	public abstract void drawCircle(Vec pos, Vec size, RGBAColor color, boolean inGame, boolean usefilter);

	/**
	 * Draw an image.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param imagePath
	 *            the path to the image
	 */
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
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 * @param usefilter
	 *            indicates whether a filter shall be used if set
	 *
	 */

	public abstract void drawImage(Vec pos, Vec size, String imagePath, boolean inGame, boolean usefilter);

	/**
	 * Draw a polygon.
	 *
	 * @param polygon
	 *            the polygon
	 * @param color
	 *            the color
	 * @param fill
	 *            indicates whether the polygon shall be filled or not
	 */
	public final void drawPolygon(Polygon polygon, RGBColor color, boolean fill) {
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
	 *            indicates whether the polygon shall be filled or not
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 */
	public final void drawPolygon(Polygon polygon, RGBColor color, boolean fill, boolean inGame) {
		this.drawPolygon(polygon, color.toRGBA(), fill, inGame, true);
	}

	/**
	 * Draw a polygon.
	 *
	 * @param polygon
	 *            the polygon
	 * @param color
	 *            the color
	 * @param fill
	 *            indicates whether the polygon shall be filled or not
	 */
	public final void drawPolygon(Polygon polygon, RGBAColor color, boolean fill) {
		this.drawPolygon(polygon, color, fill, true, true);
	}

	/**
	 * Draw a polygon.
	 *
	 * @param polygon
	 *            the polygon
	 * @param color
	 *            the color
	 * @param fill
	 *            indicates whether the polygon shall be filled or not
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 */
	public final void drawPolygon(Polygon polygon, RGBAColor color, boolean fill, boolean inGame) {
		this.drawPolygon(polygon, color, fill, inGame, true);
	}

	/**
	 * Draw a polygon.
	 *
	 * @param polygon
	 *            the polygon
	 * @param color
	 *            the color
	 * @param fill
	 *            indicates whether the polygon shall be filled or not
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 * @param usefilter
	 *            indicates whether a filter shall be used if set
	 *
	 */
	public abstract void drawPolygon(Polygon polygon, RGBAColor color, boolean fill, boolean inGame, boolean usefilter);

	/**
	 * Draw a text.
	 *
	 * @param pos
	 *            the position
	 * @param text
	 *            the text
	 * @param options
	 *            the text's options
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 *
	 */
	public abstract void drawText(Vec pos, String text, TextOptions options, boolean inGame);

	/**
	 * Draw a rectangle with round edges.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param rgbColor
	 *            the color
	 * @param arcWidth
	 *            the width of the arc
	 * @param arcHeight
	 *            the height of the arc
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 */
	public final void drawRoundRectangle(Vec pos, Vec size, RGBColor rgbColor, float arcWidth, float arcHeight, boolean inGame) {
		this.drawRoundRectangle(pos, size, rgbColor.toRGBA(), arcWidth, arcHeight);
	}

	/**
	 * Draw a rectangle with round edges.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param rgbColor
	 *            the color
	 * @param arcWidth
	 *            the width of the arc
	 * @param arcHeight
	 *            the height of the arc
	 */
	public final void drawRoundRectangle(Vec pos, Vec size, RGBColor rgbColor, float arcWidth, float arcHeight) {
		this.drawRoundRectangle(pos, size, rgbColor, arcWidth, arcHeight, true);
	}

	/**
	 * Draw a rectangle with round edges.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param rgbaColor
	 *            the color
	 * @param arcWidth
	 *            the width of the arc
	 * @param arcHeight
	 *            the height of the arc
	 */
	public final void drawRoundRectangle(Vec pos, Vec size, RGBAColor rgbaColor, float arcWidth, float arcHeight) {
		this.drawRoundRectangle(pos, size, rgbaColor, arcWidth, arcHeight, true, true);
	}

	/**
	 * Draw a rectangle with round edges.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param rgbaColor
	 *            the color
	 * @param arcWidth
	 *            the width of the arc
	 * @param arcHeight
	 *            the height of the arc
	 *
	 * @param inGame
	 *            this indicates whether the drawing belongs to a
	 *            {@link GameElement} (ingame -> CameraDependend) or a
	 *            {@link GuiElement} (not ingame -> FrameDepenedend)
	 * @param usefilter
	 *            indicates whether a filter shall be used if set
	 */
	public abstract void drawRoundRectangle(Vec pos, Vec size, RGBAColor rgbaColor, float arcWidth, float arcHeight, boolean inGame,
			boolean usefilter);

	/**
	 * Draw a path.
	 *
	 * @param startPos
	 *            the start point
	 * @param pts
	 *            the next points (relative to startPos ?!)
	 * @param in
	 *            the color
	 */
	// TODO Check JDoc
	public abstract void drawPath(Vec startPos, List<Vec> pts, RGBColor in);
}
