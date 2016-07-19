package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 *
 * As the class name already said; this Class realizes a moving box.<br>
 * The {@link Player} can jump on it to solve a level
 *
 */
@LoadMe
public class MovingBox extends DynamicInanimate {
	/**
	 * The movement speed
	 */
	private final static float SPEED = 0.3f;
	/**
	 * The first movement anchor
	 */
	private Vec a;
	/**
	 * The second movement anchor
	 */
	private Vec b;
	/**
	 * A timer for the direction change of the platform
	 */
	private TimeDependency timer;
	/**
	 * The current start of the platform
	 */
	private Vec currentStart;
	/**
	 * The target of the box
	 */
	private Vec relativeTarget;
	/**
	 * A dark color
	 */
	private RGBAColor darkCol;
	/**
	 * The relative points for the rocket (the engine of the platform)
	 */
	private Vec[] rocketPolygonRelPts;
	/**
	 * The X size divided by 16
	 */
	private float sizeX16;
	/**
	 * The particle spawner
	 */
	private static ParticleSpawner sparkParticles = null;
	static {
		MovingBox.sparkParticles = new ParticleSpawner();
		MovingBox.sparkParticles.angle = new ParticleSpawnerOption((float) Math.PI * 0.9f, (float) Math.PI * 1.1f, 0, 0);
		MovingBox.sparkParticles.colorR = new ParticleSpawnerOption(200, 230, 10, 25);
		MovingBox.sparkParticles.colorG = new ParticleSpawnerOption(200, 250, -140, -120);
		MovingBox.sparkParticles.colorB = new ParticleSpawnerOption(150, 200, -140, -120);
		MovingBox.sparkParticles.colorA = new ParticleSpawnerOption(230, 250, -120, -180);
		MovingBox.sparkParticles.timeMin = 0.1f;
		MovingBox.sparkParticles.amountMin = 1;
		MovingBox.sparkParticles.amountMax = 1;
		MovingBox.sparkParticles.speed = new ParticleSpawnerOption(3, 6, -1, 1);
	}

	/**
	 * Prototype Constructor
	 */
	public MovingBox() {
		super();
	}

	/**
	 * Create a moving box
	 *
	 * @param pos
	 *            the position
	 * @param dist
	 *            the distance of movement
	 * @param offset
	 *            indicates whether you want an offset of period/2
	 */
	protected MovingBox(Vec pos, int dist, boolean offset) {
		super(pos, new Vec(1, 0.6f), new RGBAColor(100, 100, 100, 255));

		// precalculate relative points for rocket polygon
		Vec s = this.getSize();
		this.rocketPolygonRelPts = new Vec[] { new Vec(s.getX() * (-2 / 16f), 0), new Vec(s.getX() * (-3 / 16f), s.getY() / 2f),
				new Vec(s.getX() * (1 / 16f), s.getY() / 2f), new Vec() };
		this.sizeX16 = this.getSize().getX() / 16;

		// set starting and ending point
		this.a = pos.addY(-dist);
		this.b = pos.addY(dist);

		// set current starting and ending point
		this.currentStart = this.a;
		this.relativeTarget = this.b.add(this.currentStart.scalar(-1));

		// calculate accent color
		this.darkCol = this.color.darken(0.8f);

		// initialize movement timer
		float period = dist / (2 * MovingBox.SPEED);
		this.timer = new TimeDependency(period);
		this.timer.removeTime(offset ? period / 2f : 0);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (element.getVel().getY() > 0 && dir != Direction.LEFT && dir != Direction.RIGHT) {
			super.reactToCollision(element, Direction.UP);
		}
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.timer.removeTime(deltaTime);
		this.setPos(this.currentStart.add(this.relativeTarget.scalar(this.timer.getProgress())));

		if (GameConf.PRNG.nextFloat() > 0.6f) {
			MovingBox.sparkParticles.spawn(this.getScene(), this.getPos().addX(-5.5f * this.sizeX16).addY(this.getSize().getY() / 3));
			MovingBox.sparkParticles.spawn(this.getScene(), this.getPos().addX(2.5f * this.sizeX16).addY(this.getSize().getY() / 3));
		}

		if (this.timer.timeUp()) {
			if (this.currentStart == this.a) {
				this.currentStart = this.b;
				this.relativeTarget = this.a.add(this.b.scalar(-1));
			} else {
				this.currentStart = this.a;
				this.relativeTarget = this.b.add(this.a.scalar(-1));
			}
			this.timer.reset();
		}
	}

	@Override
	public void internalRender(Field f) {
		f.drawRectangle(this.getPos().addY(this.getSize().getY() / -4f), this.getSize().scalar(1, 1 / 2f), this.color);

		f.drawPolygon(new Polygon(this.getPos().addX(-3 * this.sizeX16), this.rocketPolygonRelPts), this.darkCol, true);
		f.drawPolygon(new Polygon(this.getPos().addX(5 * this.sizeX16), this.rocketPolygonRelPts), this.darkCol, true);
	}

	@Override
	public MovingBox create(Vec startPos, String[] options) {
		int dist = 1;
		boolean offset = false;
		if (options.length >= 1 && options[0] != null && options[0].matches("(\\+|-)?[0-9]+")) {
			dist = Integer.parseInt(options[0]);
		}
		if (options.length >= 2 && options[1] != null && options[1].matches("(\\+|-)?[1]")) {
			offset = true;
		}
		MovingBox inst = new MovingBox(startPos, dist, offset);
		return inst;
	}
}
