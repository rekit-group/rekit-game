package ragnarok.logic.level.bossstructure;

import java.util.HashMap;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import ragnarok.config.GameConf;
import ragnarok.logic.IScene;
import ragnarok.logic.gameelements.GameElement;
import ragnarok.logic.gameelements.GameElementFactory;
import ragnarok.logic.gameelements.entities.FixedCameraTarget;
import ragnarok.logic.gameelements.entities.Player;
import ragnarok.logic.gameelements.inanimate.InanimateDoor;
import ragnarok.logic.gameelements.inanimate.InanimateTrigger;
import ragnarok.logic.gameelements.particles.ParticleSpawner;
import ragnarok.logic.gameelements.type.Boss;
import ragnarok.logic.gui.Text;
import ragnarok.logic.gui.TimeDecorator;
import ragnarok.logic.level.Structure;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.time.Progress;
import ragnarok.primitives.time.Timer;
import ragnarok.util.CalcUtil;
import ragnarok.util.TextOptions;
import ragnarok.util.ThreadUtils;

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

	/**
	 * Create a boss structure.
	 *
	 * @param structure
	 *            the structure definition
	 * @param boss
	 *            the boss
	 */
	public BossStructure(String[][] structure, Boss boss) {
		super(structure, new HashMap<>());
		this.boss = boss;
	}

	@Override
	public int build(int levelX, boolean autoCoinSpawn) {
		int width = super.build(levelX, false);
		this.levelX = levelX;

		// generate and add door after room
		this.door = new InanimateDoor(new Vec(levelX + width - 1, GameConf.GRID_H / 2));
		GameElementFactory.generate(this.door);

		// generate trigger at door entrance
		this.triggerPos = new Vec(levelX + 6, GameConf.GRID_H - 2);
		InanimateTrigger.createTrigger(this.triggerPos, new Vec(1), this::startBattle);
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
		IScene scene = this.door.getScene();
		// calculate where to put camera
		this.cameraTarget = this.levelX + 5 + GameConf.PLAYER_CAMERA_OFFSET + scene.getPlayer().getSize().getX() / 2;

		// Prepare boss
		this.boss = (Boss) this.boss.create(this.boss.getStartPos().addX(this.levelX), new String[0]);
		this.boss.setBossStructure(this);
		this.boss.setTarget(scene.getPlayer());

		// Create thread for asynchronous stuff
		ThreadUtils.runThread("BossRoom-Start", () -> this.startIntern(scene));

	}

	/**
	 * Start Boss Battle. Show intro text, set camera target.
	 *
	 * @param scene
	 *            the scene
	 */
	private void startIntern(IScene scene) {
		if (this.door == null || this.triggerPos == null) {
			return;
		}

		GameElement player = scene.getPlayer();
		// keep walking right to the right camera position
		while (player.getPos().getX() < this.cameraTarget) {
			player.setVel(player.getVel().setX(1.8f));
			Timer.sleep(GameConf.LOGIC_DELTA);
		}
		scene.setCameraTarget(new FixedCameraTarget(this.cameraTarget - GameConf.PLAYER_CAMERA_OFFSET));
		// Spawn Boss
		GameElementFactory.generate(this.boss);
		// Close door
		GameElementFactory.generateInanimate((int) this.triggerPos.getX(), (int) this.triggerPos.getY());

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
	public void endBattle(IScene scene) {
		// Create thread for asynchronous stuff
		ThreadUtils.runThread("BossRoom-End", () -> this.endAnimation(scene));
	}

	/**
	 * This method will invoked in a separate thread to perform the end
	 * animation of the Boss.
	 *
	 * @param scene
	 *            the scene
	 */
	private final void endAnimation(IScene scene) {
		if (this.door == null) {
			return;
		}
		final Player player = scene.getPlayer();
		final Timer timer = new Timer(7000);

		// Needed for animating camera movement
		Progress cameraMover = new Progress( //
				this.cameraTarget - GameConf.PLAYER_CAMERA_OFFSET, //
				player.getPos().getX() - GameConf.PLAYER_CAMERA_OFFSET //
		);

		// Needed for animating door movement
		Progress doorMover = new Progress( //
				this.door.getPos().getY(), //
				this.door.getPos().getY() - 10 //
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
		if (player.getLives() < GameConf.PLAYER_LIVES) {
			player.setLives(GameConf.PLAYER_LIVES);
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
	private final void phase(IScene scene, Timer timer, Progress doorMover, Progress cameraMover) {
		// phase one: show explosions
		if (timer.getProgress() < 0.4) {
			this.phase1(scene);
		}
		// phase two: show fireworks
		else if (timer.getProgress() < 0.9) {
			this.phase2(scene, timer, doorMover);
		}
		// phase three: re-move camera to player position
		else {
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
	private final void phase1(IScene scene) {
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
	private final void phase2(IScene scene, Timer timer, Progress doorMover) {

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
	private final void phase3(IScene scene, Timer timer, Progress cameraMover) {
		if (this.door != null) {
			// remove door of last phase
			this.door.destroy();
		}
		float prog = (timer.getProgress() - 0.9f) * 10f;
		scene.setCameraTarget(new FixedCameraTarget(cameraMover.getNow(prog)));
	}
}
