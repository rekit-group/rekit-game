package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.Random;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class RektKiller extends Entity {

	private int sides;

	private Polygon spikePolygon = new Polygon(new Vec2D(), new Vec2D[] {
			new Vec2D(0.5f * ((this.getSize().getX() * 0.8f) / 3f), -(this
					.getSize().getY() * 0.8f) / 3f),
			new Vec2D(1.0f * ((this.getSize().getX() * 0.8f) / 3f), 0),
			new Vec2D(1.5f * ((this.getSize().getX() * 0.8f) / 3f), -(this
					.getSize().getY() * 0.8f) / 3f),
			new Vec2D(2.0f * ((this.getSize().getX() * 0.8f) / 3f), 0),
			new Vec2D(2.5f * ((this.getSize().getX() * 0.8f) / 3f), -(this
					.getSize().getY() * 0.8f) / 3f),
			new Vec2D(3.0f * ((this.getSize().getX() * 0.8f) / 3f), 0),
			new Vec2D() // and back to start
			});

	public RektKiller(Vec2D startPos, int sides) {
		super(startPos);

		if (sides < 0 || sides > 15) {
			throw new IllegalArgumentException(
					"RektKiller must be give a number between 0 and 14");
		}

		Random r = new Random();
		int x = r.nextInt(Direction.values().length);
		this.currentDirection = Direction.values()[x];

		this.sides = sides;
	}

	private boolean hasSide(Direction dir) {
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
		return ((this.sides >> bitPos) & 1) == 1;
	}

	@Override
	public void render(Field f) {
		Vec2D pos = this.getPos();
		Vec2D size = this.getSize();

		RGB innerColor = new RGB(150, 30, 30);
		RGB spikeColor = new RGB(80, 80, 80);

		// draw rectangle in the middle
		f.drawRectangle(pos, size.multiply(0.8f), innerColor);

		// move to upper position
		this.spikePolygon.moveTo(pos.add(size.multiply(-0.8f / 2f)));

		for (Direction d : Direction.values()) {
			if (this.hasSide(d)) {
				double angle = d.getAngle();
				Polygon rotatedSpikes = spikePolygon.rotate((float) angle, pos);

				f.drawPolygon(rotatedSpikes, spikeColor);
			}
		}

	}

	public Vec2D getSize() {
		return new Vec2D(0.6f, 0.6f);
	}

	private Direction currentDirection;

	@Override
	public void logicLoop(float deltaTime) {

		// Do usual entity logic
		super.logicLoop(deltaTime);

		if (this.getPos().getY() <= 0 || this.getPos().getY() >= GameConf.gridH - 1) {
			currentDirection = currentDirection.getOpposite();
		}

		// We dont want this guy to fall
		this.setVel(this.currentDirection.getVector().multiply(
				GameConf.playerWalkMaxSpeed));
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.deleteMe) {
			return;
		}

		if (this.isHostile(element)) {
			System.out.println(dir.toString());
			// Touched harmless side
			if (!this.hasSide(dir)) {
				// give the player 40 points
				element.addPoints(20);

				// Let the player jump if he landed on top
				if (dir == Direction.UP) {
					element.setVel(element.getVel().setY(GameConf.playerJumpBoost));
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
	public void addDamage(int damage) {
		this.destroy();
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		super.collidedWith(collision, dir);

		this.currentDirection = currentDirection.getOpposite();

	}

}
