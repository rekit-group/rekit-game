package edu.kit.informatik.ragnarok.gui;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.GuiElement;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.CalcUtil;
import edu.kit.informatik.ragnarok.util.InputHelper;
import edu.kit.informatik.ragnarok.util.SwtUtils;

/**
 * Main class of the View. Manages the window and a canvas an periodically
 * renders all GameElements that GameModel.getGameElementIterator() provides
 *
 * @author Angelo Aracri
 * @author Dominik Fuchß
 *
 * @version 1.1
 */
class GameView implements View {
	/**
	 * Reference to the model, that holds all information that are required for
	 * rendering
	 */
	private Model model;

	/**
	 * Represents a graphic window
	 */
	private Shell shell;

	private long lastRenderTime;

	private Queue<Float> fpsQueue = new ArrayDeque<Float>();

	/**
	 * The canvas that is drawn upon
	 */
	private Canvas canvas;

	/**
	 * The Field that manages the graphic context
	 */
	private FieldImpl field;

	private GC gc;

	/**
	 * Constructor that creates a new window with a canvas and prepares all
	 * required attributes
	 *
	 * @param model
	 *            the model
	 */
	public GameView(Model model) {
		this.model = model;
		// Create window

		this.shell = new Shell(Display.getDefault(), SWT.DIALOG_TRIM | SWT.MIN | SWT.PRIMARY_MODAL | SWT.NO_BACKGROUND);
		this.shell.setText(GameConf.NAME);

		// Create and position a canvas
		this.canvas = new Canvas(this.shell, SWT.NONE);
		this.canvas.setSize(GameConf.GRID_W * GameConf.PX_PER_UNIT, GameConf.GRID_H * GameConf.PX_PER_UNIT);
		this.canvas.setLocation(0, 0);

		// Open Shell
		this.shell.setSize(GameConf.GRID_W * GameConf.PX_PER_UNIT, GameConf.GRID_H * GameConf.PX_PER_UNIT);
		this.shell.setLocation(SwtUtils.calcCenter(this.shell));
		this.shell.open();

		// Create Graphic context
		this.gc = new GC(this.canvas);
		this.field = new FieldImpl();
	}

	/**
	 * Starts the View by periodically invoking renderLoop()
	 */
	@Override
	public void start() {
		Thread updateThread = new Thread() {

			@Override
			public void run() {
				Display disp = Display.getDefault();
				while (true && !disp.isDisposed()) {
					disp.syncExec(new Runnable() {
						@Override
						public void run() {
							GameView.this.renderLoop();
						}
					});
					// ThreadUtils.sleep(GameConf.RENDER_DELTA);
					Thread.yield();
				}
			}
		};
		// background thread
		updateThread.setDaemon(true);
		updateThread.start();

		// Main SWT stuff ...
		Display display = Display.getDefault();
		// Wait for window to be closed, holds the window open
		while (!this.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * Getter for the GameModel
	 *
	 * @return the reference to the GameModel
	 */
	public Model getModel() {
		return this.model;
	}

	/**
	 * Games main render loop that is periodically called. It updates the canvas
	 * by iterating over all GameElements that GameMode.getGameElementIterator()
	 * supplies and invoking each render()
	 */
	public void renderLoop() {
		if (this.shell.isDisposed()) {
			return;
		}

		Scene scene = this.model.getScene();

		// Create temporary GC on new Image and let field draw on that
		// Double buffering reduces flickering
		Image image = new Image(this.shell.getDisplay(), this.canvas.getBounds());
		GC tempGC = new GC(image);
		this.field.setGC(tempGC);

		// set current camera position
		this.field.setCurrentOffset(scene.getCameraOffset());
		this.field.setBackground(SwtUtils.calcRGB(GameConf.GAME_BACKGROUD_COLOR));

		synchronized (scene.synchronize()) {
			// Get a z-index-ordered iterator
			Iterator<GameElement> it1 = scene.getOrderedGameElementIterator();
			// Iterate all GameElements
			while (it1.hasNext()) {
				// Render next element
				GameElement e = it1.next();
				if (e.isVisible()) {
					e.render(this.field);
				}
			}

			// Iterate all GuiElements (life, score, boss)
			Iterator<GuiElement> it2 = scene.getGuiElementIterator();
			while (it2.hasNext()) {
				// Render next element
				GuiElement e = it2.next();
				if (e.isVisible()) {
					e.render(this.field);
				}
			}
		}

		// draw temporary image on actual cavans
		this.gc.drawImage(image, 0, 0);

		// put trash outside
		image.dispose();
		tempGC.dispose();

		// draw FPS
		float fps = this.getFPS();
		this.field.setGC(this.gc);
		this.field.drawText(new Vec(CalcUtil.units2pixel(GameConf.GRID_W) - 10, CalcUtil.units2pixel(GameConf.GRID_H) - 60), "FPS: " + fps
				+ "\nGameElements: " + this.model.getScene().getGameElementCount(), GameConf.HINT_TEXT);

	}

	private float getFPS() {
		long thisTime = System.currentTimeMillis();
		long deltaTime = thisTime - this.lastRenderTime;
		this.lastRenderTime = System.currentTimeMillis();

		float fps = deltaTime;
		this.fpsQueue.add(fps);
		float sum = 0;
		for (float f : this.fpsQueue) {
			sum += f;
		}

		if (this.fpsQueue.size() > 5) {
			this.fpsQueue.remove();
		}

		return (int) (10000f / (sum / this.fpsQueue.size()) / 10f);
	}

	@Override
	public void attachMe(InputHelper inputHelper) {
		// Check if listenerControl is set
		if (inputHelper == null) {
			return;
		}
		// Add our custom KeyListener to an object
		KeyAdapter adapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				inputHelper.press(e.keyCode);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				inputHelper.release(e.keyCode);
			}
		};
		this.canvas.addKeyListener(adapter);

	}

}
