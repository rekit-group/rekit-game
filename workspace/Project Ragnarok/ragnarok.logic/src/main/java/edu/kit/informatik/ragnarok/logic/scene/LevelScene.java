/**
 * 
 */
package edu.kit.informatik.ragnarok.logic.scene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.EntityFactory;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.LifeGui;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.ScoreGui;
import edu.kit.informatik.ragnarok.logic.levelcreator.LevelCreator;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 * Scene that holds a playable Level created by a LevelCreator.
 * Different Levels are possible by changing the LevelCreator in the costructor.
 * @author matze
 *
 */
public class LevelScene extends Scene {
	
	private Player player = new Player(new Vec2D(3, 5));
	
	private LevelCreator levelCreator;
	
	private CameraTarget cameraTarget;
	
	private float currentOffset;
	
	private int highScore = -1;
	
	private ScoreGui scoreGui;
	
	private LifeGui lifeGui;

	public LevelScene(GameModel model, LevelCreator levelCreator) {
		super(model);
		
		this.levelCreator = levelCreator;
	}

	@Override
	public void init() {
		super.init();
		
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

		this.levelCreator.init(this);
		this.levelCreator.generate(GameConf.GRID_W);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void end() {
		// save score if higher than highscore
		if (this.getScore() > this.getHighScore()) {
			this.setHighScore(this.getScore());
		}

		// restart game
		this.restart();
	}

	@Override
	public void restart() {
		Thread restartThread = new Thread() {
			@Override
			public void run() {
				// wait 2 seconds
				ThreadUtils.sleep(2000);
				// reset all data structures
				LevelScene.this.init();
				// restart logic thread
				LevelScene.this.start();
			}
		};
		restartThread.start();
	}
	
	@Override
	protected void logicLoopPre(float timeDelta) {
		
		this.levelCreator.generate((int) (this.getCameraOffset() + GameConf.GRID_W + 1));
		
		// dont allow player to go behind currentOffset
		float minX = this.getCameraOffset() + this.player.getSize().getX() / 2f;
		if (this.player.getPos().getX() < minX) {
			this.player.setPos(this.player.getPos().setX(minX));
		}

	}
	
	@Override
	protected void logicLoopGameElement(float timeDelta, Iterator<GameElement> it) {
		GameElement e = it.next();

		// if this GameElement is marked for destruction
		if (e.deleteMe) {
			it.remove();
		}

		// check if we can delete this
		if (e.getPos().getX() < this.currentOffset - GameConf.PLAYER_CAMERA_OFFSET - 1) {
			this.removeGameElement(e);
		} else {
			e.logicLoop(timeDelta);
		}
	}
	
	@Override
	protected void logicLoopAfter() {
		synchronized (GameModel.SYNC) {
			// iterate all GameElements to detect collision
			Iterator<GameElement> it1 = this.getOrderedGameElementIterator();
			while (it1.hasNext()) {
				GameElement e1 = it1.next();
				Iterator<GameElement> it2 = this.getOrderedGameElementIterator();
				while (it2.hasNext()) {
					GameElement e2 = it2.next();
					if (e1 != e2) {
						this.checkCollision(e1, e2, e1.getLastPos(), e2.getLastPos());
					}
				}
			}
		}
		
		if (this.player.deleteMe) {
			this.end();
		}
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
	
	@Override
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public float getCameraOffset() {
		return 0;
	}
	
	public void setCameraTarget(CameraTarget cameraTarget) {
		this.cameraTarget = cameraTarget;
	}

	public int getScore() {
		return (int) (this.player.getCameraOffset() + this.getPlayer().getPoints());
	}
	
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

}
