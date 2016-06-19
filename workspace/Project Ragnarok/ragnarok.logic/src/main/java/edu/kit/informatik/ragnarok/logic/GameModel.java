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

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.levelcreator.LevelCreator;
import edu.kit.informatik.ragnarok.logic.levelcreator.InfiniteLevelCreator;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.EnemyFactory;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

public class GameModel implements CameraTarget {

	/**
	 * Synchronization Object that is used as a lock variable for blocking
	 * operations
	 */
	public static final Object SYNC = new Object();

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
	
	public void setCameraTarget(CameraTarget cameraTarget) {
		this.cameraTarget = cameraTarget;
	}

	public GameModel() {
		this.init();
	}

	public void init() {
		// Initialize Set of all gameElements that need rendering and logic
		this.gameElements = new PriorityQueue<GameElement>();
		this.waitingGameElements = new ArrayList<GameElement>();
		this.gameElementsToDelete = new ArrayList<GameElement>();

		// Create Player and add him to game
		this.player.init();
		this.cameraTarget = (CameraTarget) this.player;
		this.addGameElement(this.player);

		// Init EnemyFactory with model
		EnemyFactory.init(this);

		// Create LevelCreator
		this.levelCreator = new InfiniteLevelCreator(this);
		this.levelCreator.generate(GameConf.gridW);

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
				GameModel.this.init();
				// restart logic thread
				GameModel.this.start();
			}
		};
		restartThread.start();
	}
	
	private void end () {
		
		// save score if higher than highscore
		if (this.getScore() > this.getHighScore()) {
			this.setHighScore(this.getScore());
		}
		
		// restart game
		GameModel.this.restart();
	}
	
	private int highScore = -1;
	
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
			// TODO error
		}
		
		return highScore;
	}

	public void start() {
		this.loopThread = new Thread() {
			@Override
			public void run() {
				// repeat until player is dead
				while (!GameModel.this.getPlayer().deleteMe) {
					GameModel.this.logicLoop();
					ThreadUtils.sleep(GameConf.logicDelta);

				}
				GameModel.this.end();
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
	public void addGameElement(GameElement element) {
		// Put GameElement in waiting list
		synchronized (GameModel.SYNC) {
			this.waitingGameElements.add(element);
		}
	}

	/**
	 * Internal method to add all waiting GameElements. See addGameElement for
	 * more info.
	 */
	private void addAllWaitingGameElements() {
		synchronized (GameModel.SYNC) {
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
	public void removeGameElement(GameElement element) {
		synchronized (GameModel.SYNC) {
			this.gameElementsToDelete.add(element);
		}
	}

	/**
	 * Internal method to remove all waiting GameElements. See removeGameElement
	 * for more info.
	 */
	private void removeAllWaitingGameElements() {
		synchronized (GameModel.SYNC) {
			Iterator<GameElement> it = this.gameElementsToDelete.iterator();
			while (it.hasNext()) {
				GameElement element = it.next();
				it.remove();
				this.gameElements.remove(element);
			}
		}
	}

	/**
	 * Supplies an Iterator for all saved GameElements
	 *
	 * @return
	 */
	public Iterator<GameElement> getGameElementIterator() {
		return this.gameElements.iterator();
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

		this.levelCreator.generate((int)(this.getCameraOffset() + GameConf.gridW + 1));

		// dont allow player to go behind currentOffset
		float minX = this.getCameraOffset() + player.getSize().getX() / 2f;
		if (player.getPos().getX() < minX) {
			player.setPos(player.getPos().setX(minX));
		}

		// iterate all GameElements to invoke logicLoop
		synchronized (GameModel.SYNC) {
			Iterator<GameElement> it = this.getGameElementIterator();
			while (it.hasNext()) {
				GameElement e = it.next();

				// if this GameElement is marked for destruction
				if (e.deleteMe) {
					it.remove();
				}

				// check if we can delete this
				if (e.getPos().getX() < this.currentOffset
						- GameConf.playerCameraOffset - 1) {
					this.removeGameElement(e);
				} else {
					e.logicLoop(timeDelta / 1000.f);
				}
			}
		}

		// remove GameElements that must be removed
		this.removeAllWaitingGameElements();

		synchronized (GameModel.SYNC) {
			// iterate all GameElements to detect collision
			Iterator<GameElement> it1 = this.getGameElementIterator();
			while (it1.hasNext()) {
				GameElement e1 = it1.next();
				Iterator<GameElement> it2 = this.getGameElementIterator();
				while (it2.hasNext()) {
					GameElement e2 = it2.next();
					if (e1 != e2) {
						this.checkCollision(e1, e2, e1.getLastPos(),
								e2.getLastPos());
					}
				}
			}
		}

		// update time
		this.lastTime = timeNow;

	}

	private void checkCollision(GameElement e1, GameElement e2,
			Vec2D e1lastPos, Vec2D e2lastPos) {
		// Return if there is no collision
		if (!e1.getCollisionFrame().collidesWith(e2.getCollisionFrame())) {
			return;
		}

		// Simulate CollisionFrame with last Y position
		Vec2D e1lastYVec = new Vec2D(e1.getPos().getX(), e1lastPos.getY());
		Frame e1lastYFrame = new Frame(e1lastYVec.add(e1.getSize().multiply(
				-0.5f)), e1lastYVec.add(e1.getSize().multiply(0.5f)));

		// Simulate CollisionFrame with last X position
		Vec2D e1lastXVec = new Vec2D(e1lastPos.getX(), e1.getPos().getY());
		Frame e1lastXFrame = new Frame(e1lastXVec.add(e1.getSize().multiply(
				-0.5f)), e1lastXVec.add(e1.getSize().multiply(0.5f)));

		// Simulate CollisionFrame with last Y position
		Vec2D e2lastYVec = new Vec2D(e2.getPos().getX(), e2lastPos.getY());
		Frame e2lastYFrame = new Frame(e2lastYVec.add(e2.getSize().multiply(
				-0.5f)), e2lastYVec.add(e2.getSize().multiply(0.5f)));

		// Simulate CollisionFrame with last X position
		Vec2D e2lastXVec = new Vec2D(e2lastPos.getX(), e2.getPos().getY());
		Frame e2lastXFrame = new Frame(e2lastXVec.add(e2.getSize().multiply(
				-0.5f)), e2lastXVec.add(e2.getSize().multiply(0.5f)));

		// If they still collide with the old x positions:
		// it must be because of the y position
		if (e1lastXFrame.collidesWith(e2lastXFrame)) {
			// If relative movement is in positive y direction (down)
			float relMovement = (e2.getPos().getY() - e2lastPos.getY())
					- (e1.getPos().getY() - e1lastPos.getY());
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
			this.checkCollision(e1, e2, new Vec2D(e1lastPos.getX(), e1.getPos()
					.getY()), new Vec2D(e2lastPos.getX(), e2.getPos().getY()));
		} else
		// If they still collide with the old y positions:
		// it must be because of the x position
		if (e1lastYFrame.collidesWith(e2lastYFrame)) {
			// If he moved in positive x direction (right)
			float relMovement = (e2.getPos().getX() - e2lastPos.getX())
					- (e1.getPos().getX() - e1lastPos.getX());
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
			this.checkCollision(e1, e2,
					new Vec2D(e1.getPos().getX(), e1lastPos.getY()), new Vec2D(
							e2.getPos().getX(), e2lastPos.getY()));
		}

	}

	/**
	 * Return player
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return this.player;
	}

	public int getScore() {
		return (int) (this.player.getCameraOffset() + this.getPlayer().getPoints()
				- GameConf.playerCameraOffset);
	}

	@Override
	public float getCameraOffset() {
		return this.cameraTarget.getCameraOffset();
	}

}