package edu.kit.informatik.ragnarok.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.EntityFactory;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.GuiElement;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.LifeGui;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.ScoreGui;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.Text;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.TimeDecorator;
import edu.kit.informatik.ragnarok.logic.levelcreator.InfiniteLevelCreator;
import edu.kit.informatik.ragnarok.logic.levelcreator.LevelCreator;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.CalcUtil;
import edu.kit.informatik.ragnarok.util.TextOptions;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 * Main class of the Model. Manages the logic
 *
 * @author Angelo Aracri
 *
 * @version 1.1
 */
class GameModelImpl implements CameraTarget, GameModel {

	/**
	 * Synchronization Object that is used as a lock variable for blocking
	 * operations
	 */
	private final Object sync = new Object();

	private PriorityQueue<GuiElement> guiElements;

	private PriorityQueue<GameElement> gameElements;

	/**
	 * GameElements that are waiting to be added to the actual gameElements
	 */
	private List<GameElement> waitingGameElements;

	private List<GameElement> gameElementsToDelete;

	private Player player = new Player(new Vec2D(3, 5));

	public long lastTime;

	private LevelCreator levelCreator;

	private float currentOffset;

	private Thread loopThread;

	private CameraTarget cameraTarget;

	private ScoreGui scoreGui;

	private LifeGui lifeGui;

	private int highScore = -1;

	private GuiElement bossTextGui;

	@Override
	public void setCameraTarget(CameraTarget cameraTarget) {
		this.cameraTarget = cameraTarget;
	}

	public GameModelImpl() {
		this.init();
	}

	public void init() {
		// Initialize Set of all gameElements that need rendering and logic
		this.guiElements = new PriorityQueue<>();
		this.gameElements = new PriorityQueue<GameElement>();
		this.waitingGameElements = new ArrayList<GameElement>();
		this.gameElementsToDelete = new ArrayList<GameElement>();

		// Create Player and add him to game
		this.player.init();
		this.cameraTarget = this.player;
		this.addGameElement(this.player);

		// Create Gui
		this.scoreGui = new ScoreGui(this);
		this.scoreGui.setPos(new Vec2D(10, 10));
		this.lifeGui = new LifeGui(this);
		this.lifeGui.setPos(new Vec2D(10));
		this.addGuiElement(this.scoreGui);
		this.addGuiElement(this.lifeGui);

		// Init EnemyFactory with model
		EntityFactory.init(this);

		// Create LevelCreator with random seed
		this.levelCreator = new InfiniteLevelCreator(this, new Random().nextInt());
		this.levelCreator.generate(GameConf.GRID_W);

		// Initialize all other attributes
		this.lastTime = System.currentTimeMillis();

	}

	public void restart() {
		Thread restartThread = new Thread() {
			@Override
			public void run() {
				// wait 2 seconds
				ThreadUtils.sleep(2000);
				// reset all data structures
				GameModelImpl.this.init();
				// restart logic thread
				GameModelImpl.this.start();
			}
		};
		restartThread.start();
	}

	private void end() {

		// save score if higher than highscore
		if (this.getScore() > this.getHighScore()) {
			this.setHighScore(this.getScore());
		}

		// restart game
		GameModelImpl.this.restart();
	}

