package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.GameTime;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;

/**
 *
 * This class represents an EndPortal.
 *
 */
public final class EndTrigger extends InanimateTrigger {
	/**
	 * The prototype-instance.
	 */
	private static EndTrigger instance;
	/**
	 * The number of portal-rings.
	 */
	private static final int PORTAL_NUM = 20;
	/**
	 * The inner portals.
	 */
	private Portal[] innerPortals;
	/**
	 * The particle spawner.
	 */
	private static ParticleSpawner portalParticles = null;

	static {
		EndTrigger.portalParticles = new ParticleSpawner();
		EndTrigger.portalParticles.colorA = new ParticleSpawnerOption(220, -100);
		EndTrigger.portalParticles.timeMin = 0.3f;
		EndTrigger.portalParticles.timeMax = 0.3f;
		EndTrigger.portalParticles.speed = new ParticleSpawnerOption(2, 2, 6, 6);
		EndTrigger.portalParticles.amountMin = 1;
		EndTrigger.portalParticles.amountMax = 1;
		EndTrigger.portalParticles.size = new ParticleSpawnerOption(1, -0.8f);
	}

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
		public Portal(Vec pos, Vec size, Vec amplitude, Vec frequency, Vec phase, RGBAColor color) {
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

			this.x += deltaTime;

			// some weird "amplitude * sin(phase + frequency * x)" action
			this.currentSize = this.getSize().add(this.amplitude.multiply(this.phase.add(this.frequency.scalar(this.x)).sin()));

			float randomAngle = (float) (GameConf.PRNG.nextDouble() * 2 * Math.PI);
			float r = this.currentSize.getX() * 2;

			float x = (float) (Math.cos(randomAngle) * r);
			float y = (float) (Math.sin(randomAngle) * r);

			y = (y / this.currentSize.getX()) * this.currentSize.getY();

			EndTrigger.portalParticles.angle = new ParticleSpawnerOption((float) (randomAngle - Math.PI / 2));
			EndTrigger.portalParticles.colorR = new ParticleSpawnerOption((float) (this.color.red * 1.2));
			EndTrigger.portalParticles.colorG = new ParticleSpawnerOption((float) (this.color.green * 1.2));
			EndTrigger.portalParticles.colorB = new ParticleSpawnerOption((float) (this.color.blue * 1.2));

			EndTrigger.portalParticles.spawn(EndTrigger.this.getScene(), new Vec(x, y).add(this.getPos()));

		}

		@Override
		public void internalRender(Field f) {
			f.drawCircle(this.getPos(), this.currentSize, this.color);
		}
	}

	@Override
	public void internalRender(Field f) {
		for (Portal portal : this.innerPortals) {
			portal.render(f);
		}
	}

	@Override
	public void perform() {
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
	public synchronized static Inanimate getPrototype() {
		if (EndTrigger.instance == null) {
			EndTrigger.instance = new EndTrigger(new Vec(), new Vec(1.5f, GameConf.GRID_H));
		}
		return EndTrigger.instance;
	}

}
