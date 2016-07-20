package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Frame;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.primitives.time.Timer;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.visitor.NoVisit;
import edu.kit.informatik.ragnarok.visitor.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.Visitable;

/**
 *
 * This class realizes on of the most dangerous {@link Enemy Enemies}<br>
 * No Player can destroy that circle of colors, the only chance to destroy: "Run
 * away"
 *
 */
@LoadMe
@VisitInfo(res = "conf/basic_enemypack", visit = true)
public class Warper extends Enemy implements Visitable {

	private static float WARPER_WARP_DELTA;

	/**
	 * The time between the next jump (to next position)
	 */
	@NoVisit
	private Timer warpAction = new Timer(Warper.WARPER_WARP_DELTA);

	/**
	 * The particles of the warper
	 */
	@NoVisit
	private static ParticleSpawner warpParticles = null;

	static {
		Warper.warpParticles = new ParticleSpawner();
		Warper.warpParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), (float) (2 * Math.PI), (float) (4 * Math.PI));
		Warper.warpParticles.colorR = new ParticleSpawnerOption(250, 0);
		Warper.warpParticles.colorG = new ParticleSpawnerOption(250, -250);
		Warper.warpParticles.colorB = new ParticleSpawnerOption(150);
		Warper.warpParticles.colorA = new ParticleSpawnerOption(220, -220);
		Warper.warpParticles.timeMin = 1f;
		Warper.warpParticles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
	}

	/**
	 * Prototype Constructor
	 */
	public Warper() {
		super();
	}

	/**
	 * Create a warper by start position
	 *
	 * @param startPos
	 *            the start position
	 */
	public Warper(Vec startPos) {
		super(startPos, new Vec(), new Vec(0.6f, 0.6f));
	}

	@Override
	public void internalRender(Field f) {
		float progress = this.warpAction.getProgress();
		for (float i = 1; i >= 0.2; i -= 0.1) {
			RGBColor innerColor = new RGBColor((int) (250 * i), (int) (250 * (1 - progress)), (150));
			// draw body
			f.drawCircle(this.getPos(), this.getSize().scalar(i), innerColor);
		}

	}

	@Override
	public void logicLoop(float deltaTime) {
		// decrease time left
		this.warpAction.removeTime(deltaTime);

		// animate particles
		Warper.warpParticles.amountMin = -5;
		Warper.warpParticles.amountMax = 2;
		Warper.warpParticles.spawn(this.getScene(), this.getPos());

		// if time is up
		if (this.warpAction.timeUp()) {
			// reset time
			this.warpAction.reset();

			// get target (player)
			Vec target = this.getScene().getPlayer().getPos();

			// animate particles
			Warper.warpParticles.amountMin = 5;
			Warper.warpParticles.amountMax = 8;
			Warper.warpParticles.spawn(this.getScene(), this.getPos());

			// determine if x or y is greater in distance
			Vec dif = this.getPos().add(target.scalar(-1));
			if (Math.abs(dif.getX()) > Math.abs(dif.getY())) {
				this.setPos(this.getPos().addX(-Math.signum(dif.getX())));
			} else {
				this.setPos(this.getPos().addY(-Math.signum(dif.getY())));
			}
		}
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {

			// Give player damage
			element.addDamage(1);

			// Kill the warper itself
			this.addDamage(1);
		}
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing, pass right through everything
	}

	@Override
	public void addDamage(int damage) {
		this.destroy();
	}

	@Override
	public Entity create(Vec startPos, String[] options) {
		return new Warper(startPos);
	}

}
