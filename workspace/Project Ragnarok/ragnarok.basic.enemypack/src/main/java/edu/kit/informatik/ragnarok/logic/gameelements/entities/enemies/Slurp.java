package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class Slurp extends Enemy {

	private List<SlurpDurp> slurpDurps;

	/**
	 * Prototype Constructor
	 */
	public Slurp() {
		super();
	}

	public Slurp(Vec startPos) {
		super(startPos, new Vec(), new Vec(1));

		float sizeX = this.getSize().getX();
		float sizeY = this.getSize().getX();

		this.slurpDurps = new ArrayList<>();
		for (int i = 0; i < 15; i++) {
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

	private Direction currentDirection = Direction.LEFT;

	private boolean hasWallContact = true;

	@Override
	public void logicLoop(float deltaTime) {

		// prevent Slurp from flying upwards to infinity and beyond
		if (!this.hasWallContact) {
			this.currentDirection = this.currentDirection.getNextAntiClockwise();
		}

		// calculate velocity (by currentDirection)
		if (this.currentDirection == Direction.LEFT || this.currentDirection == Direction.RIGHT) {
			this.setVel(new Vec(this.currentDirection.getVector().getX() * GameConf.SLURP_SPEED,
					this.currentDirection.getNextAntiClockwise().getVector().getY() * 3));
		} else {
			this.setVel(new Vec(this.currentDirection.getNextAntiClockwise().getVector().getX() * 3,
					this.currentDirection.getVector().getY() * GameConf.SLURP_SPEED));
		}

		super.logicLoop(deltaTime);

		// Iterate all contained SlurpDurps
		for (SlurpDurp slurpDurp : this.slurpDurps) {
			// update new position SlurpPosition
			slurpDurp.setParentPos(this.getPos());

			// everyone need some logic
			slurpDurp.logicLoop(deltaTime);
		}

		// Randomly determine if SlurpDurp should pop off
		if (GameConf.PRNG.nextDouble() >= (1.0 - GameConf.SLURP_POPOFFS_PER_SEC * deltaTime)) {
			// get and remove one SlurpDurp from list
			SlurpDurp poppedOf = this.slurpDurps.remove(0);

			// add this SlurpDurp as regular Entity to GameModel
			this.scene.addGameElement(poppedOf);
		}

		// If every SlurpDurp has popped off
		if (this.slurpDurps.size() == 0) {
			this.scene.removeGameElement(this);
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
		if (this.currentDirection == dir.getOpposite()) {
			// Turn him
			this.currentDirection = this.currentDirection.getNextClockwise();
			this.hasWallContact = true;
		}
		super.collidedWith(collision, dir);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		element.setVel(element.getVel().multiply(0.6f));
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
