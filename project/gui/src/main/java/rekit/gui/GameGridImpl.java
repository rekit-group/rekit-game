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
import rekit.logic.filters.Filter;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.CalcUtil;
import rekit.util.TextOptions;
import rekit.util.Triple;
import rekit.util.Tuple;

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
	 * The image cache: (Path, Filter) -&gt; Image.
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

	/**
	 * Offset correction value, estimated by tests.
	 */
	private static final float CORRECTION = 0.1F;

	@Override
	public void setCurrentOffset(float cameraOffsetUnits) {
		this.cameraOffsetUnits = cameraOffsetUnits + GameGridImpl.CORRECTION;
		this.cameraOffset = -CalcUtil.units2pixel(cameraOffsetUnits + GameGridImpl.CORRECTION);
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
		Ellipse2D.Float circle = new Ellipse2D.Float(//
				(pos.x - size.x / 2f), //
				(pos.y - size.y / 2f), //
				size.x, size.y);

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
		this.graphics.fillRect(//
				(int) (pos.x - size.x / 2f), //
				(int) (pos.y - size.y / 2f), //
				(int) size.x, (int) size.y);

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
		this.graphics.fillRoundRect(//
				(int) (pos.x - size.x / 2f), // X
				(int) (pos.y - size.y / 2f), // Y
				(int) size.x, (int) size.y, // Size
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
				(int) (pos.x - size.x / 2f), // dstX
				(int) (pos.y - size.y / 2f), // dstY
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
		RGBAColor in = options.getColor();
		RGBAColor col = (!options.getUseFilter() || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		this.graphics.setColor(this.calcRGBA(col));

		Font font = new Font(options.getFont(), options.getFontOptions(), options.getHeight());
		this.graphics.setFont(font);
		FontMetrics metrics = this.graphics.getFontMetrics(font);

		float x = pos.x;
		float y = pos.y;
		float xAlign = options.getAlignment().x;
		float yAlign = options.getAlignment().y;
		for (String line : text.split("\n")) {
			Dimension offset = this.getTextOffset(line, metrics);
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
	private Dimension getTextOffset(String text, FontMetrics metrics) {
		// get the height of a line of text in this font and render context
		int hgt = metrics.getHeight();
		// get the advance of my text in this font and render context
		int adv = metrics.stringWidth(text);
		// calculate the size of a box to hold the text with some padding.
		return new Dimension(adv + 2, hgt + 2);
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
	public void setBackground(RGBAColor in) {
		RGBAColor col = (this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		this.graphics.setColor(this.calcRGBA(col));
		this.graphics.fillRect(0, 0, GameConf.PIXEL_W, GameConf.PIXEL_H);

	}

	// Adapt methods (separate world position calculation from drawing)

	@Override
	public void drawRectangle(Vec pos, Vec size, RGBAColor in, boolean inGame, boolean usefilter) {
		Triple<Vec, Vec, Color> preProcessing = this.preProcessing(pos, size, in, inGame, usefilter);
		this.drawRectangleImpl(preProcessing.getT(), preProcessing.getU(), preProcessing.getV());
	}

	@Override
	public void drawCircle(Vec pos, Vec size, RGBAColor in, boolean inGame, boolean usefilter) {
		Triple<Vec, Vec, Color> preProcessing = this.preProcessing(pos, size, in, inGame, usefilter);
		this.drawCircleImpl(preProcessing.getT(), preProcessing.getU(), preProcessing.getV());
	}

	@Override
	public void drawPolygon(Polygon polygon, RGBAColor in, boolean fill, boolean usefilter) {
		RGBAColor col = (!usefilter || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		polygon.moveTo(this.translate2D(polygon.getStartPoint(), false));

		float[] unitArray = polygon.getAbsoluteArray();
		int[] pixelArray = new int[unitArray.length];

		// calculate to pixels and add level scrolling offset
		for (int i = 0; i < unitArray.length; i += 2) {
			pixelArray[i] = this.cameraOffset + CalcUtil.units2pixel(unitArray[i]);
			pixelArray[i + 1] = CalcUtil.units2pixel(unitArray[i + 1]);
		}

		this.drawPolygonImpl(pixelArray, this.calcRGBA(col), fill);
	}

	@Override
	public void drawImage(Vec pos, Vec size, String imagePath, boolean inGame, boolean usefilter) {
		Triple<Vec, Vec, Color> preProcessing = this.preProcessing(pos, size, new RGBAColor(0), inGame, usefilter);
		this.drawImageImpl(preProcessing.getT(), preProcessing.getU(), imagePath, usefilter);
	}

	@Override
	public void drawText(Vec pos, String text, TextOptions options, boolean inGame) {
		this.drawTextImpl(this.translate2D(pos, inGame), text, options);
	}

	@Override
	public void drawRoundRectangle(Vec pos, Vec size, RGBAColor in, float arcWidth, float arcHeight, boolean inGame, boolean usefilter) {
		Triple<Vec, Vec, Color> preProcessing = this.preProcessing(pos, size, in, inGame, usefilter);
		int calcArcWidth = inGame ? CalcUtil.units2pixel(arcWidth) : (int) arcWidth;
		int calcArcHeight = inGame ? CalcUtil.units2pixel(arcHeight) : (int) arcHeight;
		this.drawRoundRectangleImpl(preProcessing.getT(), preProcessing.getU(), preProcessing.getV(), calcArcWidth, calcArcHeight);
	}

	@Override
	public void drawPath(Vec startPos, List<Vec> pts, RGBAColor in, int lineWidth, boolean usefilter) {
		if (pts.size() == 0) {
			return;
		}

		RGBAColor col = (!usefilter || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		Vec calcPos = this.translate2D(startPos, true);

		Iterator<Vec> it = pts.iterator();

		Vec lastPt = calcPos.add(CalcUtil.units2pixel(it.next()));
		this.graphics.setColor(this.calcRGBA(col));
		this.graphics.setStroke(new BasicStroke(lineWidth));
		while (it.hasNext()) {
			Vec pt = calcPos.add(CalcUtil.units2pixel(it.next()));
			this.graphics.drawLine((int) lastPt.x, (int) lastPt.y, (int) pt.x, (int) pt.y);
			lastPt = pt;
		}
	}

	private Triple<Vec, Vec, Color> preProcessing(Vec pos, Vec size, RGBAColor in, boolean inGame, boolean usefilter) {
		Vec calcPos = this.translate2D(pos, inGame);
		Vec calcSize = inGame ? CalcUtil.units2pixel(size) : size;
		RGBAColor col = (!usefilter || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		return Triple.create(calcPos, calcSize, this.calcRGBA(col));
	}

	/**
	 * Translate a vec3D to a vec2D.
	 *
	 * @param vec3D
	 *            the vec3D
	 * @param ingame
	 *            indicates whether it shall drawn as entity of the game
	 *            (relative to current game progress) or relative to the
	 *            surrounding frame
	 * @return the vec2D
	 */
	private Vec translate2D(Vec vec3D, boolean ingame) {
		Vec perspective = vec3D.z != 0 ? vec3D.translate2D(this.cameraOffsetUnits) : vec3D;

		Vec newPos = perspective;
		if (ingame) {
			newPos = CalcUtil.units2pixel(newPos);
			newPos = newPos.addX(this.cameraOffset);
		}
		return newPos;
	}

	/**
	 * Convert a {@link RGBAColor} to a {@link Color}.
	 *
	 * @param color
	 *            the color
	 * @return the converted color
	 */
	private Color calcRGBA(RGBAColor color) {
		return new Color(color.red, color.green, color.blue, color.alpha);

	}
}
