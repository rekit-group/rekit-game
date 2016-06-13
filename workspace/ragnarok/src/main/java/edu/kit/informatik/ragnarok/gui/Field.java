package edu.kit.informatik.ragnarok.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.Vec2D;

public class Field {

	private GC gc;
	private GameView view;

	public Field(GC gc, GameView view) {
		this.gc = gc;
		this.view = view;
	}

	private int units2pixel(float units) {
		return (int) (units * c.pxPerUnit);
	}

	private int currentOffset() {
		return -units2pixel(this.view.getModel().getCurrentOffset()
				- c.playerDist);
	}

	public void setBackground(RGB col) {
		gc.setBackground(new Color(gc.getDevice(), col));
		gc.fillRectangle(0, 0, units2pixel(c.gridW), units2pixel(c.gridH));
	}

	public void drawCircle(Vec2D pos, Vec2D size, RGB col) {
		gc.setBackground(new Color(gc.getDevice(), col));
		gc.fillOval(currentOffset()
				+ units2pixel((pos.getX() - size.getX() / 2f)),
				units2pixel((pos.getY() - size.getY() / 2f)),
				units2pixel(size.getX()), units2pixel(size.getY()));
	}

	public void drawRectangle(Vec2D pos, Vec2D size, RGB col) {
		gc.setBackground(new Color(gc.getDevice(), col));
		gc.fillRectangle(currentOffset() + units2pixel(pos.getX() - size.getX() / 2f),
				units2pixel(pos.getY() - size.getY() / 2f), units2pixel(size.getX()),
				units2pixel(size.getY()));
	}
	
	public void drawPolygon(Vec2D start, Vec2D[] relDirs, RGB col) {
		
		// prepare actual array {x1, y1, x2, y2, ...}
		int[] actualArray = new int[2 + relDirs.length * 2];
		
		// save first (absolute) point x1, y1
		actualArray[0] = currentOffset() + units2pixel(start.getX());
		actualArray[1] = units2pixel(start.getY());
		
		// calculate rest of absolute points from relative points to start
		for (int i = 0; i < relDirs.length; i++) {
			actualArray[2*i + 2] = currentOffset() + units2pixel(start.getX() + relDirs[i].getX());
			actualArray[2*i + 3] = units2pixel(start.getY() + relDirs[i].getY());
		}
		
		// set color
		gc.setBackground(new Color(gc.getDevice(), col));
		
		// draw actual polygon
		gc.fillPolygon(actualArray);
	}

	public void drawImage(Vec2D pos, Vec2D size, String imagePath) {
		Image image = new Image(this.gc.getDevice(), imagePath);
		gc.drawImage(image,
				currentOffset() + units2pixel(pos.getX() - size.getX() / 2f), // dstX
				units2pixel(pos.getY() - size.getY() / 2f) // dstY
		);
	}

	public void refreshUI(int lifes, int points) {

		// Iterate lifes
		Image image = new Image(this.gc.getDevice(), "resources/mrRekt_glasses_right.png");
		for (int i = 0; i < lifes; i++) {
			gc.drawImage(image, 10 + 50 * i, 10);
		}

		// Set color to red and set font
		gc.setForeground(new Color(gc.getDevice(), new RGB(200, 50, 0)));
		Font font = new Font(gc.getDevice(), "Tahoma", 18, SWT.BOLD);
		gc.setFont(font);
		// There is no alignment, so we need to calculate the text width
		String text = points + " Points";
		int textWidth = gc.stringExtent(text).x;
		// And draw the text
		gc.drawText(text, units2pixel(c.gridW) - textWidth - 10, 10, true);
	}

}
