package edu.kit.informatik.ragnarok.logic.level.bossstructure;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElementFactory;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.FixedCameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.Text;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.TimeDecorator;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateDoor;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateTrigger;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Boss;
import edu.kit.informatik.ragnarok.logic.level.Structure;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.ProgressDependency;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.CalcUtil;
import edu.kit.informatik.ragnarok.util.TextOptions;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

public class BossStructure extends Structure {
	private InanimateDoor door;
	private Vec triggerPos;
	private Boss boss;
	private float cameraTarget;
	private int levelX;

	private static ParticleSpawner explosionParticles = null;
	static {
		BossStructure.explosionParticles = new ParticleSpawner();
		BossStructure.explosionParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), 0, 0);
		BossStructure.explosionParticles.colorR = new ParticleSpawnerOption(200, 230, 10, 25);
		BossStructure.explosionParticles.colorG = new ParticleSpawnerOption(200, 250, -130, -110);
		BossStructure.explosionParticles.colorB = new ParticleSpawnerOption(150, 200, -130, -110);
		BossStructure.explosionParticles.colorA = new ParticleSpawnerOption(230, 250, -120, -200);
		BossStructure.explosionParticles.timeMin = 0.1f;
		BossStructure.explosionParticles.timeMax = 0.3f;
		BossStructure.explosionParticles.amountMin = 40;
		BossStructure.explosionParticles.amountMax = 50;
		BossStructure.explosionParticles.speed = new ParticleSpawnerOption(4, 9, -1, 1);
	}

	private static ParticleSpawner fireworkParticles = null;
	static {
		BossStructure.fireworkParticles = new ParticleSpawner();
		BossStructure.fireworkParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), 0, (float) (Math.PI));
		BossStructure.fireworkParticles.colorR = new ParticleSpawnerOption(100, 250, -100, 5);
		BossStructure.fireworkParticles.colorG = new ParticleSpawnerOption(100, 250, -100, 5);
		BossStructure.fireworkParticles.colorB = new ParticleSpawnerOption(100, 250, -100, 5);
		BossStructure.fireworkParticles.colorA = new ParticleSpawnerOption(230, 250, -120, -200);
		BossStructure.fireworkParticles.timeMin = 0.5f;
		BossStructure.fireworkParticles.timeMax = 1f;
		BossStructure.fireworkParticles.amountMin = 40;
		BossStructure.fireworkParticles.amountMax = 50;
		BossStructure.fireworkParticles.speed = new ParticleSpawnerOption(3, 5, -1, 1);
	}

	public BossStructure(int[][] structure, Boss boss) {
		super(structure);
		this.boss = boss;
	}

	@Override
	public int build(int levelX, boolean autoCoinSpawn) {
		int width = super.build(levelX, false);
		this.levelX = levelX;

		// generate and add door after room
		this.door = new InanimateDoor(new Vec(levelX + width - 1, (float) Math.ceil(GameConf.GRID_H / 2)));
		GameElementFactory.generate(this.door);

		// generate trigger at door entrance
		this.triggerPos = new Vec(levelX + 6, GameConf.GRID_H - 2);
		// add its perform-method
		InanimateTrigger trigger = new InanimateTrigger(this.triggerPos, new Vec(1, 1)) {
			@Override
			public void perform() {
				if (!this.deleteMe) {
					BossStructure.this.startBattle(this.scene);
				}
			}
		};
		// and add it to game
		GameElementFactory.generate(trigger);

		return width;
	}

	public void startBattle(Scene scene) {

		Player player = scene.getPlayer();

		// calculate where to put camera
		this.cameraTarget = this.levelX + 5 + GameConf.PLAYER_CAMERA_OFFSET + player.getSize().getX() / 2;

		// Prepare boss
		this.boss = (Boss) this.boss.create(new Vec(this.levelX + this.getWidth() / 2, GameConf.GRID_H / 2 + 1), new int[] {});
		this.boss.setBossStructure(this);
		this.boss.setTarget(player);

		// Create thread for asynchronous stuff
		ThreadUtils.runThread(() -> {
			// keep walking right to the right camera position
				while (player.getPos().getX() < BossStructure.this.cameraTarget) {
					player.setVel(player.getVel().setX(1.8f));
					ThreadUtils.sleep(GameConf.LOGIC_DELTA);
				}
				scene.setCameraTarget(new FixedCameraTarget(BossStructure.this.cameraTarget - GameConf.PLAYER_CAMERA_OFFSET));
				// Spawn Boss
				GameElementFactory.generate(this.boss);
				// Close door
				GameElementFactory.generate(1, (int) BossStructure.this.triggerPos.getX(), (int) BossStructure.this.triggerPos.getY());

				// Boss text

				TextOptions op = new TextOptions(new Vec(-0.5f, -0.5f), 30, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
				Text bossText = new Text(scene, op);
				bossText.setText(BossStructure.this.boss.getName());
				bossText.setPos(CalcUtil.units2pixel(new Vec(GameConf.GRID_W / 2f, GameConf.GRID_H / 2f)));
				TimeDecorator bossTextGui = new TimeDecorator(scene, bossText, new TimeDependency(3));
				scene.addGuiElement(bossTextGui);
			});

	}

	public void endBattle(Scene scene) {
		final Player player = scene.getPlayer();

		final TimeDependency timer = new TimeDependency(7f);

		// Needed for animating camera movement
		ProgressDependency cameraMover = new ProgressDependency(this.cameraTarget - GameConf.PLAYER_CAMERA_OFFSET, player.getPos().getX()
				- GameConf.PLAYER_CAMERA_OFFSET);

		// Needed for animating door movement
		ProgressDependency doorMover = new ProgressDependency(this.door.getPos().getY(), this.door.getPos().getY() - 10);

		// Create thread for asynchronous stuff
		ThreadUtils.runThread(() -> {
			// save Players current velocity
				Vec playerVelSave = player.getVel();
				Vec playerPosSave = player.getPos();
				Vec bossPosSave = this.boss.getPos();
				// while timer has time left...
				while (!timer.timeUp()) {
					// freeze player and pos
					player.setVel(new Vec());
					player.setPos(playerPosSave);
					this.boss.setVel(new Vec());
					this.boss.setPos(bossPosSave);
					// wait for time to be up
					ThreadUtils.sleep(GameConf.LOGIC_DELTA);
					// phase one: show explosions
					if (timer.getProgress() < 0.4) {
						if (Math.random() > 0.9) {
							Vec randPos = BossStructure.this.boss.getPos()
									.add(new Vec((float) Math.random() * 2 - 1, (float) Math.random() * 2f - 1));
							BossStructure.explosionParticles.spawn(scene, randPos);
						}
					}
					// phase two: show fireworks
				else if (timer.getProgress() < 0.9) {
					// remove boss of last phase
					scene.removeGameElement(BossStructure.this.boss);

					// show fireworks
					if (Math.random() > 0.9) {
						float deltaX = GameConf.GRID_W / 2f;
						float midX = BossStructure.this.levelX + deltaX;

						float deltaY = GameConf.GRID_H / 2f;
						float midY = deltaY;

						Vec randPos = new Vec(midX + (float) Math.random() * deltaX * 2 - deltaX, midY + (float) Math.random() * deltaY * 2 - deltaY);
						BossStructure.fireworkParticles.spawn(scene, randPos);
					}

					// open door slowly
					float prog = (timer.getProgress() - 0.4f) * 2f;
					BossStructure.this.door.setPos(BossStructure.this.door.getPos().setY(doorMover.getNow(prog)));
				}
				// phase three: re-move camera to player position
				else {
					// remove door of last phase
					this.door.destroy();
					float prog = (timer.getProgress() - 0.9f) * 10f;
					scene.setCameraTarget(new FixedCameraTarget(cameraMover.getNow(prog)));
				}

				timer.removeTime(GameConf.LOGIC_DELTA / 1000f);
			}

			// re-apply velocity to Player
			player.setVel(playerVelSave);
			// give player full health
			if (player.getLives() < GameConf.PLAYER_LIVES) {
				player.setLives(GameConf.PLAYER_LIVES);
			}
			// set camera back to player
			player.resetCameraOffset();
			scene.setCameraTarget(player);
		});

	}
}
