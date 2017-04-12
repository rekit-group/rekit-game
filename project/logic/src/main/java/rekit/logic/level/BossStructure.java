package rekit.logic.level;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.config.GameConf;
import rekit.logic.ILevelScene;
import rekit.logic.IScene;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.GameElementFactory;
import rekit.logic.gameelements.entities.FixedCameraTarget;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.inanimate.InanimateDoor;
import rekit.logic.gameelements.inanimate.InanimateTrigger;
import rekit.logic.gameelements.particles.ParticleSpawner;
import rekit.logic.gameelements.type.Boss;
import rekit.logic.gui.Text;
import rekit.logic.gui.TimeDecorator;
import rekit.primitives.TextOptions;
import rekit.primitives.geometry.Vec;
import rekit.primitives.time.Progress;
import rekit.primitives.time.Timer;
import rekit.util.CalcUtil;
import rekit.util.ThreadUtils;

/**
 *
 * This class realizes a {@link Structure} for bosses.
 *
 */
@SetterInfo(res = "conf/bossstructure")
public final class BossStructure extends Structure implements Configurable {
	/**
	 * The door.
	 */
	@NoSet
	private InanimateDoor door;
	/**
	 * The trigger's position.
	 */
	@NoSet
	private Vec triggerPos;
	/**
	 * The boss.
	 */
	@NoSet
	private Boss boss;
	/**
	 * The camera target offset.
	 */
	@NoSet
	private float cameraTarget;
	/**
	 * The x pos of the level.
	 */
	@NoSet
	private int levelX;
	/**
	 * Explosion particles.
	 */
	private static ParticleSpawner EXPLOSION_PARTICLES;
	/**
	 * Fireworks particles.
	 */
	private static ParticleSpawner FIREWORKS_PARTICLES;

	@NoSet
	private boolean ended = false;

	/**
	 * Create a boss structure.
	 *
	 * @param structure
	 *            the structure definition
	 * @param boss
	 *            the boss
	 */
	public BossStructure(String[][] structure, Boss boss) {
		super(null, structure);
		this.boss = boss;
	}

	@Override
	protected String[] applyAlias(String src) {
		return src.split(":");
	}

	@Override
	public int build(int levelX, boolean autoCoinSpawn) {
		int width = super.build(levelX, false);
		this.levelX = levelX;

		// generate and add door after room
		this.door = new InanimateDoor(new Vec(levelX + width - 1, GameConf.GRID_H / 2));
		GameElementFactory.generate(this.door);

		// generate trigger at door entrance
		this.triggerPos = new Vec(levelX, GameConf.GRID_H - 2);
		for (int y = 0; y < 3; ++y) {
			// this is straight out disgusting
			InanimateTrigger.createTrigger(this.triggerPos.addY(-y), new Vec(1), this::startBattle);
		}

		return width;
	}

	/**
	 * Start the battle.
	 *
	 */
	public void startBattle() {
		if (this.door == null || this.triggerPos == null) {
			return;
		}
		this.ended = false;
		ILevelScene scene = this.door.getScene();
		// calculate where to put camera
		this.cameraTarget = this.levelX - 1 + Player.CAMERA_OFFSET + scene.getPlayer().getSize().x / 2;

		// Prepare boss
		this.boss = (Boss) this.boss.create(this.boss.getStartPos().addX(this.levelX), new String[0]);
		this.boss.setBossStructure(this);
		this.boss.setTarget(scene.getPlayer());

		// Create thread for asynchronous stuff
		ThreadUtils.runDaemon("BossRoom-Start", () -> this.startIntern(scene));

	}

