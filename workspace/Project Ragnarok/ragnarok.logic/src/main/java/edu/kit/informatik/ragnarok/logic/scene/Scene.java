package edu.kit.informatik.ragnarok.logic.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.CameraTarget;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.GuiElement;
import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

/**
 * Based on the concept of scenes in Unity. </br>
 * "Scenes contain the objects of your game. They can be used to create a main
 * menu, individual levels, and anything else. Think of each unique Scene file
 * as a unique level. In each Scene, you will place your environments,
 * obstacles, and decorations, essentially designing and building your game in
 * pieces." <a href=" https://docs.unity3d.com/Manual/CreatingScenes.html">Unity
 * Manual</a>
 * </p>
 * A new Scene needs an entry in {@link Scenes} and a method with the Signature:
 * {@code public static Scene create(GameModel, String[])}, for the GameModel to
 * be able to start that Scene.</br>
 * For Scene switching take a look at
 * {@link GameModel#switchScene(Scenes, String[])} <br>
 * <br>
 * <b>IMPORTANT:</b> all {@link Scene Scenes} must provide a static method <br>
 * <b><em>public static Scene create(GameModel model, String[]
 * options)</em></b><br>
 * so that {@link Scenes#getNewScene(GameModel, String[])} can work
 *
 * @author Matthias Schmitt
 *
 * @version 1.0
 */
abstract class Scene implements CameraTarget, IScene {

	/**
	 * Synchronization Object that is used as a lock variable for blocking
	 * operations
	 */
	private final Object sync = new Object();

	private GameModel model;

	private PriorityQueue<GuiElement> guiElements;

	private PriorityQueue<GameElement> gameElements;

	private ArrayList<GameElement> gameElementAddQueue;

	private ArrayList<GameElement> gameElementRemoveQueue;

	private Map<Class<?>, Long> gameElementDurations = new HashMap<>();

	private boolean paused = false;

	public Scene(GameModel model) {
		this.model = model;
	}

	@Override
	public void init() {
		this.guiElements = new PriorityQueue<>();
		this.gameElements = new PriorityQueue<>();
		this.gameElementAddQueue = new ArrayList<>();
		this.gameElementRemoveQueue = new ArrayList<>();
	}

	@Override
	public void togglePause() {
		this.paused = !this.paused;
	}

	public boolean isPaused() {
		return this.paused;
	}

	@Override
	public void end(boolean won) {
		this.model.switchScene(Scenes.MENU);
	}

	@Override
	public void logicLoop(float timeDelta) {

		this.logicLoopPre(timeDelta);

		// add GameElements that have been added
		this.addGameElements();

		if (!this.paused) {
			// iterate all GameElements to invoke logicLoop
			synchronized (this.synchronize()) {
				Iterator<GameElement> it = this.getGameElementIterator();

				while (it.hasNext()) {
					this.logicLoopGameElement(timeDelta, it);
				}
			}
		}

		// remove GameElements that must be removed
		this.removeGameElements();

		this.logicLoopAfter();

		// after all game related logic update GuiElements
		synchronized (this.synchronize()) {
			Iterator<GuiElement> it = this.getGuiElementIterator();
			while (it.hasNext()) {
				GuiElement e = it.next();
				e.logicLoop(timeDelta);
			}
		}

	}

	protected void logicLoopAfter() {
	}

	protected void logicLoopPre(float timeDelta) {
	}

