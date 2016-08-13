package edu.kit.informatik.ragnarok.gui;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.primitives.geometry.Polygon;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.Filter;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.CalcUtil;
import edu.kit.informatik.ragnarok.util.SwtUtils;
import edu.kit.informatik.ragnarok.util.TextOptions;

/**
 * This class represents a {@link Field} of the {@link GameView}
 *
 * @author Angelo Aracri
 * @author Dominik Fuchß
 *
 */
class FieldImpl extends Field {
	/**
	 * The GC
	 */
	private GC gc;
	/**
	 * The current camera offset
	 */
	private int cameraOffset = 0;
	/**
	 * The current camera offset in units
	 */
	private float cameraOffsetUnits = 0;

	private Filter filter;

	@Override
	public void setCurrentOffset(float cameraOffset) {
		this.cameraOffsetUnits = cameraOffset;
		this.cameraOffset = -CalcUtil.units2pixel(cameraOffset);
	}

	private void drawCircleImpl(Vec pos, Vec size, RGBA col) {
		// set color
		this.gc.setAlpha(col.alpha);
		Color color = new Color(Display.getCurrent(), col);
		this.gc.setBackground(color);
		color.dispose();
		this.gc.fillOval((int) (pos.getX() - size.getX() / 2f), (int) (pos.getY() - size.getY() / 2f), (int) size.getX(), (int) size.getY());

		// reset alpha
		this.gc.setAlpha(255);
	}

	private void drawRectangleImpl(Vec pos, Vec size, RGBA col) {
		// set color
		this.gc.setAlpha(col.alpha);
		Color color = new Color(Display.getCurrent(), col);

		this.gc.setBackground(color);
		color.dispose();
		this.gc.fillRectangle((int) (pos.getX() - size.getX() / 2f), (int) (pos.getY() - size.getY() / 2f), (int) size.getX(), (int) size.getY());

		// reset alpha
		this.gc.setAlpha(255);
	}

	private void drawRoundRectangleImpl(Vec pos, Vec size, RGBA col, int arcWidth, int arcHeight) {
		// set color
		this.gc.setAlpha(col.alpha);
		Color color = new Color(Display.getCurrent(), col);
		this.gc.setBackground(color);
		color.dispose();
		this.gc.fillRoundRectangle( //
				(int) (pos.getX() - size.getX() / 2f), // X
				(int) (pos.getY() - size.getY() / 2f), // Y
				(int) size.getX(), (int) size.getY(), // Size
				arcWidth, arcHeight); // arc
		// reset alpha
		this.gc.setAlpha(255);

	}

	private void drawPolygonImpl(int[] pixelArray, RGBA col, boolean fill) {
		// set color
		this.gc.setAlpha(col.alpha);
		Color color = new Color(Display.getCurrent(), col);

		// draw actual polygon
		if (fill) {
			this.gc.setBackground(color);
			this.gc.fillPolygon(pixelArray);
		} else {
			this.gc.setForeground(color);
			this.gc.setLineWidth(1);
			this.gc.drawPolygon(pixelArray);
		}

		color.dispose();

		this.gc.setAlpha(255);
	}

	/**
	 * Draw an image
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 * @param imagePath
	 *            the image path
	 */
	private void drawImageImpl(Vec pos, Vec size, String imagePath) {
		Image image = ImageLoader.get(imagePath);
		this.gc.drawImage(image, // image
				(int) (pos.getX() - size.getX() / 2f), // dstX
				(int) (pos.getY() - size.getY() / 2f) // dstY
		);

	}

	/**
	 * Draw an text
	 *
	 * @param pos
	 *            the position
	 * @param text
	 *            the text
	 * @param options
	 *            the options
	 */
	private void drawTextImpl(Vec pos, String text, TextOptions options) {
		// Set color to red and set font
		RGBColor in = new RGBColor(options.getColor().red, options.getColor().green, options.getColor().blue);
		RGBColor col = this.filter == null ? in : this.filter.apply(in);
		Color color = new Color(Display.getCurrent(), SwtUtils.calcRGB(col));
		this.gc.setForeground(color);
		color.dispose();

		Font font = new Font(Display.getCurrent(), options.getFont(), options.getHeight(), options.getFontOptions() | SWT.BOLD);
		this.gc.setFont(font);

		Point textBounds = this.gc.textExtent(text);

		this.gc.drawText(text, // text
				(int) (pos.getX() + options.getAlignment().getX() * textBounds.x), // dstX
				(int) (pos.getY() + options.getAlignment().getY() * textBounds.y), // dstY
				true);
		font.dispose();
	}

	/**
	 * Set the current GC
	 *
	 * @param gc
	 *            the gc
	 */
	public void setGC(GC gc) {
		this.gc = gc;
	}

