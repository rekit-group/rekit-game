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
import java.util.Objects;

import org.fuchss.tools.tuple.Tuple3;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.filters.Filter;
import rekit.primitives.TextOptions;
import rekit.primitives.geometry.Polygon;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.CalcUtil;

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
	private Vec cameraOffset = new Vec();
	/**
	 * The current camera offset in units.
	 */
	private Vec cameraOffsetUnits = new Vec();
	/**
	 * The current filter.
	 */
	private Filter filter;
	/**
	 * The current graphics for drawing.
	 */
	private Graphics2D graphics;
	/**
	 * The image cache: (Path, Filter, etc) -&gt; Image.
	 */
	private final Map<CacheKey, Image> images = new HashMap<>();

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
	private static final Vec CORRECTION = new Vec(0.1f, 0.5f);

	@Override
	public void setCurrentOffset(float cameraOffsetUnits) {
		this.cameraOffsetUnits = GameGridImpl.CORRECTION.addX(cameraOffsetUnits);
		this.cameraOffset = CalcUtil.units2pixel(this.cameraOffsetUnits).scalar(-1, 1);
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
		Tuple3<Vec, Vec, Color> preProcessing = this.preProcessing(pos, size, in, inGame, usefilter);
		this.drawRectangleImpl(preProcessing.getFirst(), preProcessing.getSecond(), preProcessing.getThird());
	}

	@Override
	public void drawCircle(Vec pos, Vec size, RGBAColor in, boolean inGame, boolean usefilter) {
		Tuple3<Vec, Vec, Color> preProcessing = this.preProcessing(pos, size, in, inGame, usefilter);
		this.drawCircleImpl(preProcessing.getFirst(), preProcessing.getSecond(), preProcessing.getThird());
	}

	@Override
	public void drawPolygon(Polygon polygon, RGBAColor in, boolean fill, boolean usefilter) {
		RGBAColor col = (!usefilter || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		polygon.moveTo(this.translate2D(polygon.getStartPoint(), false));

		float[] unitArray = polygon.getAbsoluteArray();
		int[] pixelArray = new int[unitArray.length];

		// calculate to pixels and add level scrolling offset
		for (int i = 0; i < unitArray.length; i += 2) {
			pixelArray[i] = (int) (this.cameraOffset.x + CalcUtil.units2pixel(unitArray[i]));
			pixelArray[i + 1] = (int) (this.cameraOffset.y + CalcUtil.units2pixel(unitArray[i + 1]));
		}
		this.drawPolygonImpl(pixelArray, this.calcRGBA(col), fill);
	}

	@Override
	public void drawImage(Vec pos, Vec size, String imagePath, boolean inGame, boolean usefilter, boolean mirrorX, boolean mirrorY) {
		Tuple3<Vec, Vec, Color> preProcessing = this.preProcessing(pos, size, new RGBAColor(0), inGame, usefilter);
		this.drawImageImpl(preProcessing.getFirst(), preProcessing.getSecond(), imagePath, usefilter, mirrorX, mirrorY);
	}

	@Override
	public void drawText(Vec pos, String text, TextOptions options, boolean inGame) {
		this.drawTextImpl(this.translate2D(pos, inGame), text, options);
	}

	@Override
	public void drawRoundRectangle(Vec pos, Vec size, RGBAColor in, float arcWidth, float arcHeight, boolean inGame, boolean usefilter) {
		Tuple3<Vec, Vec, Color> preProcessing = this.preProcessing(pos, size, in, inGame, usefilter);
		int calcArcWidth = inGame ? CalcUtil.units2pixel(arcWidth) : (int) arcWidth;
		int calcArcHeight = inGame ? CalcUtil.units2pixel(arcHeight) : (int) arcHeight;
		this.drawRoundRectangleImpl(preProcessing.getFirst(), preProcessing.getSecond(), preProcessing.getThird(), calcArcWidth, calcArcHeight);
	}

	@Override
	public void drawLine(Vec a, Vec b, int lineWidth, RGBAColor color, boolean ingame, boolean usefilter) {
		// calc col and position
		RGBAColor col = (!usefilter || this.filter == null || !this.filter.isApplyPixel()) ? color : this.filter.apply(color);
		Vec calcA = this.translate2D(a, ingame);
		Vec calcB = this.translate2D(b, ingame);

		// set parameters for drawing
		this.graphics.setColor(this.calcRGBA(col));
		this.graphics.setStroke(new BasicStroke(lineWidth));

		// draw line
		this.graphics.drawLine((int) calcA.x, (int) calcA.y, (int) calcB.x, (int) calcB.y);
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

	private Tuple3<Vec, Vec, Color> preProcessing(Vec pos, Vec size, RGBAColor in, boolean inGame, boolean usefilter) {
		Vec calcPos = this.translate2D(pos, inGame);
		Vec calcSize = inGame ? CalcUtil.units2pixel(size) : size;
		RGBAColor col = (!usefilter || this.filter == null || !this.filter.isApplyPixel()) ? in : this.filter.apply(in);
		return Tuple3.of(calcPos, calcSize, this.calcRGBA(col));
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
		Vec perspective = vec3D.z != 0 ? vec3D.translate2D(this.cameraOffsetUnits.x) : vec3D;

		Vec newPos = perspective;
		if (ingame) {
			newPos = CalcUtil.units2pixel(newPos);
			newPos = newPos.add(this.cameraOffset);
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

	private void drawCircleImpl(Vec pos, Vec size, Color col) {
		this.graphics.setColor(col);
		Ellipse2D.Float circle = new Ellipse2D.Float(//
				(pos.x - size.x / 2f), //
				(pos.y - size.y / 2f), //
				size.x, size.y);

		this.graphics.fill(circle);

	}

	private void drawRectangleImpl(Vec pos, Vec size, Color col) {
		this.graphics.setColor(col);
		this.graphics.fillRect(//
				(int) (pos.x - size.x / 2f), //
				(int) (pos.y - size.y / 2f), //
				(int) size.x, (int) size.y);

	}

	private void drawRoundRectangleImpl(Vec pos, Vec size, Color col, int arcWidth, int arcHeight) {
		this.graphics.setColor(col);
		this.graphics.fillRoundRect(//
				(int) (pos.x - size.x / 2f), // X
				(int) (pos.y - size.y / 2f), // Y
				(int) size.x, (int) size.y, // Size
				arcWidth, arcHeight); // arc

	}

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

	private void drawImageImpl(Vec pos, Vec size, String imagePath, boolean usefilter, boolean mirrorX, boolean mirrorY) {
		Image image = null;
		CacheKey key = new CacheKey(imagePath, usefilter ? this.filter : null, mirrorX, mirrorY);
		if (this.images.containsKey(key) && !(this.filter != null && this.filter.changed())) {
			image = this.images.get(key);
		} else {
			if (this.filter != null && this.filter.isApplyImage()) {
				image = ImageManagement.toImage(this.filter.apply(ImageManagement.getAsAbstractImage(imagePath, mirrorX, mirrorY)));
			} else {
				image = ImageManagement.get(imagePath, mirrorX, mirrorY);
			}
			this.images.put(key, image);
			GameConf.GAME_LOGGER.debug("GameGrid: Image Cache Miss: " + key);
		}

		this.graphics.drawImage(image, // image
				(int) (pos.x - size.x / 2f), // dstX
				(int) (pos.y - size.y / 2f), // dstY
				null);

	}

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

	private static final class CacheKey {
		public final String image;
		public final Filter filter;
		public final boolean mirrorX;
		public final boolean mirrorY;

		public CacheKey(String image, Filter filter, boolean mirrorX, boolean mirrorY) {
			this.image = image;
			this.filter = filter;
			this.mirrorX = mirrorX;
			this.mirrorY = mirrorY;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.filter == null) ? 0 : this.filter.hashCode());
			result = prime * result + ((this.image == null) ? 0 : this.image.hashCode());
			result = prime * result + (this.mirrorX ? 1231 : 1237);
			result = prime * result + (this.mirrorY ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || this.getClass() != obj.getClass()) {
				return false;
			}
			CacheKey other = (CacheKey) obj;
			return Objects.equals(this.filter, other.filter) && Objects.equals(this.image, other.image) && this.mirrorX == other.mirrorX && this.mirrorY == other.mirrorY;
		}

	}

}
