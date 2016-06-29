package edu.kit.informatik.ragnarok.logic.levelcreator;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.FixedCameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.type.Boss;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.Text;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.TimeDecorator;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateDoor;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateTrigger;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.ProgressDependency;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.CalcUtil;
import edu.kit.informatik.ragnarok.util.TextOptions;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

public class BossRoom {

	private LevelStructure roomStructure;
	private int x;
	private LevelCreator levelCreator;
	private InanimateDoor door;
	private Vec triggerPos;
	private Boss boss;
	private float cameraTarget;

	private static ParticleSpawner explosionParticles = null;
	static {
		BossRoom.explosionParticles = new ParticleSpawner();
		BossRoom.explosionParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), 0, 0);
		BossRoom.explosionParticles.colorR = new ParticleSpawnerOption(200, 230, 10, 25);
		BossRoom.explosionParticles.colorG = new ParticleSpawnerOption(200, 250, -130, -110);
		BossRoom.explosionParticles.colorB = new ParticleSpawnerOption(150, 200, -130, -110);
		BossRoom.explosionParticles.colorA = new ParticleSpawnerOption(230, 250, -120, -200);
		BossRoom.explosionParticles.timeMin = 0.1f;
		BossRoom.explosionParticles.timeMax = 0.3f;
		BossRoom.explosionParticles.amountMin = 40;
		BossRoom.explosionParticles.amountMax = 50;
		BossRoom.explosionParticles.speed = new ParticleSpawnerOption(4, 9, -1, 1);
	}

	private static ParticleSpawner fireworkParticles = null;
	static {
		BossRoom.fireworkParticles = new ParticleSpawner();
		BossRoom.fireworkParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), 0, (float) (Math.PI));
		BossRoom.fireworkParticles.colorR = new ParticleSpawnerOption(100, 250, -100, 5);
		BossRoom.fireworkParticles.colorG = new ParticleSpawnerOption(100, 250, -100, 5);
		BossRoom.fireworkParticles.colorB = new ParticleSpawnerOption(100, 250, -100, 5);
		BossRoom.fireworkParticles.colorA = new ParticleSpawnerOption(230, 250, -120, -200);
		BossRoom.fireworkParticles.timeMin = 0.5f;
		BossRoom.fireworkParticles.timeMax = 1f;
		BossRoom.fireworkParticles.amountMin = 40;
		BossRoom.fireworkParticles.amountMax = 50;
		BossRoom.fireworkParticles.speed = new ParticleSpawnerOption(3, 5, -1, 1);
	}

	public BossRoom(Boss boss, LevelStructure roomStructure, final LevelCreator levelCreator) {
		this.roomStructure = roomStructure;
		this.levelCreator = levelCreator;
		this.boss = boss;
	}

	public void generate(int x) {
		this.x = x;

		// generate floor before boss room
		this.levelCreator.generateEvenFloor(x, x + 5);
		// generate boss room structure
		this.roomStructure.buildStructure(this.levelCreator, x + 6, GameConf.GRID_H);
		this.levelCreator.generatedUntil += this.roomStructure.getWidth();
		// generate floor after boss room
		this.levelCreator.generateEvenFloor(x + 6 + this.roomStructure.getWidth(), x + 6 + this.roomStructure.getWidth() + 5);

		// generate door after room
		this.door = new InanimateDoor(new Vec(x + 5 + this.roomStructure.getWidth(), (float) Math.ceil(GameConf.GRID_H / 2)));
		this.levelCreator.generateGameElement(this.door);

		this.triggerPos = new Vec(x + 6, GameConf.GRID_H - 2);
		InanimateTrigger trigger = new InanimateTrigger(this.triggerPos, new Vec(1, 1)) {
			@Override
			public void perform() {
				if (!this.deleteMe) {
					BossRoom.this.startBattle(this);
				}
			}
		};
		this.levelCreator.generateGameElement(trigger);
	}

	public void startBattle(GameElement trigger) {

		// destroy invisible InanimateTrigger
		trigger.destroy();

		final Scene scene = this.levelCreator.getScene();
		final Player player = scene.getPlayer();

		// calculate where to put camera
		this.cameraTarget = this.x + 5 + GameConf.PLAYER_CAMERA_OFFSET + player.getSize().getX() / 2;

		// Prepare boss
		this.boss.setBossRoom(this);
		this.boss.setPos(new Vec(this.x + 6 + this.roomStructure.getWidth() / 2, GameConf.GRID_H / 2 + 1));
		this.boss.setTarget(player);

		// Create thread for asynchronous stuff
		ThreadUtils.runThread(() -> {
			// keep walking right to the right camera position
			while (player.getPos().getX() < BossRoom.this.cameraTarget) {
				player.setVel(player.getVel().setX(1.8f));
				ThreadUtils.sleep(GameConf.LOGIC_DELTA);
			}
			scene.setCameraTarget(new FixedCameraTarget(BossRoom.this.cameraTarget - GameConf.PLAYER_CAMERA_OFFSET));
			// Spawn Boss
			this.levelCreator.generateGameElement(BossRoom.this.boss);
			// Close door
			this.levelCreator.generateBox((int) BossRoom.this.triggerPos.getX(), (int) BossRoom.this.triggerPos.getY());

			// Boss text

			TextOptions op = new TextOptions(new Vec(-0.5f, -0.5f), 30, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
			Text bossText = new Text(scene, op);
			bossText.setText(BossRoom.this.boss.getName());
			bossText.setPos(CalcUtil.units2vec(new Vec(GameConf.GRID_W / 2f, GameConf.GRID_H / 2f)));
			TimeDecorator bossTextGui = new TimeDecorator(scene, bossText, new TimeDependency(3));
			scene.addGuiElement(bossTextGui);
		});

	}

	public void endBattle() {
		final Scene scene = this.levelCreator.getScene();
		final Player player = scene.getPlayer();

		final TimeDependency timer = new TimeDependency(7f);

		// Needed for animating camera movement
		ProgressDependency cameraMover = new ProgressDependency(this.cameraTarget - GameConf.PLAYER_CAMERA_OFFSET,
				player.getPos().getX() - GameConf.PLAYER_CAMERA_OFFSET);

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
						Vec randPos = BossRoom.this.boss.getPos().add(new Vec((float) Math.random() * 2 - 1, (float) Math.random() * 2f - 1));
						BossRoom.explosionParticles.spawn(scene, randPos);
					}
				}
				// phase two: show fireworks
				else if (timer.getProgress() < 0.9) {
					// remove boss of last phase
					scene.removeGameElement(BossRoom.this.boss);

					// show fireworks
					if (Math.random() > 0.9) {
						float deltaX = GameConf.GRID_W / 2f;
						float midX = BossRoom.this.x + deltaX;

						float deltaY = GameConf.GRID_H / 2f;
						float midY = deltaY;

						Vec randPos = new Vec(midX + (float) Math.random() * deltaX * 2 - deltaX, midY + (float) Math.random() * deltaY * 2 - deltaY);
						BossRoom.fireworkParticles.spawn(scene, randPos);
					}

					// open door slowly
					float prog = (timer.getProgress() - 0.4f) * 2f;
					BossRoom.this.door.setPos(BossRoom.this.door.getPos().setY(doorMover.getNow(prog)));
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
			if (player.getLifes() < GameConf.PLAYER_LIFES) {
				player.setLifes(GameConf.PLAYER_LIFES);
			}
			// set camera back to player
			player.resetCameraOffset();
			scene.setCameraTarget(player);
		});

	}
}
