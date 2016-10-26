package edu.kit.informatik.ragnarok.gui;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.GameTime;
import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.logic.Model;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.AbstractImage;
import edu.kit.informatik.ragnarok.primitives.image.Filter;
import edu.kit.informatik.ragnarok.util.InputHelper;
import edu.kit.informatik.ragnarok.util.SwtUtils;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 * Main class of the View. Manages the window and a canvas an periodically
 * renders all GameElements that GameModel.getGameElementIterator() provides
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 *
 * @version 1.1
 */
class GameView implements View {

	/**
	 * The location of the icon of the game.
	 */
	private static final String ICON_LOCATION = "/images/icon.png";

	/**
	 * Reference to the model, that holds all information that are required for
	 * rendering.
	 */
	private Model model;

	/**
	 * Represents a graphic window.
	 */
	private Shell shell;
	/**
	 * The last render time.
	 */
	private long lastRenderTime;
	/**
	 * The queue used for fps calculation.
	 */
	private Queue<Float> fpsQueue = new ArrayDeque<>();

	/**
	 * The canvas that is drawn upon.
	 */
	private Canvas canvas;

	/**
	 * The Field that manages the graphic context.
	 */
	private FieldImpl field;
	/**
	 * The GC of the {@link Canvas}.
	 */
	private GC gc;
	/**
	 * The filter from the model.
	 */
	private Filter filter;

	/**
	 * Constructor that creates a new window with a canvas and prepares all
	 * required attributes.
	 *
	 * @param model
	 *            the model
	 */
	public GameView(Model model) {
		this.model = model;
		// Create window

		this.shell = new Shell(Display.getDefault(), SWT.DIALOG_TRIM | SWT.MIN | SWT.PRIMARY_MODAL | SWT.NO_BACKGROUND);
		this.shell.setText(GameConf.NAME + " (" + GameConf.VERSION + ")");
		this.shell.setImage(new Image(Display.getDefault(), this.getClass().getResourceAsStream(GameView.ICON_LOCATION)));
		// Create and position a canvas
		this.canvas = new Canvas(this.shell, SWT.NONE);
		this.canvas.setSize(GameConf.PIXEL_W, GameConf.PIXEL_H);
		this.canvas.setLocation(0, 0);

		// TODO This depends on the window manager. This issue should be solved
		// Open Shell (5,28) seems to be the additional size of my window
		// decoration (boder, title, close button etc)
		this.shell.setSize(GameConf.PIXEL_W + 5, GameConf.PIXEL_H + 28);
		this.shell.setLocation(SwtUtils.calcCenter(this.shell));
		this.shell.open();

		// Create Graphic context
		this.gc = new GC(this.canvas);
		this.field = new FieldImpl();
	}

	/**
	 * Starts the View by periodically invoking renderLoop().
	 */
	@Override
	public void start() {
		// background thread
		ThreadUtils.runDaemon(() -> this.update());
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
	 * "Update-Thread" content.
	 */
	private void update() {
		Display disp = Display.getDefault();
		while (!disp.isDisposed()) {
			disp.syncExec(() -> this.renderLoop());
			Thread.yield();
		}
	}

	/**
	 * Getter for the GameModel.
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

		IScene scene = this.model.getScene();

		// Create temporary GC on new Image and let field draw on that
		// Double buffering reduces flickering
		Image image = new Image(this.shell.getDisplay(), this.canvas.getBounds());
		GC tempGC = new GC(image);
		if (this.model.filterChanged()) {
			this.filter = this.model.getFilter();
			this.field.setFilter(this.filter);
		}
		this.field.setGC(tempGC);

		// set current camera position
		this.field.setCurrentOffset(scene.getCameraOffset());
		this.field.setBackground(GameConf.GAME_BACKGROUD_COLOR);

		synchronized (scene.synchronize()) {
			scene.getOrderedGameElementIterator().forEachRemaining((e) -> e.render(this.field));
			scene.getGuiElementIterator().forEachRemaining((e) -> e.render(this.field));
		}

		// draw temporary image on actual cavans
		if (this.filter == null || this.filter.isApplyPixel()) {
			this.gc.drawImage(image, 0, 0);
		} else {
			Rectangle bounds = image.getBounds();
			ImageData target = (ImageData) image.getImageData().clone();
			AbstractImage res = this.filter.apply(new AbstractImage(bounds.height, bounds.width, target.data));
			target.data = res.pixels;
			Image toDraw = new Image(this.shell.getDisplay(), target);
			this.gc.drawImage(toDraw, 0, 0);
			toDraw.dispose();
		}

		// put trash outside
		image.dispose();
		tempGC.dispose();

		// draw FPS
		this.field.setGC(this.gc);
		String debugInfo = "FPS: " + this.getFPS();
		this.field.drawText(new Vec(GameConf.PIXEL_W - 10, GameConf.PIXEL_H - 60), debugInfo, GameConf.HINT_TEXT, false);

		if (GameConf.DEBUG) {
			this.drawDebug();
		}

	}

	/**
	 * If {@link GameConf#DEBUG} is set this method will be used for drawing
	 * stats.
	 */
	private void drawDebug() {
		HashMap<Class<?>, Integer> classCounter = new HashMap<>();

		synchronized (this.model.getScene().synchronize()) {
			Iterator<GameElement> it = this.model.getScene().getGameElementIterator();
			while (it.hasNext()) {
				GameElement e = it.next();
				Class<?> className = e.getClass();
				if (classCounter.containsKey(className)) {
					classCounter.put(className, classCounter.get(className) + 1);
				} else {
					classCounter.put(className, 1);
				}
			}
		}

		StringBuilder resultName = new StringBuilder();
		StringBuilder resultNum = new StringBuilder();
		StringBuilder resultDur = new StringBuilder();

		resultName.append("GameElements");
		resultName.append("\n");
		resultNum.append(this.model.getScene().getGameElementCount());
		resultNum.append("\n");
		resultDur.append("\n");

		Map<Class<?>, Long> durations = this.model.getScene().getGameElementDurations();

		Iterator<Entry<Class<?>, Integer>> it2 = classCounter.entrySet().iterator();
		while (it2.hasNext()) {
			Entry<Class<?>, Integer> e = it2.next();
			resultName.append(e.getKey().getSimpleName());
			resultName.append("\n");
			resultNum.append(e.getValue());
			resultNum.append("\n");
			Long dur = durations.get(e.getKey());
			resultDur.append(dur == null ? "-" : dur);
			resultDur.append("\n");
		}
		this.field.drawText(new Vec(GameConf.PIXEL_W - 55, GameConf.PIXEL_H / 2f), resultName.toString(), GameConf.HINT_TEXT, false);
		this.field.drawText(new Vec(GameConf.PIXEL_W - 30, GameConf.PIXEL_H / 2f), resultNum.toString(), GameConf.HINT_TEXT, false);
		this.field.drawText(new Vec(GameConf.PIXEL_W - 5, GameConf.PIXEL_H / 2f), resultDur.toString(), GameConf.HINT_TEXT, false);
	}

	/**
	 * Get the latest FPS.
	 *
	 * @return the FPS
	 */
	private int getFPS() {
		long thisTime = GameTime.getTime();
		long deltaTime = thisTime - this.lastRenderTime;
		this.lastRenderTime = GameTime.getTime();

		float fps = deltaTime;
		this.fpsQueue.add(fps);
		float sum = 0;
		for (float f : this.fpsQueue) {
			sum += f;
		}

		if (this.fpsQueue.size() > 200) {
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