	@Override
	public int getHighScore() {
		if (this.highScore != -1) {
			return this.highScore;
		}

		int highScore = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader("score.dat"));
			String input = in.readLine();
			highScore = Integer.parseInt(input);
			in.close();
		} catch (IOException e) {
			highScore = 0;
		}

		return highScore;
	}

	public int setHighScore(int highScore) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("score.dat"));
			out.write(Integer.toString(highScore));
			out.close();
			this.highScore = highScore;
		} catch (IOException e) {
			System.err.println("Cannot write highscore: " + e.getMessage());
		}

		return highScore;
	}

	@Override
	public void start() {
		this.loopThread = new Thread() {
			@Override
			public void run() {
				// repeat until player is dead
				while (!GameModelImpl.this.getPlayer().deleteMe) {
					GameModelImpl.this.logicLoop();
					ThreadUtils.sleep(GameConf.LOGIC_DELTA);

				}
				GameModelImpl.this.end();
			}
		};
		this.loopThread.setDaemon(true);
		this.loopThread.start();
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
		synchronized (this.sync) {
			this.waitingGameElements.add(element);
		}
	}

	/**
	 * Internal method to add all waiting GameElements. See addGameElement for
	 * more info.
	 */
	private void addAllWaitingGameElements() {
		synchronized (this.sync) {
			Iterator<GameElement> it = this.waitingGameElements.iterator();
			while (it.hasNext()) {
				GameElement element = it.next();
				this.gameElements.add(element);
				element.setGameModel(this);
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
		synchronized (this.sync) {
			this.gameElementsToDelete.add(element);
		}
	}

	/**
	 * Internal method to remove all waiting GameElements. See removeGameElement
	 * for more info.
	 */
	private void removeAllWaitingGameElements() {
		synchronized (this.sync) {
			Iterator<GameElement> it = this.gameElementsToDelete.iterator();
			while (it.hasNext()) {
				GameElement element = it.next();
				it.remove();
				this.gameElements.remove(element);
			}
		}
	}

	@Override
	public Iterator<GameElement> getOrderedGameElementIterator() {
		// return this.gameElements.iterator();
		synchronized (this.sync) {
			return new PriorityQueueIterator<GameElement>(this.gameElements);
		}
	}

	/**
	 * Supplies an Iterator for all saved GuiElements
	 *
	 * @return
	 */
	@Override
	public Iterator<GuiElement> getGuiElementIterator() {
		return this.guiElements.iterator();
	}

	/**
	 * Calculate DeltaTime Get Collisions .. & Invoke ReactCollision Iterate
	 * over Elements --> invoke GameElement:logicLoop()
	 *
	 */
	public void logicLoop() {

		// calculate time difference since last physics loop
		long timeNow = System.currentTimeMillis();
		long timeDelta = timeNow - this.lastTime;

		// add GameElements that have been added
		this.addAllWaitingGameElements();

		this.levelCreator.generate((int) (this.getCameraOffset() + GameConf.GRID_W + 1));

		// dont allow player to go behind currentOffset
		float minX = this.getCameraOffset() + this.player.getSize().getX() / 2f;
		if (this.player.getPos().getX() < minX) {
			this.player.setPos(this.player.getPos().setX(minX));
		}

		// iterate all GameElements to invoke logicLoop
		synchronized (this.sync) {
			Iterator<GameElement> it = this.gameElements.iterator();
			while (it.hasNext()) {
				GameElement e = it.next();

				// if this GameElement is marked for destruction
				if (e.deleteMe) {
					it.remove();
				}

				// check if we can delete this
				if (e.getPos().getX() < this.currentOffset - GameConf.PLAYER_CAMERA_OFFSET - 1) {
					this.removeGameElement(e);
				} else {
					e.logicLoop(timeDelta / 1000.f);
				}
			}
		}

		// remove GameElements that must be removed
		this.removeAllWaitingGameElements();

		synchronized (this.sync) {
			// iterate all GameElements to detect collision
			Iterator<GameElement> it1 = this.gameElements.iterator();
			while (it1.hasNext()) {
				GameElement e1 = it1.next();
				Iterator<GameElement> it2 = this.gameElements.iterator();
				while (it2.hasNext()) {
					GameElement e2 = it2.next();
					if (e1 != e2) {
						this.checkCollision(e1, e2, e1.getLastPos(), e2.getLastPos());
					}
				}
			}
		}

		// after all game related logic update GuiElements
		synchronized (this.sync) {
			Iterator<GuiElement> it = this.getGuiElementIterator();
			while (it.hasNext()) {
				GuiElement e = it.next();
				e.logicLoop(timeDelta / 1000.f);
			}
		}

		// update time
		this.lastTime = timeNow;

	}

	private void checkCollision(GameElement e1, GameElement e2, Vec2D e1lastPos, Vec2D e2lastPos) {
		// Return if there is no collision
		if (!e1.getCollisionFrame().collidesWith(e2.getCollisionFrame())) {
			return;
		}

		// Simulate CollisionFrame with last Y position
		Vec2D e1lastYVec = new Vec2D(e1.getPos().getX(), e1lastPos.getY());
		Frame e1lastYFrame = new Frame(e1lastYVec.add(e1.getSize().multiply(-0.5f)), e1lastYVec.add(e1.getSize().multiply(0.5f)));

		// Simulate CollisionFrame with last X position
		Vec2D e1lastXVec = new Vec2D(e1lastPos.getX(), e1.getPos().getY());
		Frame e1lastXFrame = new Frame(e1lastXVec.add(e1.getSize().multiply(-0.5f)), e1lastXVec.add(e1.getSize().multiply(0.5f)));

		// Simulate CollisionFrame with last Y position
		Vec2D e2lastYVec = new Vec2D(e2.getPos().getX(), e2lastPos.getY());
		Frame e2lastYFrame = new Frame(e2lastYVec.add(e2.getSize().multiply(-0.5f)), e2lastYVec.add(e2.getSize().multiply(0.5f)));

		// Simulate CollisionFrame with last X position
		Vec2D e2lastXVec = new Vec2D(e2lastPos.getX(), e2.getPos().getY());
		Frame e2lastXFrame = new Frame(e2lastXVec.add(e2.getSize().multiply(-0.5f)), e2lastXVec.add(e2.getSize().multiply(0.5f)));

		// If they still collide with the old x positions:
		// it must be because of the y position
		if (e1lastXFrame.collidesWith(e2lastXFrame)) {
			// If relative movement is in positive y direction (down)
			float relMovement = (e2.getPos().getY() - e2lastPos.getY()) - (e1.getPos().getY() - e1lastPos.getY());
			if (relMovement > 0) {
				e1.reactToCollision(e2, Direction.UP);
			} else
			// If relative movement is in negative y direction (up)
			if (relMovement < 0) {
				e1.reactToCollision(e2, Direction.DOWN);
			} else {
				return;
			}
			// check if he is still colliding even with last x position
			this.checkCollision(e1, e2, new Vec2D(e1lastPos.getX(), e1.getPos().getY()), new Vec2D(e2lastPos.getX(), e2.getPos().getY()));
		} else
		// If they still collide with the old y positions:
		// it must be because of the x position
		if (e1lastYFrame.collidesWith(e2lastYFrame)) {
			// If he moved in positive x direction (right)
			float relMovement = (e2.getPos().getX() - e2lastPos.getX()) - (e1.getPos().getX() - e1lastPos.getX());
			if (relMovement > 0) {
				e1.reactToCollision(e2, Direction.LEFT);
			} else
			// If he moved in negative x direction (left)
			if (relMovement < 0) {
				e1.reactToCollision(e2, Direction.RIGHT);
			} else {
				return;
			}
			// check if he is still colliding even with last x position
			this.checkCollision(e1, e2, new Vec2D(e1.getPos().getX(), e1lastPos.getY()), new Vec2D(e2.getPos().getX(), e2lastPos.getY()));
		}

	}

	/**
	 * Return player
	 *
	 * @return the player
	 */
	@Override
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public int getScore() {
		return (int) (this.player.getCameraOffset() + this.getPlayer().getPoints());
	}

	@Override
	public float getCameraOffset() {
		return this.cameraTarget.getCameraOffset();
	}

	/**
	 * Adds a GuiElement to the GameModelImpl.
	 *
	 * @param e
	 *            the GuiElement to add
	 */
	public void addGuiElement(GuiElement e) {
		this.guiElements.add(e);
	}

	@Override
	public void removeGuiElement(GuiElement e) {
		this.guiElements.remove(e);
	}

	@Override
	public void addBossText(String text) {
		this.guiElements.remove(this.bossTextGui);

		TextOptions op = new TextOptions(new Vec2D(-0.5f, -0.5f), 30, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
		Text bossText = new Text(this, op);
		bossText.setText(text);
		bossText.setPos(CalcUtil.units2vec(new Vec2D(GameConf.GRID_W / 2f, GameConf.GRID_H / 2f)));

		this.bossTextGui = new TimeDecorator(this, bossText, new TimeDependency(3));

		this.addGuiElement(this.bossTextGui);
	}

	@Override
	public Object synchronize() {
		return this.sync;
	}
}
