package edu.kit.informatik.ragnarok.gui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
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
		return -units2pixel(this.view.getModel().getCurrentOffset() - c.playerDist);
	}
	
	public void setBackground(RGB col) {
		gc.setBackground(new Color(gc.getDevice(), col));
		gc.fillRectangle(0, 0, units2pixel(c.gridW), units2pixel(c.gridH));
	}
	
	public void drawCircle(Vec2D pos, Vec2D size, RGB col) {
		gc.setBackground(new Color(gc.getDevice(), col));
		gc.fillOval(
				currentOffset() + units2pixel((pos.getX() - 0.5f)),
				units2pixel((pos.getY() - 0.5f)),
				units2pixel(size.getX()),
				units2pixel(size.getY()));
	}

	public void drawRectangle(Vec2D pos, Vec2D size, RGB col) {
		gc.setBackground(new Color(gc.getDevice(), col));
		gc.fillRectangle(
				currentOffset() + units2pixel(pos.getX() - 0.5f + 0.01f),
				units2pixel(pos.getY() - 0.5f + 0.01f),
				units2pixel(size.getX() - 0.02f),
				units2pixel(size.getY() - 0.02f) 
			);
	}
	
}
