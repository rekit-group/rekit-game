package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.Particle;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.primitives.operable.OpProgress;

/**
 * The standard Inanimate that will be used for the lowest height in the level.
 * By mechanics, it does not differ from the {@link InanimateBox} but is instead
 * designed to resemble grass.
 * 
 * @author Angelo Aracri
 */
public class InanimateFloor extends Inanimate {

	/**
	 * The amount of layers that the {@link InanimateFloor} should be composed
	 * of
	 */
	private static int LAYERS = 5;

	/**
	 * The amount of GrassStraws the InanimateFloor shall have.
	 */
	private static int STRAW_NUM = 20;

	/**
	 * Holds the colors for each layer the {@link InanimateFloor} is composed
	 * of.
	 */
	private static RGBColor[] layerCols;

	/**
	 * Holds the positions relative to the middle position for each layer the
	 * {@link InanimateFloor} is composed of.
	 */
	private static Vec[] layerVecs;

	/**
	 * Holds all GrassStraws that will be rendered on the upper half of the
	 * {@link InanimateFloor}.
	 */
	private GrassStraw[] straws;

	/**
	 * Cached version of the {@link ParticleSpawner} that will be used to show
	 * both {@link Particle Particles} when an {@link Entity} walks on the
	 * surface very fast and when an {@link Entity} lands on it very fast.
	 */
	private static ParticleSpawner dustParticles;

	/**
	 * Holds the angle for the {@link ParticleSpawner} that is used when an
	 * {@link Entity} walks fast to the right on the {@link InanimateFloor}.
	 */
	private static final ParticleSpawnerOption dustParticleAngleLeft;

	/**
	 * Holds the angle for the {@link ParticleSpawner} that is used when an
	 * {@link Entity} walks fast to the left on the {@link InanimateFloor}.
	 */
	private static final ParticleSpawnerOption dustParticleAngleRight;

	/**
	 * Holds the angle for the {@link ParticleSpawner} that is used when an
	 * {@link Entity} lands on the {@link InanimateFloor}.
	 */
	private static final ParticleSpawnerOption dustParticleAngleTop;

	static {
		// define color range for blocks layers
		RGBColor startCol = new RGBColor(22, 140, 30);
		RGBColor endCol = new RGBColor(23, 210, 49);

		// define position range for blocks layers
		float size = 1;
		Vec startVec = new Vec(0, -size / 2 + (size / 2) / LAYERS);
		Vec endVec = new Vec(0, size / 2 - (size / 2) / LAYERS);

		// create Progresses from ranges.
		OpProgress<RGBColor> progCols = new OpProgress<>(startCol, endCol);
		OpProgress<Vec> progVecs = new OpProgress<>(startVec, endVec);

		// fill arrays for each layer
		layerCols = new RGBColor[LAYERS];
		layerVecs = new Vec[LAYERS];
		for (int i = 0; i < LAYERS; i++) {
			float prog = i / (float) (LAYERS - 1);
			layerCols[i] = progCols.getNow(prog);
			layerVecs[i] = progVecs.getNow(prog);
		}

		// prepare particles
		dustParticles = new ParticleSpawner();
		dustParticles.colorR = new ParticleSpawnerOption(layerCols[0].red);
		dustParticles.colorG = new ParticleSpawnerOption(layerCols[0].green);
		dustParticles.colorB = new ParticleSpawnerOption(layerCols[0].blue);
		dustParticles.colorA = new ParticleSpawnerOption(255);
		dustParticleAngleLeft = new ParticleSpawnerOption((float) ((7 / 4f) * Math.PI), (float) ((5 / 4f) * Math.PI), -(float) ((1 / 4f) * Math.PI),
				0);
		dustParticleAngleRight = new ParticleSpawnerOption((float) ((1 / 4f) * Math.PI), (float) ((3 / 4f) * Math.PI), 0,
				(float) ((1 / 4f) * Math.PI));
		dustParticleAngleTop = new ParticleSpawnerOption((float) (-(1 / 2f) * Math.PI), (float) ((1 / 2f) * Math.PI), 0, 0);
		dustParticles.amountMin = 8;
		dustParticles.amountMax = 15;

		dustParticles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
	}

