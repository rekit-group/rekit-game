/**
 *
 */
package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.CameraTarget;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElementFactory;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gui.LifeGui;
import edu.kit.informatik.ragnarok.logic.gui.ScoreGui;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.gui.parallax.HeapElementCloud;
import edu.kit.informatik.ragnarok.logic.gui.parallax.HeapElementMountain;
import edu.kit.informatik.ragnarok.logic.gui.parallax.HeapLayer;
import edu.kit.informatik.ragnarok.logic.gui.parallax.ParallaxContainer;
import edu.kit.informatik.ragnarok.logic.gui.parallax.TriangulationLayer;
import edu.kit.informatik.ragnarok.logic.level.Level;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 * Scene that holds a playable Level created by a LevelCreator. Different Levels
 * are possible by changing the LevelCreator in the constructor.
 *
 * @author matze
 *
 */
public abstract class LevelScene extends Scene {

	private Player player = new Player(new Vec(6, 5));

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
		GameElementFactory.setScene(this);

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
		return;
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
	protected void logicLoopAfter() {
		if (this.isPaused()) {
			return;
		}
		synchronized (this.synchronize()) {
			// iterate all GameElements to detect collision
			this.getGameElementIterator().forEachRemaining((e1) -> {
				if (!e1.getTeam().isNeutral()) {
					this.getGameElementIterator().forEachRemaining((e2) -> {
						if (!e2.getTeam().isNeutral() && e1 != e2) {
							e1.checkCollision(e2);
						}
					});
				}

			});

		}

		if (this.player.getDeleteMe()) {
			this.end(false);
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

	@Override
	public int getScore() {
		return (int) (this.player.getCameraOffset() + this.getPlayer().getPoints());
	}

	@Override
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

	@Override
	public final MenuItem getMenu() {
		throw new UnsupportedOperationException("Menu not supported in LevelScene");
	}
}
