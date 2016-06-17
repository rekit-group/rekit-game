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
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class Field {

	private GC gc;
	private GameView view;

	public Field(GameView view) {
		this.view = view;
	}

	private int units2pixel(float units) {
		return (int) (units * GameConf.pxPerUnit);
	}

	private int currentOffset() {
		return -this.units2pixel(this.view.getModel().getCurrentOffset() - GameConf.playerDist);
	}

	public void setBackground(RGB col) {
		this.gc.setBackground(new Color(Display.getCurrent(), col));
		this.gc.fillRectangle(0, 0, this.units2pixel(GameConf.gridW), this.units2pixel(GameConf.gridH));
	}

	public void drawCircle(Vec2D pos, Vec2D size, RGB col) {
		this.gc.setBackground(new Color(Display.getCurrent(), col));
		this.gc.fillOval(this.currentOffset() + this.units2pixel((pos.getX() - size.getX() / 2f)),
				this.units2pixel((pos.getY() - size.getY() / 2f)), this.units2pixel(size.getX()),
				this.units2pixel(size.getY()));
	}

	public void drawRectangle(Vec2D pos, Vec2D size, RGB col) {
		this.gc.setBackground(new Color(Display.getCurrent(), col));
		this.gc.fillRectangle(this.currentOffset() + this.units2pixel(pos.getX() - size.getX() / 2f),
				this.units2pixel(pos.getY() - size.getY() / 2f), this.units2pixel(size.getX()),
				this.units2pixel(size.getY()));
	}

	public void drawPolygon(Polygon polygon, RGB col) {
		RGBA actualCol = new RGBA(col.red, col.green, col.blue, 255);
		this.drawPolygon(polygon, actualCol);
	}

	public void drawPolygon(Polygon polygon, RGBA col) {
		// set color
		this.gc.setAlpha(col.alpha);
		this.gc.setBackground(new Color(Display.getCurrent(), col));

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

	public void drawImage(Vec2D pos, Vec2D size, String imagePath) {
		Image image = ImageLoader.get(imagePath);
		this.gc.drawImage(image, this.currentOffset() + this.units2pixel(pos.getX() - size.getX() / 2f), // dstX
				this.units2pixel(pos.getY() - size.getY() / 2f) // dstY
		);
	}

	public void refreshUI(int lifes, int points, int highScore) {

		// Iterate lifes
		Image image = ImageLoader.get("mrRekt_glasses_right.png");
		for (int i = 0; i < lifes; i++) {
			this.gc.drawImage(image, 10 + 50 * i, 10);
		}

		// Set color to red and set font
		this.gc.setForeground(new Color(Display.getCurrent(), new RGB(200, 50, 0)));
		Font font = new Font(Display.getCurrent(), "Tahoma", 18, SWT.BOLD);
		this.gc.setFont(font);
		// There is no alignment, so we need to calculate the text width
		String text = points + " Points";
		int textWidth = this.gc.stringExtent(text).x;
		int textHeight = this.gc.stringExtent(text).y;
		// And draw the text
		this.gc.drawText(text, this.units2pixel(GameConf.gridW) - textWidth - 10, 10, true);
		
		//There is no alignment, so we need to calculate the text width
		text = highScore + " HighScore";
		textWidth = this.gc.stringExtent(text).x;
		// And draw the text
		this.gc.drawText(text, this.units2pixel(GameConf.gridW) - textWidth - 10, 10 + 10 + textHeight, true);
	}
	
	public void drawFPS(float fps) {
		// Set color to red and set font
		gc.setForeground(new Color(Display.getCurrent(), new RGB(200, 50, 0)));
		Font font = new Font(Display.getCurrent(), "Tahoma", 18, SWT.BOLD);
		gc.setFont(font);
		// There is no alignment, so we need to calculate the text width
		String text = "FPS: " + fps;
		int textWidth = gc.stringExtent(text).x;
		// And draw the text
		gc.drawText(text, units2pixel(GameConf.gridW) - textWidth - 10, units2pixel(GameConf.gridH) - 60, true);
	}

	public void setGC(GC gc) {
		this.gc = gc;
	}

}
