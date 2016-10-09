package edu.kit.informatik.ragnarok.logic.gameelements.particles;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.primitives.geometry.Polygon;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.visitor.Visitable;
import edu.kit.informatik.ragnarok.visitor.annotations.NoVisit;
import edu.kit.informatik.ragnarok.visitor.annotations.SetParser;

/**
 *
 * This class will be used to manage the spawning of particles.<br>
 * <b>You have to set the parameters of the particles by the public Fields of
 * your ParticleSpawner Object</b>
 *
 */
public class ParticleSpawner implements Visitable {
	/**
	 * The particle prototype.
	 */
	@NoVisit
	private Particle particlePrototype = new Particle();

	/**
	 * The current polygon.
	 */
	public Polygon polygon;
	/**
	 * Red Channel.
	 */
	@SetParser(ParticleSpawnerOptionParser.class)
	public ParticleSpawnerOption colorR;
	/**
	 * Green Channel.
	 */
	@SetParser(ParticleSpawnerOptionParser.class)
	public ParticleSpawnerOption colorG;
	/**
	 * Blue Channel.
	 */
	@SetParser(ParticleSpawnerOptionParser.class)
	public ParticleSpawnerOption colorB;
	/**
	 * Alpha Channel.
	 */
	@SetParser(ParticleSpawnerOptionParser.class)
	public ParticleSpawnerOption colorA;
	/**
	 * The speed of the particle.
	 */
	@SetParser(ParticleSpawnerOptionParser.class)
	public ParticleSpawnerOption speed;
	/**
	 * The angle of the particle.
	 */
	@SetParser(ParticleSpawnerOptionParser.class)
	public ParticleSpawnerOption angle;

	/**
	 * The rotation of the particle.
	 */
	@SetParser(ParticleSpawnerOptionParser.class)
	public ParticleSpawnerOption rotation;

	/**
	 * The size of the particle.
	 */
	@SetParser(ParticleSpawnerOptionParser.class)
	public ParticleSpawnerOption size;
	/**
	 * The minimum amount of particles.
	 */
	public int amountMin;
	/**
	 * The maximum amount of particles.
	 */
	public int amountMax;
	/**
	 * The minimum lifetime.
	 */
	public float timeMin;
	/**
	 * The maximum lifetime.
	 */
	public float timeMax;

	/**
	 * Create a new ParticleSpawner by prototype.
	 *
	 * @param prototype
	 *            the prototype
	 */
	public ParticleSpawner(Particle prototype) {
		this();
		this.particlePrototype = prototype;
	}

	/**
	 * Create a new particle spawner with its default values.
	 */
	public ParticleSpawner() {

		// default polygon is square wit a = 0.2
		this.polygon = new Polygon(new Vec(), new Vec[] { new Vec(0.2f, 0), new Vec(0.2f, 0.2f), new Vec(0, 0.2f), new Vec() });

		// default color is fully opaque black
		this.colorR = new ParticleSpawnerOption(0, 0, 0, 0);
		this.colorG = new ParticleSpawnerOption(0, 0, 0, 0);
		this.colorB = new ParticleSpawnerOption(0, 0, 0, 0);
		this.colorA = new ParticleSpawnerOption(255, 255, 0, 0);

		// default size is factor 1 with no variation
		this.size = new ParticleSpawnerOption(1, 1, 0, 0);
		// default speed is factor 1 with no variation
		this.speed = new ParticleSpawnerOption(4, 5, -1, 1);
		// default angle is between 0 and 2PI
		this.angle = new ParticleSpawnerOption(0, (float) Math.PI * 2, 0, 0);

		this.rotation = new ParticleSpawnerOption(0);

		// some spawns between 8 and 12
		this.amountMin = 8;
		this.amountMax = 12;

		// animation takes between 0.3s and 0.5s
		this.timeMin = 0.4f;
		this.timeMax = 0.6f;
	}

	/**
	 * Spawn particles in a {@link IScene} at a position.
	 *
	 * @param scene
	 *            the scene
	 * @param pos
	 *            the position
	 */
	public void spawn(IScene scene, Vec pos) {
		int randomAmount = (int) (this.amountMin + GameConf.PRNG.nextDouble() * (this.amountMax - this.amountMin));

		for (int i = 0; i < randomAmount; i++) {
			// TODO All changed to millis
			long randomTime = (long) (1000 * (this.timeMin + GameConf.PRNG.nextDouble() * (this.timeMax - this.timeMin)));

			this.polygon.moveTo(pos);

			Particle p = this.particlePrototype.clone();
			p.setProperties(this.polygon, pos, randomTime, this.size.randomize(), this.speed.randomize(), this.rotation.randomize(),
					this.angle.randomize(), this.colorR.randomize(), this.colorG.randomize(), this.colorB.randomize(), this.colorA.randomize());

			scene.addGameElement(p);
		}

	}
}
