package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Frame;
import edu.kit.informatik.ragnarok.primitives.geometry.Polygon;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 * <p>
 * Enemy that moves either horizontal or vertical and reflects upon colliding.
 * On each side it has either spikes or not, specified by the first 4 bits in
 * the int <i>sides</i>.
 * </p>
 * <p>
 * It has one life and gets damaged when touched by the Player on a side without
 * spikes, while damaging the Player if he touches a side with spikes.
 * </p>
 *
 * @author Angelo Aracri
 * @version 1.0
 */
@LoadMe
public class RektKiller extends Enemy {

	/**
	 * Number whose first 4 bits are used as booleans for the spike at each
	 * side. First bit represents UP, the rest is clockwise.
	 */
	private int sides;

	/**
	 * Cache the Polygon of the visualization of spikes that will later on be
	 * rotated and rendered
	 */
	private Polygon spikePolygon;

	/**
	 * Holds the Direction the RektKiller is currently moving to.
	 */
	private Direction currentDirection;

	/**
	 * Prototype Constructor.
	 */
	public RektKiller() {
		super();
	}

	/**
	 * Standard Constructor that saves position, size and the integer that
	 * represents which sides have spikes.
	 *
	 * @param startPos
	 *            the initial position of the Enemy.
	 * @param size
	 *            the initial size of the Enemy.
	 * @param sides
	 *            the integer that represents which sides have sides.
	 */
	public RektKiller(Vec startPos, Vec size, int sides) {
		super(startPos, new Vec(), size);
		if (sides < 0 || sides > 15) {
			throw new IllegalArgumentException("RektKiller must be give a number between 0 and 14");
		}
		// save initial attributes
		int x = GameConf.PRNG.nextInt(Direction.values().length);
		this.setCurrentDirection(Direction.values()[x]);
		this.setSides(sides);

		this.prepare();
	}

	/**
	 * Alternative Constructor that uses the default size of (0.6, 0.6)
	 *
	 * @param startPos
	 *            the initial position of the Enemy
	 * @param sides
	 *            the integer that represents which sides have sides.
	 */
	public RektKiller(Vec startPos, int sides) {
		this(startPos, new Vec(0.6f, 0.6f), sides);
	}

	/**
	 * Calculates the Polygon for the size-dependent spikes and saves them in
	 * the attribute <i>spikePolygon</i>
	 */
	public void prepare() {
		// calculate size dependent Polygon for spikes
		this.spikePolygon = new Polygon(new Vec(),
				new Vec[] { //
						new Vec(0.5f * ((this.getSize().getX() * 0.8f) / 3f), -(this.getSize().getY() * 0.8f) / 3f),
						new Vec(1.0f * ((this.getSize().getX() * 0.8f) / 3f), 0),
						new Vec(1.5f * ((this.getSize().getX() * 0.8f) / 3f), -(this.getSize().getY() * 0.8f) / 3f),
						new Vec(2.0f * ((this.getSize().getX() * 0.8f) / 3f), 0),
						new Vec(2.5f * ((this.getSize().getX() * 0.8f) / 3f), -(this.getSize().getY() * 0.8f) / 3f),
						new Vec(3.0f * ((this.getSize().getX() * 0.8f) / 3f), 0), //
						new Vec() //
				});
	}

	/**
	 * Checks whether the RektKiller has spikes on a given side.
	 *
	 * @param dir
	 *            the {@link Direction} that is checked.
	 * @return true if the RektKiller has spikes at the side <i>dir</i>, false
	 *         otherwise.
	 */
	public boolean hasSide(Direction dir) {
		int bitPos = this.dirToInt(dir);
		return ((this.getSides() >> bitPos) & 1) == 1;
	}

	/**
	 * Sets if the RektKiller shall have spikes or not at a given side.
	 *
	 * @param dir
	 *            the {@link Direction} where to set/remove spikes to/from.
	 * @param spikes
	 *            true to set, false to remove spikes.
	 */
	public void setSide(Direction dir, boolean spikes) {
		int bitPos = this.dirToInt(dir);
		if (spikes) {
			this.setSides(this.getSides() | (1 << bitPos));
		} else {
			this.setSides(this.getSides() & ~(1 << bitPos));
		}
	}

