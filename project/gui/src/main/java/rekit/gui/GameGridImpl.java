package rekit.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.Filter;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.image.RGBColor;
import rekit.util.CalcUtil;
import rekit.util.TextOptions;
import rekit.util.Tuple;
import rekit.util.Utils;

/**
 * This class represents a {@link GameGrid} of the {@link GameView}.
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 *
 */
class GameGridImpl extends GameGrid {

	/**
	 * The current camera offset.
	 */
	private int cameraOffset = 0;
	/**
	 * The current camera offset in units.
	 */
	private float cameraOffsetUnits = 0;
	/**
	 * The current filter.
	 */
	private Filter filter;
	/**
	 * The current graphics for drawing.
	 */
	private Graphics2D graphics;
	/**
	 * The image cache: (Path, Filter) -> Image.
	 */
	private final Map<Tuple<String, Filter>, Image> images = new HashMap<>();

	/**
	 * Set the current graphics.
	 *
	 * @param current
	 *            the new graphics
	 */
	void setGraphics(Graphics2D current) {
		this.graphics = current;
	}

	@Override
	public void setCurrentOffset(float cameraOffsetUnits) {
		this.cameraOffsetUnits = cameraOffsetUnits;
		this.cameraOffset = -CalcUtil.units2pixel(cameraOffsetUnits);
	}

	/**
	 * The implementation of {@link #drawCircle(Vec, Vec, RGBAColor)}.
	 *
	 * @param pos
	 *            same as in base method
	 * @param size
	 *            same as in base method
	 * @param col
	 *            same as in base method
	 */
	private void drawCircleImpl(Vec pos, Vec size, Color col) {
		this.graphics.setColor(col);
		Ellipse2D.Float circle = new Ellipse2D.Float( //
				(pos.getX() - size.getX() / 2f), //
				(pos.getY() - size.getY() / 2f), //
				size.getX(), size.getY());

		this.graphics.fill(circle);

	}

	/**
	 * The implementation of {@link #drawRectangle(Vec, Vec, RGBAColor)}.
	 *
	 * @param pos
	 *            same as in base method
	 * @param size
	 *            same as in base method
	 * @param col
	 *            same as in base method
	 */
	private void drawRectangleImpl(Vec pos, Vec size, Color col) {
		this.graphics.setColor(col);
		this.graphics.fillRect( //
				(int) (pos.getX() - size.getX() / 2f), //
				(int) (pos.getY() - size.getY() / 2f), //
				(int) size.getX(), (int) size.getY());

	}

	/**
	 * The implementation of
	 * {@link #drawRoundRectangle(Vec, Vec, RGBAColor, float, float)}.
	 *
	 * @param pos
	 *            same as in base method
	 * @param size
	 *            same as in base method
	 * @param col
	 *            same as in base method
	 * @param arcWidth
	 *            same as in base method
	 * @param arcHeight
	 *            same as in base method
	 */
	private void drawRoundRectangleImpl(Vec pos, Vec size, Color col, int arcWidth, int arcHeight) {
		this.graphics.setColor(col);
		this.graphics.fillRoundRect( //
				(int) (pos.getX() - size.getX() / 2f), // X
				(int) (pos.getY() - size.getY() / 2f), // Y
				(int) size.getX(), (int) size.getY(), // Size
				arcWidth, arcHeight); // arc

	}

	/**
	 * The implementation of {@link #drawPolygon(Polygon, RGBAColor, boolean)}.
	 *
	 * @param polygon
	 *            px-points (x,y),(x,y),...
	 * @param col
	 *            same as in base method
	 * @param fill
	 *            same as in base method
	 */
	private void drawPolygonImpl(int[] polygon, Color col, boolean fill) {
		this.graphics.setColor(col);
		// Split in x and y points.
		int[] xpoints = new int[polygon.length / 2];
		int[] ypoints = new int[polygon.length / 2];
		for (int i = 0; i < polygon.length; i += 2) {
			xpoints[i / 2] = polygon[i];
			ypoints[i / 2] = polygon[i + 1];
		}

		java.awt.Polygon toDraw = new java.awt.Polygon(xpoints, ypoints, polygon.length / 2);
		// draw actual polygon
		if (fill) {
			this.graphics.fillPolygon(toDraw);
		} else {
			this.graphics.drawPolygon(toDraw);
		}

	}

