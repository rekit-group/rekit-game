package edu.kit.informatik.ragnarok.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;
import edu.kit.informatik.ragnarok.util.SwtUtils;
import edu.kit.informatik.ragnarok.util.TextOptions;

/**
 * This class represents a {@link Field} of the {@link GameView}
 *
 * @author Angelo Aracri
 * @author Dominik Fuchß
 *
 */
public class FieldImpl implements Field {

	private GC gc;
	private int cameraOffset = 0;

	public FieldImpl() {
	}

	private int units2pixel(float units) {
		return (int) (units * GameConf.PX_PER_UNIT);
	}

	@Override
	public void setCurrentOffset(float cameraOffset) {
		this.cameraOffset = -this.units2pixel(cameraOffset);
	}

	private int currentOffset() {
		return this.cameraOffset;
	}

	public void setBackground(RGB col) {
		this.gc.setBackground(new Color(Display.getCurrent(), col));
		this.gc.fillRectangle(0, 0, this.units2pixel(GameConf.GRID_W), this.units2pixel(GameConf.GRID_H));
	}

	private void drawCircle(Vec2D pos, Vec2D size, RGBA col) {
		// set color
		this.gc.setAlpha(col.alpha);
		Color color = new Color(Display.getCurrent(), col);
		this.gc.setBackground(color);
		color.dispose();
		this.gc.fillOval(this.currentOffset() + this.units2pixel((pos.getX() - size.getX() / 2f)), this.units2pixel((pos.getY() - size.getY() / 2f)),
				this.units2pixel(size.getX()), this.units2pixel(size.getY()));

		// reset alpha
		this.gc.setAlpha(255);
	}

	private void drawCircle(Vec2D pos, Vec2D size, RGB col) {
		this.drawCircle(pos, size, new RGBA(col.red, col.green, col.blue, 255));
	}

	private void drawRectangle(Vec2D pos, Vec2D size, RGBA col) {
		// set color
		this.gc.setAlpha(col.alpha);
		Color color = new Color(Display.getCurrent(), col);

		this.gc.setBackground(color);
		color.dispose();
		this.gc.fillRectangle(this.currentOffset() + this.units2pixel(pos.getX() - size.getX() / 2f), this.units2pixel(pos.getY() - size.getY() / 2f),
				this.units2pixel(size.getX()), this.units2pixel(size.getY()));

		// reset alpha
		this.gc.setAlpha(255);
	}

	private void drawRectangle(Vec2D pos, Vec2D size, RGB col) {
		this.drawRectangle(pos, size, new RGBA(col.red, col.green, col.blue, 255));
	}

	private void drawPolygon(Polygon polygon, RGBA col) {
		// set color
		this.gc.setAlpha(col.alpha);
		Color color = new Color(Display.getCurrent(), col);
		this.gc.setBackground(color);
		color.dispose();

		float[] unitArray = polygon.getAbsoluteArray();
		int[] pixelArray = new int[unitArray.length];

		// calculate to pixels and add level scrolling offset
		for (int i = 0; i < unitArray.length; i += 2) {
			pixelArray[i] = this.currentOffset() + this.units2pixel(unitArray[i]);
			pixelArray[i + 1] = this.units2pixel(unitArray[i + 1]);
		}

		// draw actual polygon
		this.gc.fillPolygon(pixelArray);

		this.gc.setAlpha(255);
	}

	private void drawPolygon(Polygon polygon, RGB col) {
		RGBA actualCol = new RGBA(col.red, col.green, col.blue, 255);
		this.drawPolygon(polygon, actualCol);

	}

	@Override
	public void drawImage(Vec2D pos, Vec2D size, String imagePath) {
		Image image = ImageLoader.get(imagePath);
		this.gc.drawImage(image, // img
				this.currentOffset() + this.units2pixel(pos.getX() - size.getX() / 2f), // dstX
				this.units2pixel(pos.getY() - size.getY() / 2f) // dstY
		);
	}

	@Override
	public void drawGuiImage(Vec2D pos, Vec2D size, String imagePath) {
		Image image = ImageLoader.get(imagePath);
		this.gc.drawImage(image, // img
				(int) (pos.getX() - size.getX() / 2f), // dstX
				(int) (pos.getY() - size.getY() / 2f) // dstY
		);
	}

	@Override
	public void drawText(Vec2D pos, String text, TextOptions options) {
		// Set color to red and set font
		RGB rgb = new RGB(options.getColor().red, options.getColor().green, options.getColor().blue);
		Color color = new Color(Display.getCurrent(), rgb);
		this.gc.setForeground(color);
		color.dispose();

		Font font = new Font(Display.getCurrent(), options.getFont(), options.getHeight(), options.getFontOptions() | SWT.BOLD);
		this.gc.setFont(font);

		Point textBounds = this.gc.textExtent(text);

		this.gc.drawText(text, // txt
				(int) (pos.getX() + options.getAlignment().getX() * textBounds.x), // dstX
				(int) (pos.getY() + options.getAlignment().getY() * textBounds.y), // dstY
				true);
		font.dispose();
	}

	public void setGC(GC gc) {
		this.gc = gc;
	}

	// Adapt existing methods to Interface-Functions

	@Override
	public void drawRectangle(Vec2D pos, Vec2D size, RGBColor color) {
		this.drawRectangle(pos, size, SwtUtils.calcRGB(color));
	}

	@Override
	public void drawRectangle(Vec2D pos, Vec2D size, RGBAColor rgbaColor) {
		this.drawRectangle(pos, size, SwtUtils.calcRGBA(rgbaColor));
	}

	@Override
	public void drawCircle(Vec2D pos, Vec2D size, RGBAColor color) {
		this.drawCircle(pos, size, SwtUtils.calcRGBA(color));
	}

	@Override
	public void drawCircle(Vec2D pos, Vec2D size, RGBColor color) {
		this.drawCircle(pos, size, SwtUtils.calcRGB(color));
	}

	@Override
	public void drawPolygon(Polygon polygon, RGBAColor color) {
		this.drawPolygon(polygon, SwtUtils.calcRGBA(color));
	}

	@Override
	public void drawPolygon(Polygon polygon, RGBColor color) {
		this.drawPolygon(polygon, SwtUtils.calcRGB(color));
	}

}