	/**
	 * <p>
	 * Converts a given {@link Direction} to the corresponding int that will be
	 * used for determining the bit position of <i>sides</i>.
	 * </p>
	 * <p>
	 * 1 represents UP, the rest is clockwise.
	 * </p>
	 *
	 * @param dir
	 *            the {@link Direction} to convert.
	 * @return the int corresponding to the {@link Direction} <i>dir</i>.
	 */
	private int dirToInt(Direction dir) {
		int bitPos;
		switch (dir) {
		case UP:
			bitPos = 0;
			break;
		case RIGHT:
			bitPos = 1;
			break;
		case DOWN:
			bitPos = 2;
			break;
		default:
			bitPos = 3;
			break;
		}
		return bitPos;
	}

	@Override
	public void internalRender(Field f) {
		RGBColor innerColor = new RGBColor(150, 30, 30);
		RGBColor spikeColor = new RGBColor(80, 80, 80);
		// draw rectangle in the middle
		f.drawRectangle(this.getPos(), this.getSize().scalar(0.8f), innerColor);
		// move to upper position
		this.spikePolygon.moveTo(this.getPos().add(this.getSize().scalar(-0.8f / 2f)));
		for (Direction d : Direction.values()) {
			if (this.hasSide(d)) {
				double angle = d.getAngle();
				Polygon rotatedSpikes = this.spikePolygon.rotate((float) angle, this.getPos());
				f.drawPolygon(rotatedSpikes, spikeColor, true);
			}
		}

	}

	@Override
	public void logicLoop(float deltaTime) {
		// Do usual entity logic
		super.logicLoop(deltaTime);

		if (this.getPos().getY() <= 0) {
			this.collidedWith(new Frame(new Vec(0, 0), new Vec(0, 0)), Direction.DOWN);
		}
		if (this.getPos().getY() >= GameConf.GRID_H - 1) {
			this.collidedWith(new Frame(new Vec(0, GameConf.GRID_H - 1), new Vec(0, GameConf.GRID_H - 1)), Direction.UP);
		}

		// We dont want this guy to fall
		this.setVel(this.getCurrentDirection().getVector().scalar(GameConf.PLAYER_WALK_MAX_SPEED));
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			// Touched harmless side
			if (!this.hasSide(dir)) {
				// give the player 40 points
				element.addPoints(20);
				// Let the player jump if he landed on top
				if (dir == Direction.UP) {
					element.setVel(element.getVel().setY(GameConf.PLAYER_KILL_BOOST));
				}
				// kill the enemy
				this.addDamage(1);
			}
			// Touched dangerous side
			else {
				// Give player damage
				element.addDamage(1);
				// Kill the enemy itself
				this.addDamage(1);
			}
		}
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		super.collidedWith(collision, dir);
		this.setCurrentDirection(Direction.getOpposite(this.getCurrentDirection()));
	}

	@Override
	public Entity create(Vec startPos, String[] options) {
		RektKiller inst = new RektKiller(startPos, GameConf.PRNG.nextInt(16));

		// if option 0 is given: set defined direction
		if (options.length >= 1 && options[0] != null && options[0].matches("(\\+|-)?[0-9]+")) {
			int opt = Integer.parseInt(options[0]);
			if (opt >= 0 && opt < Direction.values().length) {
				inst.setCurrentDirection(Direction.values()[opt]);
			} else {
				System.err.println("Error, RektKiller was supplied invalid option " + options[0] + " at index 0 for Direction");
			}
		}

		// if option 1 is given: set defined sides
		if (options.length >= 2 && options[1] != null && options[1].matches("(\\+|-)?[0-9]+")) {
			inst.setSides(Integer.parseInt(options[1]));
		}

		return inst;
	}

	/**
	 * Getter for the current {@link Direction}, the RektKiller moves to.
	 *
	 * @return the current {@link Direction}, the RektKiller moves to.
	 */
	public Direction getCurrentDirection() {
		return this.currentDirection;
	}

	/**
	 * Setter for the current {@link Direction}, the RektKiller shall move to.
	 *
	 * @param currentDirection
	 *            the {@link Direction} the RektKiller shall move to.
	 */
	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}

	/**
	 * Getter for the number whose first 4 bits are used as booleans for the
	 * spikes at each side.
	 *
	 * @return the int <i>sides</i> that represents spike positions.
	 */
	public int getSides() {
		return this.sides;
	}

	/**
	 * Setter for the number whose first 4 bits are used as booleans for the
	 * spikes at each side.
	 *
	 * @param sides
	 *            the int <i>sides</i> that represents spike positions.
	 */
	public void setSides(int sides) {
		this.sides = sides;
	}

}