	/**
	 * <p>
	 * Standard constructor that requires position and size of the
	 * {@link InanimateFloor}.
	 * </p>
	 * <p>
	 * Also, it creates the {@link GrassStraw GrassStraws} that will be rendered
	 * on top of the {@link InanimateFloor}.
	 * </p>
	 * 
	 * @param pos
	 *            the position of the {@link InanimateFloor}.
	 * @param size
	 *            the size of the {@link InanimateFloor}.
	 */
	protected InanimateFloor(Vec pos, Vec size) {
		super(pos, size, layerCols[0].toRGBA());

		this.straws = new GrassStraw[STRAW_NUM];
		for (int i = 0; i < STRAW_NUM; i++) {
			this.straws[i] = new GrassStraw(pos);
		}
	}

	/**
	 * <p>
	 * Internal class whose only purpose is to represent a graphical element. It
	 * is supposed to look like a GrassStraw.
	 * </p>
	 * <p>
	 * It must be supplied with the parent position but alters the position as
	 * well as its size and color to create a random effect.
	 * </p>
	 * <p>
	 * In order to show the {@link GrassStraw} one must call
	 * {@link #internalRender(Field f)}.
	 * </p>
	 * 
	 * @author Angelo Aracri
	 */
	public class GrassStraw {
		/**
		 * The (absolute) position of the {@link GrassStraw}.
		 */
		private Vec pos;

		/**
		 * The color of the {@link GrassStraw}.
		 */
		private RGBColor col;

		/**
		 * The size of the {@link GrassStraw}
		 */
		private Vec size;

		/**
		 * Standard constructor that must be supplied with the position of the
		 * parenting {@link InanimateFloor} and performs all random calculations
		 * (positioning, size, color).
		 * 
		 * @param parentPos
		 *            the position of the parenting {@link InanimateFloor}.
		 */
		public GrassStraw(Vec parentPos) {
			this.pos = parentPos.add(new Vec(GameConf.PRNG.nextFloat() * 0.9f - 0.45f, -0.3f - GameConf.PRNG.nextFloat() * 0.4f));
			this.size = new Vec(0.06f, 0.3f + GameConf.PRNG.nextFloat() * 0.4f);
			this.col = layerCols[GameConf.PRNG.nextInt(layerCols.length)];
		}

		/**
		 * Render method to display the {@link GrassStraw}.
		 * 
		 * @param f
		 *            the {@link Field} to render upon.
		 */
		public void internalRender(Field f) {
			f.drawRectangle(this.pos, this.size, this.col);
		}
	}

	@Override
	public void internalRender(Field f) {
		// Draw rectangles that this Floor is composed of
		for (int i = 0; i < LAYERS; i++) {
			f.drawRectangle(this.getPos().add(layerVecs[i]), this.getSize().setY(1 / (float) LAYERS), layerCols[i]);
		}
		// Draw GrassStraws
		for (GrassStraw straw : this.straws) {
			straw.internalRender(f);
		}
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {

		if (Math.abs(element.getVel().getY()) > 6) {
			dustParticles.angle = InanimateFloor.dustParticleAngleTop;
			dustParticles.spawn(this.getScene(), this.getPos().addY(-this.getSize().getY() / 2));
		}
		// if strong velocity in x direction
		else if (Math.abs(element.getVel().getX()) > 5) {
			// if moving right
			if (element.getVel().getX() > 0) {
				dustParticles.angle = InanimateFloor.dustParticleAngleLeft;
			} else {
				dustParticles.angle = InanimateFloor.dustParticleAngleRight;
			}

			Vec pos = this.getPos().addY(-this.getSize().getY() / 2).setX(element.getPos().getX());

			dustParticles.spawn(this.getScene(), pos);
		}

		super.reactToCollision(element, dir);
	}

	/**
	 * Static create method that will be used by {@link Inanimate} for it in
	 * order to decide between {@link InanimateFloor} and {@link InanimateBox}.
	 * 
	 * @param pos
	 *            the position to create the {@link InanimateFloor} to.
	 * @return the newly created {@link InanimateFloor}.
	 */
	public static Inanimate staticCreate(Vec pos) {
		return new InanimateFloor(pos, new Vec(1, 1));
	}

	@Override
	protected int getOrderZ() {
		return 10;
	}

}
