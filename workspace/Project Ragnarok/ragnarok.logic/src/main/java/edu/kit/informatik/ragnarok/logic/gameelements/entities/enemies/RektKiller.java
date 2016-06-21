package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.Random;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class RektKiller extends Entity {

	private int sides;

	private Polygon spikePolygon;

	private Direction currentDirection;

	/**
	 * Prototype Constructor
	 */
	public RektKiller() {
		super(null);
	}

	public RektKiller(Vec2D startPos, int sides) {
		super(startPos);

		if (sides < 0 || sides > 15) {
			throw new IllegalArgumentException("RektKiller must be give a number between 0 and 14");
		}

		// save initial attributes
		Random r = new Random();
		int x = r.nextInt(Direction.values().length);
		this.setCurrentDirection(Direction.values()[x]);
		this.setSides(sides);

		this.setSize(new Vec2D(0.6f, 0.6f));
		this.prepare();
	}

	public void prepare() {
		// calculate size dependent Polygon for spikes
		this.spikePolygon = new Polygon(new Vec2D(),
				new Vec2D[] { new Vec2D(0.5f * ((this.getSize().getX() * 0.8f) / 3f), -(this.getSize().getY() * 0.8f) / 3f),
						new Vec2D(1.0f * ((this.getSize().getX() * 0.8f) / 3f), 0),
						new Vec2D(1.5f * ((this.getSize().getX() * 0.8f) / 3f), -(this.getSize().getY() * 0.8f) / 3f),
						new Vec2D(2.0f * ((this.getSize().getX() * 0.8f) / 3f), 0),
						new Vec2D(2.5f * ((this.getSize().getX() * 0.8f) / 3f), -(this.getSize().getY() * 0.8f) / 3f),
						new Vec2D(3.0f * ((this.getSize().getX() * 0.8f) / 3f), 0), new Vec2D() // and
																								// back
																								// to
																								// start
				});
	}

	public boolean hasSide(Direction dir) {
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
		return ((this.getSides() >> bitPos) & 1) == 1;
	}

	public void setSide(Direction dir, boolean spikes) {
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
		if (spikes) {
			this.setSides(this.getSides() | (1 << bitPos));
		} else {
			this.setSides(this.getSides() & ~(1 << bitPos));
		}

	}

	@Override
	public void render(Field f) {
		Vec2D pos = this.getPos();
		Vec2D size = this.getSize();
		RGBColor innerColor = new RGBColor(150, 30, 30);
		RGBColor spikeColor = new RGBColor(80, 80, 80);

		// draw rectangle in the middle
		f.drawRectangle(pos, size.multiply(0.8f), innerColor);

		// move to upper position
		this.spikePolygon.moveTo(pos.add(size.multiply(-0.8f / 2f)));

		for (Direction d : Direction.values()) {
			if (this.hasSide(d)) {
				double angle = d.getAngle();
				Polygon rotatedSpikes = this.spikePolygon.rotate((float) angle, pos);

				f.drawPolygon(rotatedSpikes, spikeColor);
			}
		}

	}

	@Override
	public void logicLoop(float deltaTime) {

		// Do usual entity logic
		super.logicLoop(deltaTime);

		if (this.getPos().getY() <= 0 || this.getPos().getY() >= GameConf.GRID_H - 1) {
			this.setCurrentDirection(this.getCurrentDirection().getOpposite());
		}

		// We dont want this guy to fall
		this.setVel(this.getCurrentDirection().getVector().multiply(GameConf.PLAYER_WALK_MAX_SPEED));
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.deleteMe) {
			return;
		}

		if (this.isHostile(element)) {
			// Touched harmless side
			if (!this.hasSide(dir)) {
				// give the player 40 points
				element.addPoints(20);

				// Let the player jump if he landed on top
				if (dir == Direction.UP) {
					element.setVel(element.getVel().setY(GameConf.PLAYER_JUMP_BOOST));
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

		this.setCurrentDirection(this.getCurrentDirection().getOpposite());

	}

	private static final Random PRNG = new Random();

	@Override
	public Entity create(Vec2D startPos) {
		return new RektKiller(startPos, RektKiller.PRNG.nextInt(16));
	}

	public Direction getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}

	public int getSides() {
		return sides;
	}

	public void setSides(int sides) {
		this.sides = sides;
	}

}
