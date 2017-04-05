package rekit.logic.gameelements.entities.enemies;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.core.GameGrid;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gameelements.particles.ParticleSpawner;
import rekit.logic.gameelements.type.Enemy;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Frame;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.time.Timer;
import rekit.util.ReflectUtils.LoadMe;

/**
 *
 * This class realizes on of the most dangerous {@link Enemy Enemies}.<br>
 * No Player can destroy that circle of colors, the only chance to destroy: "Run
 * away"
 *
 */
@LoadMe
@SetterInfo(res = "conf/warper")
public final class Warper extends Enemy implements Configurable {

	/**
	 * The delta time between position changes.
	 */
	private static float WARPER_WARP_DELTA;
	
	private static int WARPS;

	/**
	 * The time between the next jump (to next position).
	 */
	@NoSet
	private final Timer warpAction = new Timer((long) (1000 * Warper.WARPER_WARP_DELTA));

	/**
	 * The particles of the warper.
	 */
	private static ParticleSpawner WARP_PARTICLES;

	/**
	 * The default size.
	 */
	private static Vec SIZE;

	/**
	 * Prototype Constructor.
	 */
	public Warper() {
		super();
	}

	/**
	 * Create a warper by start position.
	 *
	 * @param startPos
	 *            the start position
	 */
	public Warper(Vec startPos) {
		super(startPos, new Vec(), Warper.SIZE);
		this.lives = Warper.WARPS;
	}

	@Override
	public void internalRender(GameGrid f) {
		float progress = this.warpAction.getProgress();
		int alpha = (int) (255 * (this.lives / (float) Warper.WARPS));
		for (float i = 1; i >= 0.2; i -= 0.1) {
			RGBAColor innerColor = new RGBAColor((int) (250 * i), (int) (250 * (1 - progress)), 150, alpha);
			// draw body
			f.drawCircle(this.getPos(), this.getSize().scalar(i), innerColor);
		}

	}

	@Override
	protected void innerLogicLoop() {
		// decrease time left
		this.warpAction.logicLoop();
		// this.warpAction.removeTime(this.deltaTime);

		// animate particles
		Warper.WARP_PARTICLES.amountMin = -5;
		Warper.WARP_PARTICLES.amountMax = 2;
		Warper.WARP_PARTICLES.spawn(this.getScene(), this.getPos());

		// if time is up
		if (this.warpAction.timeUp()) {
			// reset time
			this.warpAction.reset();

			// get target (player)
			Vec target = this.getScene().getPlayer().getPos();

			// animate particles
			Warper.WARP_PARTICLES.amountMin = 5;
			Warper.WARP_PARTICLES.amountMax = 8;
			Warper.WARP_PARTICLES.spawn(this.getScene(), this.getPos());

			// determine if x or y is greater in distance
			Vec dif = this.getPos().add(target.scalar(-1));
			if (Math.abs(dif.x) > Math.abs(dif.y)) {
				this.setPos(this.getPos().addX(-Math.signum(dif.x)));
			} else {
				this.setPos(this.getPos().addY(-Math.signum(dif.y)));
			}
			
			this.addDamage(1);
			this.invincibility = null;
		}
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {

			// Give player damage
			element.addDamage(1);

			// Kill the warper itself
			this.destroy();
		}
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing, pass right through everything
	}

	@Override
	public Entity create(Vec startPos, String[] options) {
		return new Warper(startPos);
	}

}
