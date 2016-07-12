/**
 *
 */
package edu.kit.informatik.ragnarok.logic.scene;

import java.util.Iterator;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElementFactory;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.LifeGui;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.ScoreGui;
import edu.kit.informatik.ragnarok.logic.level.Level;
import edu.kit.informatik.ragnarok.logic.parallax.HeapElementCloud;
import edu.kit.informatik.ragnarok.logic.parallax.HeapElementMountain;
import edu.kit.informatik.ragnarok.logic.parallax.HeapLayer;
import edu.kit.informatik.ragnarok.logic.parallax.ParallaxContainer;
import edu.kit.informatik.ragnarok.logic.parallax.TriangulationLayer;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 * Scene that holds a playable Level created by a LevelCreator. Different Levels
 * are possible by changing the LevelCreator in the constructor.
 *
 * @author matze
 *
 */
public abstract class LevelScene extends Scene {

	private Player player = new Player(new Vec(3, 5));

	private Level level;

	private CameraTarget cameraTarget;

	private ScoreGui scoreGui;

	private LifeGui lifeGui;

	protected ParallaxContainer parallax;

	public LevelScene(GameModel model, Level level) {
		super(model);
		this.level = level;
	}

	@Override
	public void init() {
		super.init();

		// Create Player and add him to game
		this.player.init();
		this.cameraTarget = this.player;
		this.addGameElement(this.player);

		// Init EnemyFactory with model
		GameElementFactory.init(this);

		this.level.init();

		// Create parallax background
		this.parallax = new ParallaxContainer(this);

		this.parallax.addLayer(new TriangulationLayer(1.5f));
		this.parallax.addLayer(new HeapLayer(new HeapElementCloud(null, new Vec(), null, null), 1.1f));
		this.parallax.addLayer(new HeapLayer(new HeapElementMountain(null, new Vec(), null, null), 1.3f));

		// Create Gui
		this.scoreGui = new ScoreGui(this);
		this.scoreGui.setPos(new Vec(10, 10));
		this.lifeGui = new LifeGui(this);
		this.lifeGui.setPos(new Vec(10));
		this.addGuiElement(this.scoreGui);
		this.addGuiElement(this.lifeGui);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void end(boolean won) {
		// only save score if the level is infinite or the player has won
		// don't save it upon losing in finite level
		if (this.level.getLevelAssember().isInfinite() || won) {
			// save score if higher than highscore
			if (this.getScore() > this.getHighScore()) {
				this.setHighScore(this.getScore());
			}
		}

		// restart game
		this.restart();
	}

	@Override
	public void restart() {
		// wait 2 seconds
		ThreadUtils.sleep(2000);
		// reset all data structures
		this.init();
		// restart logic thread
		this.start();
	}

	@Override
	protected void logicLoopPre(float timeDelta) {

		this.level.getLevelAssember().generate((int) (this.getCameraOffset() + GameConf.GRID_W + 1));

		// dont allow player to go behind currentOffset
		float minX = this.getCameraOffset() + this.player.getSize().getX() / 2f;
		if (this.player.getPos().getX() < minX) {
			this.player.setPos(this.player.getPos().setX(minX));
		}

		this.parallax.logicLoop(this.getCameraOffset());

	}

	@Override
	protected void logicLoopGameElement(float timeDelta, Iterator<GameElement> it) {
		GameElement e = it.next();

		// if this GameElement is marked for destruction
		if (e.getDeleteMe()) {
			it.remove();
		}

		// check if we can delete this
		if (e.getPos().translate2D(this.getCameraOffset()).getX() + e.getSize().getX() / 2 < this.getCameraOffset()) {

			this.removeGameElement(e);
		} else {
			e.logicLoop(timeDelta);
		}
	}

	@Override
	protected void logicLoopAfter() {
		synchronized (this.synchronize()) {
			// iterate all GameElements to detect collision
			Iterator<GameElement> it1 = this.getGameElementIterator();
			while (it1.hasNext()) {
				GameElement e1 = it1.next();
				Iterator<GameElement> it2 = this.getGameElementIterator();
				while (it2.hasNext()) {
					GameElement e2 = it2.next();
					if (e1 != e2) {
						this.checkCollision(e1, e2, e1.getLastPos(), e2.getLastPos());
					}
				}
			}
		}

		if (this.player.getDeleteMe()) {
			this.end(false);
		}
	}

	private void checkCollision(GameElement e1, GameElement e2, Vec e1lastPos, Vec e2lastPos) {
		// Return if one of the elements is about to be deleted.
		if (e1.getDeleteMe() || e2.getDeleteMe()) {
			return;
		}
		
		// Return if there is no collision
		if (!e1.getCollisionFrame().collidesWith(e2.getCollisionFrame())) {
			return;
		}

		// Simulate CollisionFrame with last Y position
		Vec e1lastYVec = new Vec(e1.getPos().getX(), e1lastPos.getY());
		Frame e1lastYFrame = new Frame(e1lastYVec.add(e1.getSize().multiply(-0.5f)), e1lastYVec.add(e1.getSize().multiply(0.5f)));

		// Simulate CollisionFrame with last X position
		Vec e1lastXVec = new Vec(e1lastPos.getX(), e1.getPos().getY());
		Frame e1lastXFrame = new Frame(e1lastXVec.add(e1.getSize().multiply(-0.5f)), e1lastXVec.add(e1.getSize().multiply(0.5f)));

		// Simulate CollisionFrame with last Y position
		Vec e2lastYVec = new Vec(e2.getPos().getX(), e2lastPos.getY());
		Frame e2lastYFrame = new Frame(e2lastYVec.add(e2.getSize().multiply(-0.5f)), e2lastYVec.add(e2.getSize().multiply(0.5f)));

		// Simulate CollisionFrame with last X position
		Vec e2lastXVec = new Vec(e2lastPos.getX(), e2.getPos().getY());
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
			this.checkCollision(e1, e2, new Vec(e1lastPos.getX(), e1.getPos().getY()), new Vec(e2lastPos.getX(), e2.getPos().getY()));
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
			this.checkCollision(e1, e2, new Vec(e1.getPos().getX(), e1lastPos.getY()), new Vec(e2.getPos().getX(), e2lastPos.getY()));
		}

	}

	@Override
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public float getCameraOffset() {
		return this.cameraTarget.getCameraOffset();
	}

	@Override
	public void setCameraTarget(CameraTarget cameraTarget) {
		this.cameraTarget = cameraTarget;
	}

	public int getScore() {
		return (int) (this.player.getCameraOffset() + this.getPlayer().getPoints());
	}

	public int getHighScore() {
		return this.level.getHighScore();
	}

	public void setHighScore(int highScore) {
		this.level.setHighScore(highScore);
	}

	@Override
	public long getTime() {
		return this.model.getTime();
	}

}
