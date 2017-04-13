package rekit.logic.gameelements.entities.enemies.slurp;

import rekit.core.GameGrid;
import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gameelements.entities.Player;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.operable.OpProgress;
import rekit.util.Math;

/**
 *
 * This class realizes bubbles which will slow down the {@link Player}.<br>
 * These bubbles are part of {@link Slurp Slurps}
 *
 */
public final class SlurpDurp extends Entity {
	/**
	 * The position of the parent {@link Slurp}.
	 */
	private Vec parentPos;
	/**
	 * The inner position of this SlurpDurp.
	 */
	private Vec innerPos;
	/**
	 * The frequency (pulsar).
	 */
	private float frequency;
	/**
	 * The base size of this SlurpDurp.
	 */
	private float baseSize;
	/**
	 * The amplitude (pulsar).
	 */
	private float amplitude;
	/**
	 * The phase (pulsar).
	 */
	private float phase;
	/**
	 * The current X position.
	 */
	private float currentX = 0;
	/**
	 * The resolution factor of the slups.
	 */
	private static final int ITERATIONS = 7;
	/**
	 * The components of a slurp ({@link #ITERATIONS}).
	 */
	private static SlurpDurpVisComp[] circles;

	static {
		OpProgress<RGBAColor> col = new OpProgress<>(new RGBAColor(94, 233, 101), new RGBAColor(184, 255, 201));
		OpProgress<Vec> relPos = new OpProgress<>(new Vec(0), new Vec(-0.45f));
		OpProgress<Vec> relSize = new OpProgress<>(new Vec(1), new Vec(0.1f));

		SlurpDurp.circles = new SlurpDurpVisComp[SlurpDurp.ITERATIONS];

		for (int i = 0; i < SlurpDurp.ITERATIONS; ++i) {
			float progress = i / (float) SlurpDurp.ITERATIONS;
			SlurpDurp.circles[i] = new SlurpDurpVisComp(relPos.getNow(progress), relSize.getNow(progress), col.getNow(progress));
		}
	}

	/**
	 * Prototype Constructor.
	 */
	public SlurpDurp() {
		super(Team.ENEMY);
	}

	/**
	 * Create a slurp durp.
	 *
	 * @param parentPos
	 *            the parent Position
	 * @param innerPos
	 *            the inner position
	 * @param baseSize
	 *            the base size
	 * @param frequency
	 *            the frequency
	 * @param amplitude
	 *            the amplitude
	 * @param phase
	 *            the phase
	 */
	public SlurpDurp(Vec parentPos, Vec innerPos, float baseSize, float frequency, float amplitude, float phase) {
		super(parentPos.add(innerPos), new Vec(), new Vec(), Team.ENEMY);
		this.parentPos = parentPos;
		this.innerPos = innerPos;
		this.baseSize = baseSize;
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.phase = phase;
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			element.setVel(element.getVel().scalar(0.9f));
		}
	}

	/**
	 * Set the position of the parent {@link Slurp}.
	 *
	 * @param parentPos
	 *            the parent position
	 */
	public void setParentPos(Vec parentPos) {
		this.parentPos = parentPos.clone();
	}

	@Override
	protected void innerLogicLoop() {
		this.currentX += this.deltaTime / 1000F;
		this.setSize(new Vec(this.baseSize + (float) (this.amplitude * Math.sin(this.currentX * this.frequency + this.phase))));
		this.setPos(this.parentPos.add(this.innerPos));
	}

	@Override
	public void internalRender(GameGrid f) {
		for (SlurpDurpVisComp vis : SlurpDurp.circles) {
			vis.render(f, this.parentPos.add(this.innerPos), this.getSize());
		}
		// f.drawCircle(this.parentPos.add(this.innerPos), this.getSize(), new
		// (94, 233, 101));
	}

}