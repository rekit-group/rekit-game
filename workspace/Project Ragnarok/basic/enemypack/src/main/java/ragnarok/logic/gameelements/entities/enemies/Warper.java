package ragnarok.logic.gameelements.entities.enemies;

import home.fox.configuration.Configurable;
import home.fox.configuration.annotations.NoSet;
import home.fox.configuration.annotations.SetterInfo;
import ragnarok.core.GameElement;
import ragnarok.core.GameGrid;
import ragnarok.logic.gameelements.entities.Entity;
import ragnarok.logic.gameelements.particles.ParticleSpawner;
import ragnarok.logic.gameelements.type.Enemy;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Frame;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBColor;
import ragnarok.primitives.time.Timer;
import ragnarok.util.ReflectUtils.LoadMe;

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
	}

	@Override
	public void internalRender(GameGrid f) {
		float progress = this.warpAction.getProgress();
		for (float i = 1; i >= 0.2; i -= 0.1) {
			RGBColor innerColor = new RGBColor((int) (250 * i), (int) (250 * (1 - progress)), (150));
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
