package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.slurp;

import java.util.ArrayList;
import java.util.List;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Frame;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.visitor.Visitable;
import edu.kit.informatik.ragnarok.visitor.annotations.NoVisit;
import edu.kit.informatik.ragnarok.visitor.annotations.VisitInfo;

/**
 * This class realizes a simple enemy that will not hurt the {@link Player} but
 * slows the player. This enemy consits of {@link SlurpDurp} (green bubbles)
 * which will slow down the {@link Player} at collision
 *
 * @author Dominik Fuchß
 *
 */
@LoadMe
@VisitInfo(res = "conf/slurp", visit = true)
public final class Slurp extends Enemy implements Visitable {
	/**
	 * The Slurp's SlurpDurps.
	 */
	@NoVisit
	private List<SlurpDurp> slurpDurps;
	/**
	 * The speed of the slurps.
	 */
	private static float SLURP_SPEED;
	/**
	 * The popoffs of slurp per second.
	 */
	private static float SLURP_POPOFFS_PER_SEC;
	/**
	 * The amount of {@link SlurpDurp}.
	 */
	private static int SLURP_DURP_AMOUNT;
	/**
	 * The factor one Slurp slows an enemy.
	 */
	private static float SLOWING_FACTOR;

	/**
	 * Prototype Constructor.
	 */
	public Slurp() {
		super();
	}

	/**
	 * Create a slurp by start position.
	 *
	 * @param startPos
	 *            the start position
	 */
	public Slurp(Vec startPos) {
		super(startPos, new Vec(), new Vec(1));

		float sizeX = this.getSize().getX();
		float sizeY = this.getSize().getX();

		this.slurpDurps = new ArrayList<>();
		for (int i = 0; i < Slurp.SLURP_DURP_AMOUNT; i++) {
			// randomize position, and pulsing options
			float randX = GameConf.PRNG.nextFloat() * (sizeX) - (sizeX / 2.0f);
			float randY = GameConf.PRNG.nextFloat() * (sizeY) - (sizeY / 2.0f);
			float baseSize = 0.2f + GameConf.PRNG.nextFloat() / 2f;
			float frequency = GameConf.PRNG.nextFloat() * 10f;
			float amplitude = GameConf.PRNG.nextFloat() / 4.0f;
			float phase = (float) (GameConf.PRNG.nextFloat() * 2 * Math.PI);
			// add SlurpDurp to List
			this.slurpDurps.add(new SlurpDurp(this.getPos(), new Vec(randX, randY), baseSize, frequency, amplitude, phase));
		}
	}

	/**
	 * The current direction of the Slurp.
	 */
	@NoVisit
	private Direction currentDirection = Direction.LEFT;

	/**
	 * This bool indicates whether the Slurp has contact to a wall.
	 */
	@NoVisit
	private boolean hasWallContact = true;

	@Override
	protected void innerLogicLoop() {

		// prevent Slurp from flying upwards to infinity and beyond
		if (!this.hasWallContact) {
			this.currentDirection = this.currentDirection.getNextAntiClockwise();
		}

		// calculate velocity (by currentDirection)
		if (this.currentDirection == Direction.LEFT || this.currentDirection == Direction.RIGHT) {
			this.setVel(new Vec(this.currentDirection.getVector().getX() * Slurp.SLURP_SPEED,
					this.currentDirection.getNextAntiClockwise().getVector().getY() * 3));
		} else {
			this.setVel(new Vec(this.currentDirection.getNextAntiClockwise().getVector().getX() * 3,
					this.currentDirection.getVector().getY() * Slurp.SLURP_SPEED));
		}

		super.innerLogicLoop();

		// Iterate all contained SlurpDurps
		for (SlurpDurp slurpDurp : this.slurpDurps) {
			// update new position SlurpPosition
			slurpDurp.setParentPos(this.getPos());

			// everyone need some logic
			slurpDurp.logicLoop();
		}

		// Randomly determine if SlurpDurp should pop off
		if (GameConf.PRNG.nextDouble() >= (1.0 - Slurp.SLURP_POPOFFS_PER_SEC * this.deltaTime / 1000.0)) {
			// get and remove one SlurpDurp from list
			SlurpDurp poppedOf = this.slurpDurps.remove(0);
			// add this SlurpDurp as regular Entity to GameModel
			this.getScene().addGameElement(poppedOf);
		}

		// If every SlurpDurp has popped off
		if (this.slurpDurps.size() == 0) {
			this.getScene().markForRemove(this);
		}

		this.hasWallContact = false;
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// If slurp collides against wall, orthogonal to direction
		if (this.currentDirection == dir.getNextAntiClockwise()) {
			// He sticks to a wall
			this.hasWallContact = true;
		}
		// If Slurp ran against wall
		if (this.currentDirection == Direction.getOpposite(dir)) {
			// Turn him
			this.currentDirection = this.currentDirection.getNextClockwise();
			this.hasWallContact = true;
		}
		super.collidedWith(collision, dir);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		element.setVel(element.getVel().scalar(Slurp.SLOWING_FACTOR));
	}

	@Override
	public void internalRender(Field f) {
		for (SlurpDurp slurpDurp : this.slurpDurps) {
			slurpDurp.internalRender(f);
		}
	}

	@Override
	public Entity create(Vec startPos, String[] options) {
		return new Slurp(startPos);
	}

}
