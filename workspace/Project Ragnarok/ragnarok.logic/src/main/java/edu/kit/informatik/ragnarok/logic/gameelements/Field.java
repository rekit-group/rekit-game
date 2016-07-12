package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;
import edu.kit.informatik.ragnarok.util.TextOptions;

/**
 * This class defines all necessary methods for drawing {@link GameElement
 * GameElements} etc. on a game grid
 *
 *
 */
// TODO What means ingame??
public abstract class Field {
	/**
	 * Set the current camera offset (e.g. for Bosses)
	 *
	 * @param cameraOffset
	 *            the camera offset
	 */
	public abstract void setCurrentOffset(float cameraOffset);

	/**
	 * Draw a rectangle
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	public void drawRectangle(Vec pos, Vec size, RGBColor color) {
		this.drawRectangle(pos, size, color, true);
	}

	/**
	 * Draw a rectangle
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param inGame
	 *
	 */
	public abstract void drawRectangle(Vec pos, Vec size, RGBColor color, boolean inGame);

	/**
	 * Draw a rectangle
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param rgbaColor
	 *            the color
	 */
	public void drawRectangle(Vec pos, Vec size, RGBAColor rgbaColor) {
		this.drawRectangle(pos, size, rgbaColor, true);
	}

	/**
	 * Draw a rectangle
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param rgbaColor
	 *            the color
	 * @param inGame
	 */
	public abstract void drawRectangle(Vec pos, Vec size, RGBAColor rgbaColor, boolean inGame);

	/**
	 * Draw a circle
	 * 
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	public void drawCircle(Vec pos, Vec size, RGBAColor color) {
		this.drawCircle(pos, size, color, true);
	}

	/**
	 * Draw a circle
	 * 
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param inGame
	 */
	public abstract void drawCircle(Vec pos, Vec size, RGBColor color, boolean inGame);

	/**
	 * Draw a circle
	 * 
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 */
	public void drawCircle(Vec pos, Vec size, RGBColor color) {
		this.drawCircle(pos, size, color, true);
	}

	/**
	 * Draw a circle
	 * 
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param color
	 *            the color
	 * @param inGame
	 */
	public abstract void drawCircle(Vec pos, Vec size, RGBAColor color, boolean inGame);

	/**
	 * Draw an image
	 * 
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param imagePath
	 *            the path to the image
	 */
	public void drawImage(Vec pos, Vec size, String imagePath) {
		this.drawImage(pos, size, imagePath, true);
	}

	/**
	 * Draw an image
	 * 
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param imagePath
	 *            the path to the image
	 * @param inGame
	 */
	public abstract void drawImage(Vec pos, Vec size, String imagePath, boolean inGame);

	/**
	 * Draw a polygon
	 * 
	 * @param polygon
	 *            the polygon
	 * @param color
	 *            the color
	 * @param fill
	 *            indicates whether the polygon shall be filled or not
	 */
	public void drawPolygon(Polygon polygon, RGBColor color, boolean fill) {
		this.drawPolygon(polygon, color, fill, true);
	}

	/**
	 * Draw a polygon
	 * 
	 * @param polygon
	 *            the polygon
	 * @param color
	 *            the color
	 * @param fill
	 *            indicates whether the polygon shall be filled or not
	 * @param inGame
	 */
	public abstract void drawPolygon(Polygon polygon, RGBColor color, boolean fill, boolean inGame);

	/**
	 * Draw a polygon
	 * 
	 * @param polygon
	 *            the polygon
	 * @param color
	 *            the color
	 * @param fill
	 *            indicates whether the polygon shall be filled or not
	 */
	public void drawPolygon(Polygon polygon, RGBAColor color, boolean fill) {
		this.drawPolygon(polygon, color, fill, true);
	}

	/**
	 * Draw a polygon
	 * 
	 * @param polygon
	 *            the polygon
	 * @param color
	 *            the color
	 * @param fill
	 *            indicates whether the polygon shall be filled or not
	 * @param inGame
	 */
	public abstract void drawPolygon(Polygon polygon, RGBAColor color, boolean fill, boolean inGame);

	/**
	 * Draw a text
	 * 
	 * @param pos
	 *            the position
	 * @param text
	 *            the text
	 * @param options
	 *            the text's options
	 * @param inGame
	 */
	public abstract void drawText(Vec pos, String text, TextOptions options, boolean inGame);
}