	/**
	 * The implementation of {@link #drawImage(Vec, Vec, String)}.
	 *
	 * @param pos
	 *            same as in base method
	 * @param size
	 *            same as in base method
	 * @param imagePath
	 *            same as in base method
	 * @param usefilter
	 *            indicates whether a filter shall used
	 */
	private void drawImageImpl(Vec pos, Vec size, String imagePath, boolean usefilter) {
		Image image = null;
		Tuple<String, Filter> key = Tuple.create(imagePath, usefilter ? this.filter : null);
		if (this.images.containsKey(key) && !(this.filter != null && this.filter.changed())) {
			image = this.images.get(key);
		} else {
			image = ImageManagement.get(imagePath);
			if (this.filter != null && this.filter.isApplyImage()) {
				image = ImageManagement.toImage(this.filter.apply(ImageManagement.getAsAbstractImage(imagePath)));
			}
			this.images.put(key, image);
			GameConf.GAME_LOGGER.debug("GameGrid: Image Cache Miss: " + key);
		}

		this.graphics.drawImage(image, // image
				(int) (pos.getX() - size.getX() / 2f), // dstX
				(int) (pos.getY() - size.getY() / 2f), // dstY
				null);

	}

	/**
	 * The implementation of
	 * {@link #drawText(Vec, String, TextOptions, boolean)}.
	 *
	 * @param pos
	 *            same as in base method
	 * @param text
	 *            same as in base method
	 * @param options
	 *            same as in base method
	 */
	private void drawTextImpl(Vec pos, String text, TextOptions options) {
		// Set color to red and set font
		RGBColor in = new RGBColor(options.getColor().red, options.getColor().green, options.getColor().blue);
		RGBColor col = (!options.getUseFilter() || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		this.graphics.setColor(Utils.calcRGB(col));

		Font font = new Font(options.getFont(), options.getFontOptions(), options.getHeight());
		this.graphics.setFont(font);
		FontMetrics metrics = this.graphics.getFontMetrics(font);

		float x = pos.getX(), y = pos.getY();
		float xAlign = options.getAlignment().getX(), yAlign = options.getAlignment().getY();
		for (String line : text.split("\n")) {
			Dimension offset = this.getOffset(line, metrics);
			this.graphics.drawString(line, //
					(x + xAlign * offset.width), //
					(y += metrics.getHeight()) + yAlign * offset.height);
		}

	}

	/**
	 * Get offset for {@link #drawTextImpl(Vec, String, TextOptions)}.
	 *
	 * @param text
	 *            the line
	 * @param metrics
	 *            the metrics
	 * @return the dimensions for the correction of positioning
	 */
	private Dimension getOffset(String text, FontMetrics metrics) {
		// get the height of a line of text in this
		// font and render context
		int hgt = metrics.getHeight();
		// get the advance of my text in this font
		// and render context
		int adv = metrics.stringWidth(text);
		// calculate the size of a box to hold the
		// text with some padding.
		return new Dimension(adv + 2, hgt + 2);

	}

	/**
	 * Translate a vec3D to a vec2D.
	 *
	 * @param vec3D
	 *            the vec3D
	 * @return the vec2D
	 */
	private Vec translate2D(Vec vec3D) {
		if (vec3D.getZ() != 1) {
			return vec3D.translate2D(this.cameraOffsetUnits);
		}
		return vec3D;
	}

	/**
	 * Set the current filter.
	 *
	 * @param filter
	 *            the filter or {@code null} for deleting current filters
	 */
	void setFilter(Filter filter) {
		if (filter == null) {
			this.filter = null;
		} else {
			this.filter = filter;
		}
	}

	/**
	 * Set the background of the field.
	 *
	 * @param in
	 *            the color
	 */
	public void setBackground(RGBColor in) {
		RGBColor col = (this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		this.graphics.setColor(Utils.calcRGB(col));
		this.graphics.fillRect(0, 0, GameConf.PIXEL_W, GameConf.PIXEL_H);

	}

	// Adapt methods (separate world position calculation from drawing)

	@Override
	public void drawRectangle(Vec pos, Vec size, RGBAColor in, boolean inGame, boolean usefilter) {
		RGBAColor col = (!usefilter || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		if (!inGame) {
			this.drawRectangleImpl(this.translate2D(pos), size, Utils.calcRGBA(col));
		} else {
			Vec newPos = this.translate2D(pos);
			newPos = CalcUtil.units2pixel(newPos);
			newPos = newPos.addX(this.cameraOffset);

			Vec newSize = CalcUtil.units2pixel(size);

			this.drawRectangleImpl(newPos, newSize, Utils.calcRGBA(col));
		}
	}

	@Override
	public void drawPath(Vec startPos, List<Vec> pts, RGBColor in) {
		if (pts.size() == 0) {
			return;
		}

		RGBColor col = (this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);

		Vec newPos = this.translate2D(startPos);
		newPos = CalcUtil.units2pixel(newPos);
		newPos = newPos.addX(this.cameraOffset);

		Iterator<Vec> it = pts.iterator();

		Vec lastPt = newPos.add(CalcUtil.units2pixel(it.next()));

		this.graphics.setColor(Utils.calcRGB(col));
		this.graphics.setStroke(new BasicStroke(1));
		while (it.hasNext()) {
			Vec pt = newPos.add(CalcUtil.units2pixel(it.next()));
			this.graphics.drawLine((int) lastPt.getX(), (int) lastPt.getY(), (int) pt.getX(), (int) pt.getY());
			lastPt = pt;
		}
	}

	@Override
	public void drawCircle(Vec pos, Vec size, RGBAColor in, boolean inGame, boolean usefilter) {
		RGBAColor col = (!usefilter || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		if (!inGame) {
			this.drawCircleImpl(this.translate2D(pos), size, Utils.calcRGBA(col));
		} else {
			Vec newPos = this.translate2D(pos);
			newPos = CalcUtil.units2pixel(newPos);
			newPos = newPos.addX(this.cameraOffset);

			Vec newSize = CalcUtil.units2pixel(size);

			this.drawCircleImpl(newPos, newSize, Utils.calcRGBA(col));
		}
	}

	@Override
	public void drawPolygon(Polygon polygon, RGBAColor in, boolean fill, boolean inGame, boolean usefilter) {
		RGBAColor col = (!usefilter || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		polygon.moveTo(this.translate2D(polygon.getStartPoint()));

		float[] unitArray = polygon.getAbsoluteArray();
		int[] pixelArray = new int[unitArray.length];

		// calculate to pixels and add level scrolling offset
		for (int i = 0; i < unitArray.length; i += 2) {
			pixelArray[i] = this.cameraOffset + CalcUtil.units2pixel(unitArray[i]);
			pixelArray[i + 1] = CalcUtil.units2pixel(unitArray[i + 1]);
		}

		this.drawPolygonImpl(pixelArray, Utils.calcRGBA(col), fill);
	}

	@Override
	public void drawImage(Vec pos, Vec size, String imagePath, boolean inGame, boolean usefilter) {
		if (!inGame) {
			this.drawImageImpl(pos, size, imagePath, usefilter);
		} else {
			Vec newPos = CalcUtil.units2pixel(pos);
			newPos = newPos.addX(this.cameraOffset);
			Vec newSize = CalcUtil.units2pixel(size);
			this.drawImageImpl(newPos, newSize, imagePath, usefilter);
		}
	}

	@Override
	public void drawText(Vec pos, String text, TextOptions options, boolean inGame) {
		if (!inGame) {
			this.drawTextImpl(pos, text, options);
		} else {
			Vec newPos = CalcUtil.units2pixel(pos);
			newPos = newPos.addX(this.cameraOffset);
			this.drawTextImpl(newPos, text, options);
		}
	}

	@Override
	public void drawRoundRectangle(Vec pos, Vec size, RGBAColor in, float arcWidth, float arcHeight, boolean inGame, boolean usefilter) {
		RGBAColor col = (!usefilter || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		if (!inGame) {
			this.drawRoundRectangleImpl(this.translate2D(pos), size, Utils.calcRGBA(col), (int) arcWidth, (int) arcHeight);
		} else {
			Vec newPos = this.translate2D(pos);
			newPos = CalcUtil.units2pixel(newPos);
			newPos = newPos.addX(this.cameraOffset);

			Vec newSize = CalcUtil.units2pixel(size);

			this.drawRoundRectangleImpl(newPos, newSize, Utils.calcRGBA(col), CalcUtil.units2pixel(arcWidth), CalcUtil.units2pixel(arcHeight));
		}

	}

}