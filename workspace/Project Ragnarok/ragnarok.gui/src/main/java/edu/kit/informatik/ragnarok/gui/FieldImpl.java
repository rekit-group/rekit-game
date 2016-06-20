package edu.kit.informatik.ragnarok.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;

/**
 * This class represents a {@link Field} of the {@link GameView}
 *
 * @author Angelo Aracri
 * @author Dominik Fuchß
 *
 */
public class FieldImpl implements Field {

	private GC gc;
	private GameView view;

	public FieldImpl(GameView view) {
		this.view = view;
	}

	private int units2pixel(float units) {
		return (int) (units * GameConf.pxPerUnit);
	}

	private int currentOffset() {
		return -this.units2pixel(this.view.getModel().getCameraOffset());
	}

	public void setBackground(RGB col) {
		this.gc.setBackground(new Color(Display.getCurrent(), col));
		this.gc.fillRectangle(0, 0, this.units2pixel(GameConf.gridW), this.units2pixel(GameConf.gridH));
	}

	public void drawCircle(Vec2D pos, Vec2D size, RGB col) {
		Color color = new Color(Display.getCurrent(), col);
		this.gc.setBackground(color);
		color.dispose();
		this.gc.fillOval(this.currentOffset() + this.units2pixel((pos.getX() - size.getX() / 2f)),
				this.units2pixel((pos.getY() - size.getY() / 2f)), this.units2pixel(size.getX()), this.units2pixel(size.getY()));
	}

	public void drawRectangle(Vec2D pos, Vec2D size, RGB col) {
		Color color = new Color(Display.getCurrent(), col);
		this.gc.setBackground(color);
		color.dispose();
		this.gc.fillRectangle(this.currentOffset() + this.units2pixel(pos.getX() - size.getX() / 2f),
				this.units2pixel(pos.getY() - size.getY() / 2f), this.units2pixel(size.getX()), this.units2pixel(size.getY()));
	}

	public void drawPolygon(Polygon polygon, RGB col) {
		RGBA actualCol = new RGBA(col.red, col.green, col.blue, 255);
		this.drawPolygon(polygon, actualCol);

	}

	public void drawPolygon(Polygon polygon, RGBA col) {
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

	@Override
	public void drawImage(Vec2D pos, Vec2D size, String imagePath) {
		Image image = ImageLoader.get(imagePath);
		this.gc.drawImage(image, this.currentOffset() + this.units2pixel(pos.getX() - size.getX() / 2f), // dstX
				this.units2pixel(pos.getY() - size.getY() / 2f) // dstY
		);
	}

	public void refreshUI(int lifes, int points, int highScore, String bossText) {

		// Iterate lifes
		Image image = ImageLoader.get("mrRekt_glasses_right.png");
		for (int i = 0; i < lifes; i++) {
			this.gc.drawImage(image, 10 + 50 * i, 10);
		}
		// Set color to red and set font
		Color color = new Color(Display.getCurrent(), new RGB(200, 50, 0));
		this.gc.setForeground(color);
		color.dispose();
		Font font = new Font(Display.getCurrent(), "Tahoma", 18, SWT.BOLD);
		this.gc.setFont(font);
		// There is no alignment, so we need to calculate the text width
		String text = points + " Points";
		int textWidth = this.gc.stringExtent(text).x;
		int textHeight = this.gc.stringExtent(text).y;
		// And draw the text
		this.gc.drawText(text, this.units2pixel(GameConf.gridW) - textWidth - 10, 10, true);

		// There is no alignment, so we need to calculate the text width
		text = highScore + " HighScore";
		textWidth = this.gc.stringExtent(text).x;
		// And draw the text
		this.gc.drawText(text, this.units2pixel(GameConf.gridW) - textWidth - 10, 10 + 10 + textHeight, true);
		font.dispose();

		if (bossText != null) {
			font = new Font(Display.getCurrent(), "Tahoma", 30, SWT.BOLD);
			this.gc.setFont(font);
			// There is no alignment, so we need to calculate the text width
			textWidth = this.gc.stringExtent(bossText).x;
			textHeight = this.gc.stringExtent(bossText).y;
			// And draw the text
			this.gc.drawText(bossText, (this.units2pixel(GameConf.gridW) - textWidth) / 2,
					(this.units2pixel(GameConf.gridH) - textHeight) / 2, true);
		}

	}

	public void drawFPS(float fps) {
		// Set color to red and set font
		Color color = new Color(Display.getCurrent(), new RGB(200, 50, 0));
		this.gc.setForeground(color);
		color.dispose();
		Font font = new Font(Display.getCurrent(), "Tahoma", 18, SWT.BOLD);
		this.gc.setFont(font);
		font.dispose();
		// There is no alignment, so we need to calculate the text width
		String text = "FPS: " + fps;
		int textWidth = this.gc.stringExtent(text).x;
		// And draw the text
		this.gc.drawText(text, this.units2pixel(GameConf.gridW) - textWidth - 10, this.units2pixel(GameConf.gridH) - 60, true);
	}

	public void setGC(GC gc) {
		this.gc = gc;
	}

	@Override
	public void drawRectangle(Vec2D pos, Vec2D size, RGBColor color) {
		this.drawRectangle(pos, size, new RGB(color.red, color.green, color.blue));

	}

	@Override
	public void drawCircle(Vec2D pos, Vec2D size, RGBColor color) {
		this.drawCircle(pos, size, new RGB(color.red, color.green, color.blue));
	}

	@Override
	public void drawPolygon(Polygon polygon, RGBAColor color) {
		this.drawPolygon(polygon, new RGBA(color.red, color.green, color.blue, color.alpha));
	}

	@Override
	public void drawPolygon(Polygon polygon, RGBColor color) {
		this.drawPolygon(polygon, new RGB(color.red, color.green, color.blue));
	}
}
