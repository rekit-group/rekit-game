package edu.kit.informatik.ragnarok.gui;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import config.c;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;

public class GameView {
	/**
	 * Reference to the model, that holds all information that are required for
	 * rendering
	 */
	private GameModel model;

	/**
	 * Reference to the window, where the view renders everything we need
	 */
	private MainFrame window;

	private Shell shell;
	private Display display;

	private GC gc;

	public GameView() {

		// Create window
		this.display = new Display();
		this.shell = new Shell(display);
		this.shell.setText("Project Ragnarok");

		// Create and position a canvas
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setSize(c.gridW * c.pxPerUnit, c.gridH * c.pxPerUnit);
		canvas.setLocation(0, 0);

		// Open Shell
		shell.open();
		shell.setSize(c.gridW * c.pxPerUnit, c.gridH * c.pxPerUnit);

		// Create Graphic context
		this.gc = new GC(canvas);
		/*
		 * gc.drawRectangle(10, 10, 40, 45); gc.drawOval(65, 10, 30, 35);
		 * gc.drawLine(130, 10, 90, 80); gc.drawPolygon(new int[] { 20, 70, 45,
		 * 90, 70, 70 }); gc.drawPolyline(new int[] { 10, 120, 70, 100, 100,
		 * 130, 130, 75 }); gc.dispose();
		 */

	}

	public void start() {
		Thread updateThread = new Thread() {
			public void run() {
				while (true) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							renderLoop();
						}
					});

					try {
						Thread.sleep(c.renderDelta);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		// background thread
		updateThread.setDaemon(true);
		updateThread.start();

		// Wait for window to be closed
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public void setModel(GameModel value) {
		this.model = value;
	}

	public GameModel getModel() {
		return this.model;
	}

	public void renderLoop() {
		
		gc.setBackground(new Color(display, 100, 100, 255));
		gc.fillRectangle(0, 0, c.gridW * c.pxPerUnit, c.gridH * c.pxPerUnit);

		// iterate all GameElements
		Iterator<GameElement> it = this.model.getGameElementIterator();
		while (it.hasNext()) {
			GameElement e = it.next();
			e.render(this.gc);
		}
	}

}