	/**
	 * Translate a vec3D to a vec2D
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

	public void setFilter(Filter filter) {
		if (filter == null || !filter.isApplyPixel()) {
			this.filter = null;
		} else {
			this.filter = filter;
		}
	}

	/**
	 * Set the background of the field
	 *
	 * @param col
	 *            the color
	 */
	public void setBackground(RGBColor in) {
		RGBColor col = this.filter == null ? in : this.filter.apply(in);
		this.gc.setBackground(new Color(Display.getCurrent(), SwtUtils.calcRGB(col)));
		this.gc.fillRectangle(0, 0, GameConf.PIXEL_W, GameConf.PIXEL_H);

	}

	// Adapt methods (separate world position calculation from drawing)

	@Override
	public void drawRectangle(Vec pos, Vec size, RGBAColor in, boolean inGame) {
		RGBAColor col = this.filter == null ? in : this.filter.apply(in);
		if (!inGame) {
			this.drawRectangleImpl(this.translate2D(pos), size, SwtUtils.calcRGBA(col));
		} else {
			Vec newPos = this.translate2D(pos);
			newPos = CalcUtil.units2pixel(newPos);
			newPos = newPos.addX(this.cameraOffset);

			Vec newSize = CalcUtil.units2pixel(size);

			this.drawRectangleImpl(newPos, newSize, SwtUtils.calcRGBA(col));
		}
	}

	@Override
	public void drawPath(Vec startPos, List<Vec> pts, RGBColor in) {
		if (pts.size() == 0) {
			return;
		}

		RGBColor col = this.filter == null ? in : this.filter.apply(in);

		Vec newPos = this.translate2D(startPos);
		newPos = CalcUtil.units2pixel(newPos);
		newPos = newPos.addX(this.cameraOffset);

		Iterator<Vec> it = pts.iterator();

		Vec lastPt = newPos.add(CalcUtil.units2pixel(it.next()));

		this.gc.setLineWidth(1);
		this.gc.setForeground(new Color(Display.getCurrent(), SwtUtils.calcRGB(col)));
		while (it.hasNext()) {
			Vec pt = newPos.add(CalcUtil.units2pixel(it.next()));
			this.gc.drawLine((int) lastPt.getX(), (int) lastPt.getY(), (int) pt.getX(), (int) pt.getY());
			lastPt = pt;
		}
	}

	@Override
	public void drawCircle(Vec pos, Vec size, RGBAColor in, boolean inGame) {
		RGBAColor col = this.filter == null ? in : this.filter.apply(in);
		if (!inGame) {
			this.drawCircleImpl(this.translate2D(pos), size, SwtUtils.calcRGBA(col));
		} else {
			Vec newPos = this.translate2D(pos);
			newPos = CalcUtil.units2pixel(newPos);
			newPos = newPos.addX(this.cameraOffset);

			Vec newSize = CalcUtil.units2pixel(size);

			this.drawCircleImpl(newPos, newSize, SwtUtils.calcRGBA(col));
		}
	}

	@Override
	public void drawPolygon(Polygon polygon, RGBAColor in, boolean fill, boolean inGame) {
		RGBAColor col = this.filter == null ? in : this.filter.apply(in);
		polygon.moveTo(this.translate2D(polygon.getStartPoint()));

		float[] unitArray = polygon.getAbsoluteArray();
		int[] pixelArray = new int[unitArray.length];

		// calculate to pixels and add level scrolling offset
		for (int i = 0; i < unitArray.length; i += 2) {
			pixelArray[i] = this.cameraOffset + CalcUtil.units2pixel(unitArray[i]);
			pixelArray[i + 1] = CalcUtil.units2pixel(unitArray[i + 1]);
		}

		this.drawPolygonImpl(pixelArray, SwtUtils.calcRGBA(col), fill);
	}

	@Override
	public void drawImage(Vec pos, Vec size, String imagePath, boolean inGame) {
		if (!inGame) {
			this.drawImageImpl(pos, size, imagePath);
		} else {
			Vec newPos = CalcUtil.units2pixel(pos);
			newPos = newPos.addX(this.cameraOffset);
			Vec newSize = CalcUtil.units2pixel(size);
			this.drawImageImpl(newPos, newSize, imagePath);
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
	public void drawRoundRectangle(Vec pos, Vec size, RGBAColor in, float arcWidth, float arcHeight, boolean inGame) {
		RGBAColor col = this.filter == null ? in : this.filter.apply(in);
		if (!inGame) {
			this.drawRoundRectangleImpl(this.translate2D(pos), size, SwtUtils.calcRGBA(col), (int) arcWidth, (int) arcHeight);
		} else {
			Vec newPos = this.translate2D(pos);
			newPos = CalcUtil.units2pixel(newPos);
			newPos = newPos.addX(this.cameraOffset);

			Vec newSize = CalcUtil.units2pixel(size);

			this.drawRoundRectangleImpl(newPos, newSize, SwtUtils.calcRGBA(col), CalcUtil.units2pixel(arcWidth), CalcUtil.units2pixel(arcHeight));
		}

	}

}