	protected void logicLoopGameElement(float timeDelta, Iterator<GameElement> it) {
		GameElement e = it.next();

		// if this GameElement is marked for destruction
		// TODO This is a bugfix for inanimates which wont be deleted upon time
		if (e.getDeleteMe() || (e.getTeam() == Team.INANIMATE && this.getModel().getCameraOffset() - 20 > e.getPos().getX())) {
			it.remove();
		}

		// Debug: Save time before logicLoop
		long timeBefore = 0;
		if (GameConf.DEBUG) {
			timeBefore = System.currentTimeMillis();
		}

		e.logicLoop(timeDelta);

		// Debug: Compare and save logicLoop Duration
		if (GameConf.DEBUG) {
			long timeAfter = System.currentTimeMillis();
			Class<?> clazz = e.getClass();
			long dur = (timeAfter - timeBefore);
			if (this.gameElementDurations.containsKey(clazz)) {
				long newTime = this.gameElementDurations.get(clazz) + dur;
				this.gameElementDurations.put(clazz, newTime);
			} else {
				this.gameElementDurations.put(clazz, dur);
			}
		}
	}

	/**
	 * Adds a GameElement to the Model. The elements will not directly be added
	 * to the internal data structure to prevent concurrency errors. Instead
	 * there is an internal list to hold all waiting GameElements that will be
	 * added in the next call of logicLoop
	 *
	 * @param element
	 *            the GameElement to add
	 */
	@Override
	public void addGameElement(GameElement element) {
		// Put GameElement in waiting list
		synchronized (this.synchronize()) {
			this.gameElementAddQueue.add(element);
		}
	}

	/**
	 * Internal method to add all waiting GameElements. See addGameElement for
	 * more info.
	 */
	private void addGameElements() {
		synchronized (this.synchronize()) {
			Iterator<GameElement> it = this.gameElementAddQueue.iterator();
			while (it.hasNext()) {
				GameElement element = it.next();
				this.gameElements.add(element);
				element.setScene(this);

				it.remove();
			}
		}
	}

	/**
	 * Removes a GameElement from the Model The elements will not directly be
	 * removed from the internal data structure to prevent concurrency errors.
	 * Instead there is an internal list to hold all waiting GameElements that
	 * will be removed in the next call of logicLoop
	 *
	 * @param element
	 *            the GameElement to remove
	 */
	@Override
	public void removeGameElement(GameElement element) {
		synchronized (this.synchronize()) {
			this.gameElementRemoveQueue.add(element);
		}
	}

	/**
	 * Internal method to remove all waiting GameElements. See removeGameElement
	 * for more info.
	 */
	private void removeGameElements() {
		synchronized (this.synchronize()) {
			Iterator<GameElement> it = this.gameElementRemoveQueue.iterator();
			while (it.hasNext()) {
				GameElement element = it.next();
				it.remove();
				this.gameElements.remove(element);
			}
		}
	}

	@Override
	public Iterator<GameElement> getOrderedGameElementIterator() {
		return new PriorityQueueIterator<>(this.gameElements);
	}

	@Override
	public Iterator<GameElement> getGameElementIterator() {
		return this.gameElements.iterator();
	}

	/**
	 * Adds a GuiElement to the GameModel.
	 *
	 * @param e
	 *            the GuiElement to add
	 */
	@Override
	public void addGuiElement(GuiElement e) {
		this.guiElements.add(e);
	}

	@Override
	public void removeGuiElement(GuiElement e) {
		this.guiElements.remove(e);
	}

	@Override
	public Iterator<GuiElement> getGuiElementIterator() {
		return this.guiElements.iterator();
	}

	@Override
	public float getCameraOffset() {
		return 0;
	}

	@Override
	public void setCameraTarget(CameraTarget cameraTarget) {

	}

	@Override
	public int getGameElementCount() {
		return this.gameElements.size();
	}

	@Override
	public Object synchronize() {
		return this.sync;
	}

	@Override
	public Entity getPlayer() {
		return null;
	}

	@Override
	public long getTime() {
		return 0;
	}

	@Override
	public Map<Class<?>, Long> getGameElementDurations() {
		// Reset debug info
		Map<Class<?>, Long> ret = this.gameElementDurations;
		this.gameElementDurations = new HashMap<>();
		return ret;
	}

	@Override
	public final GameModel getModel() {
		return this.model;
	}
}