	/**
	 * Start Boss Battle. Show intro text, set camera target.
	 *
	 * @param scene
	 *            the scene
	 */
	private void startIntern(ILevelScene scene) {
		if (this.door == null || this.triggerPos == null) {
			return;
		}

		GameElement player = scene.getPlayer();
		// keep walking right to the right camera position
		while (player.getPos().x < this.cameraTarget) {
			player.setVel(player.getVel().setX(1.8f));
			Timer.sleep(GameConf.LOGIC_DELTA);
		}
		scene.setCameraTarget(new FixedCameraTarget(this.cameraTarget - Player.CAMERA_OFFSET));
		// Spawn Boss
		GameElementFactory.generate(this.boss);

		for (int y = 0; y < 3; ++y) {
			// Close door
			GameElementFactory.generateInanimate((int) this.triggerPos.x, (int) this.triggerPos.y - y);
		}

		// Boss text
		TextOptions op = new TextOptions(new Vec(-0.5f, -0.5f), 30, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
		Text bossText = new Text(scene, op).setText(this.boss.getName());
		bossText.setPos(CalcUtil.units2pixel(new Vec(GameConf.GRID_W / 2f, GameConf.GRID_H / 2f)));
		scene.addGuiElement(new TimeDecorator(scene, bossText, new Timer(3000)));

	}

	/**
	 * End the battle.
	 *
	 * @param scene
	 *            the scene
	 */
	public void endBattle(ILevelScene scene) {
		if (this.ended) {
			return;
		}
		this.ended = true;
		// Create thread for asynchronous stuff
		ThreadUtils.runDaemon("BossRoom-End", () -> this.endAnimation(scene));
	}

	/**
	 * This method will invoked in a separate thread to perform the end
	 * animation of the Boss.
	 *
	 * @param scene
	 *            the scene
	 */
	private void endAnimation(ILevelScene scene) {
		if (this.door == null) {
			return;
		}
		final Player player = scene.getPlayer();
		final Timer timer = new Timer(7000);

		// Needed for animating camera movement
		Progress cameraMover = new Progress(//
				this.cameraTarget - Player.CAMERA_OFFSET, //
				player.getPos().x - Player.CAMERA_OFFSET //
		);

		// Needed for animating door movement
		Progress doorMover = new Progress(//
				this.door.getPos().y, //
				this.door.getPos().y - 10 //
		);
		// save Players current velocity
		Vec[] save = { player.getVel(), player.getPos(), this.boss.getPos() };
		// while timer has time left...
		while (!timer.timeUp()) {
			// freeze player and pos
			player.setVel(new Vec());
			player.setPos(save[1]);
			this.boss.setVel(new Vec());
			this.boss.setPos(save[2]);
			// wait for time to be up
			Timer.sleep(GameConf.LOGIC_DELTA);
			this.phase(scene, timer, doorMover, cameraMover);
			timer.logicLoop();
		}

		// re-apply velocity to Player
		player.setVel(save[0]);
		// give player full health
		if (player.getLives() < Player.LIVES) {
			player.setLives(Player.LIVES);
		}
		// set camera back to player
		player.resetCameraOffset();
		scene.setCameraTarget(player);

	}

	/**
	 * Determinate and invoke phase.
	 *
	 * @param scene
	 *            the scene
	 * @param timer
	 *            the timer
	 * @param doorMover
	 *            the door mover
	 * @param cameraMover
	 *            the camera mover
	 * @see #phase1(IScene)
	 * @see #phase2(IScene, Timer, Progress)
	 * @see #phase3(IScene, Timer, Progress)
	 */
	private void phase(IScene scene, Timer timer, Progress doorMover, Progress cameraMover) {
		if (timer.getProgress() < 0.4) {
			// phase one: show explosions
			this.phase1(scene);
		} else if (timer.getProgress() < 0.9) {
			// phase two: show fireworks
			this.phase2(scene, timer, doorMover);
		} else {
			// phase three: re-move camera to player position
			this.phase3(scene, timer, cameraMover);
		}
	}

	/**
	 * Phase 1: {@link #EXPLOSION_PARTICLES} will shown and {@link Boss}
	 * wobbles.
	 *
	 * @param scene
	 *            the scene
	 */
	private void phase1(IScene scene) {
		if (GameConf.PRNG.nextDouble() > 0.9) {
			Vec randPos = this.boss.getPos().add(new Vec((float) GameConf.PRNG.nextDouble() * 2 - 1, (float) GameConf.PRNG.nextDouble() * 2f - 1));
			BossStructure.EXPLOSION_PARTICLES.spawn(scene, randPos);
		}
	}

	/**
	 * Phase 2: {@link #FIREWORKS_PARTICLES} will shown and door moves up
	 * (opens).
	 *
	 * @param scene
	 *            the scene
	 * @param timer
	 *            the timer to determinate door movement progress
	 * @param doorMover
	 *            the door mover
	 */
	private void phase2(IScene scene, Timer timer, Progress doorMover) {

		// remove boss of last phase
		scene.markForRemove(this.boss);

		// show fireworks
		if (GameConf.PRNG.nextDouble() > 0.9) {
			float deltaX = GameConf.GRID_W / 2f;
			float midX = this.levelX + deltaX;

			float deltaY = GameConf.GRID_H / 2f;
			float midY = deltaY;

			Vec randPos = new Vec(midX + (float) GameConf.PRNG.nextDouble() * deltaX * 2 - deltaX,
					midY + (float) GameConf.PRNG.nextDouble() * deltaY * 2 - deltaY);
			BossStructure.FIREWORKS_PARTICLES.spawn(scene, randPos);
		}

		// open door slowly
		float prog = (timer.getProgress() - 0.4f) * 2f;
		if (this.door != null) {
			this.door.setPos(this.door.getPos().setY(doorMover.getNow(prog)));
		}
	}

	/**
	 * Phase 3: Door will be destroyed. Camera focus moves back to
	 * {@link Player}.
	 *
	 * @param scene
	 *            the scene
	 * @param timer
	 *            the timer to determinate camera movement progress
	 * @param cameraMover
	 *            the camera mover
	 */
	private void phase3(IScene scene, Timer timer, Progress cameraMover) {
		if (this.door != null) {
			// remove door of last phase
			this.door.destroy();
		}
		float prog = (timer.getProgress() - 0.9f) * 10f;
		scene.setCameraTarget(new FixedCameraTarget(cameraMover.getNow(prog)));
	}
}
