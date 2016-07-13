package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

/**
 *
 * This class realizes bubbles which will slow down the {@link Player}.<br>
 * These bubbles are part of {@link Slurp Slurps}
 *
 */
public class SlurpDurp extends Entity {
	/**
	 * The position of the parent {@link Slurp}
	 */
	private Vec parentPos;
	/**
	 * The inner position of this SlurpDurp
	 */
	private Vec innerPos;
	/**
	 * The frequency (pulsar)
	 */
	private float frequency;
	/**
	 * The base size of this SlurpDurp
	 */
	private float baseSize;
	/**
	 * The amplitude (pulsar)
	 */
	private float amplitude;
	/**
	 * The phase (pulsar)
	 */
	private float phase;
	/**
	 * The current X position
	 */
	private float currentX = 0;

	/**
	 * Prototype Constructor
	 */
	public SlurpDurp() {
		super(Team.ENEMY);
	}

	/**
	 * Create a slurp durp
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
		if (this.team.isHostile(element.getTeam())) {
			element.setVel(element.getVel().multiply(0.9f));
		}
	}

	/**
	 * Set the position of the parent {@link Slurp}
	 * 
	 * @param parentPos
	 *            the parent position
	 */
	public void setParentPos(Vec parentPos) {
		this.parentPos = parentPos.clone();
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.currentX += deltaTime;
		this.size = new Vec(this.baseSize + (float) (this.amplitude * Math.sin(this.currentX * this.frequency + this.phase)));

	}

	@Override
	public void internalRender(Field f) {
		f.drawCircle(this.parentPos.add(this.innerPos), this.size, new RGBColor(94, 233, 101));
	}

}