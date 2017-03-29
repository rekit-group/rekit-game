package rekit.logic.gameelements.inanimate;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.core.GameTime;
import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.particles.ParticleSpawner;
import rekit.logic.gameelements.particles.ParticleSpawnerOption;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.util.LambdaUtil;

/**
 *
 * This class represents an EndPortal.
 *
 */
@SetterInfo(res = "conf/endtrigger")
public final class EndTrigger extends InanimateTrigger implements Configurable {
	/**
	 * The prototype-instance.
	 */
	@NoSet
	private static EndTrigger instance;
	/**
	 * The number of portal-rings.
	 */
	private static int PORTAL_NUM;
	/**
	 * The inner portals.
	 */
	@NoSet
	private Portal[] innerPortals;
	/**
	 * The particle spawner.
	 */
	private static ParticleSpawner PORTAL_PARTICLES;

	/**
	 * Create an EndTrigger.
	 *
	 * @param pos
	 *            the position
	 * @param size
	 *            the size
	 */
	private EndTrigger(Vec pos, Vec size) {
		super(pos, size);
		this.preparePortals();
	}

	/**
	 * Prepare the portalrings.
	 */
	private void preparePortals() {
		this.innerPortals = new Portal[EndTrigger.PORTAL_NUM];

		Vec pos = this.getPos();
		Vec size = this.getSize().scalar(2f, 1f);
		Vec lastAmplitude;
		Vec amplitude = new Vec();

		for (int i = 0; i < EndTrigger.PORTAL_NUM; i++) {
			Vec frequency = new Vec(GameConf.PRNG.nextFloat() * 8 + 6, GameConf.PRNG.nextFloat() * 8 + 6);
			lastAmplitude = amplitude;
			amplitude = new Vec(GameConf.PRNG.nextFloat() / 20 + 0.04f, GameConf.PRNG.nextFloat() / 20 + 0.1f);
			Vec phase = new Vec(GameConf.PRNG.nextFloat() * 2 + 1, GameConf.PRNG.nextFloat() * 2 + 1);

			RGBAColor color = new RGBAColor((int) (GameConf.PRNG.nextFloat() * 50), (int) (GameConf.PRNG.nextFloat() * 30 + 7 * i),
					(int) (GameConf.PRNG.nextFloat() * 80 + 175), 10 * i);

			size = size.add(lastAmplitude.add(amplitude).scalar(-1f));

			pos = pos.addX(0.003f * i);

			this.innerPortals[i] = new Portal(pos, size, amplitude, frequency, phase, color);
		}
	}

	@Override
	public void logicLoop() {
		for (Portal portal : this.innerPortals) {
			portal.logicLoop();
		}
	}

	/**
	 * This inner class represents the different portal-rings.
	 *
	 */
	private class Portal extends GameElement {
		/**
		 * The amplitude.
		 */
		private Vec amplitude;
		/**
		 * The frequency.
		 */
		private Vec frequency;
		/**
		 * The phase.
		 */
		private Vec phase;
		/**
		 * The x-position.
		 */
		private float x;
		/**
		 * The color.
		 */
		private RGBAColor color;
		/**
		 * The current size of the portal-ring.
		 */
		private Vec currentSize;

		/**
		 * The last time of invoking {@link #logicLoop()}.
		 */
		private long lastTime = GameTime.getTime();

		/**
		 * Create a portal-ring.
		 *
		 * @param pos
		 *            the position
		 * @param size
		 *            the size
		 * @param amplitude
		 *            the amplitude
		 * @param frequency
		 *            the frequency
		 * @param phase
		 *            the phase
		 * @param color
		 *            the color
		 */
		Portal(Vec pos, Vec size, Vec amplitude, Vec frequency, Vec phase, RGBAColor color) {
			super(pos, new Vec(), size, Team.TRIGGER);
			this.amplitude = amplitude;
			this.frequency = frequency;
			this.phase = phase;
			this.color = color;
		}

		@Override
		public void logicLoop() {
			long deltaTime = GameTime.getTime() - this.lastTime;
			this.lastTime += deltaTime;

			this.x += deltaTime / 1000F;

			// some weird "amplitude * sin(phase + frequency * x)" action
			this.currentSize = this.getSize().add(this.amplitude.multiply(this.phase.add(this.frequency.scalar(this.x)).sin()));

			float randomAngle = (float) (GameConf.PRNG.nextDouble() * 2 * Math.PI);
			float r = this.currentSize.x * 2;

			float x = (float) (Math.cos(randomAngle) * r);
			float y = (float) (Math.sin(randomAngle) * r);

			y = (y / this.currentSize.x) * this.currentSize.y;

			EndTrigger.PORTAL_PARTICLES.angle = new ParticleSpawnerOption((float) (randomAngle - Math.PI / 2));
			EndTrigger.PORTAL_PARTICLES.colorR = new ParticleSpawnerOption((float) (this.color.red * 1.2));
			EndTrigger.PORTAL_PARTICLES.colorG = new ParticleSpawnerOption((float) (this.color.green * 1.2));
			EndTrigger.PORTAL_PARTICLES.colorB = new ParticleSpawnerOption((float) (this.color.blue * 1.2));

			EndTrigger.PORTAL_PARTICLES.spawn(EndTrigger.this.getScene(), new Vec(x, y).add(this.getPos()));

		}

		@Override
		public void internalRender(GameGrid f) {
			f.drawCircle(this.getPos(), this.currentSize, this.color);
		}
	}

	@Override
	public void internalRender(GameGrid f) {
		for (Portal portal : this.innerPortals) {
			portal.render(f);
		}
	}

	@Override
	public void perform() {
		// Make player invisible
		this.getScene().getPlayer().setTemporaryApperance(LambdaUtil.destroy(), 3000);
		this.getScene().end(true);
	}

	@Override
	public EndTrigger create(Vec startPos, String[] options) {
		return new EndTrigger(startPos, this.getSize());
	}

	/**
	 * Get the prototype of the EndTrigger.
	 *
	 * @return the prototype
	 */
	public static synchronized Inanimate getPrototype() {
		if (EndTrigger.instance == null) {
			EndTrigger.instance = new EndTrigger(new Vec(), new Vec(1.5f, GameConf.GRID_H));
		}
		return EndTrigger.instance;
	}

	@Override
	public void destroy() {
		// cannot be destroyed.
	}

	@Override
	public Integer getZHint() {
		return (int) Team.TRIGGER.zRange.max;
	}
}
