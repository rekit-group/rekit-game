package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.Random;

import org.eclipse.swt.graphics.RGB;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.controller.commands.InputCommand;
import edu.kit.informatik.ragnarok.controller.commands.WalkCommand;
import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;

public class RektKiller extends Entity {

	private int sides;

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
		RGB spikeColor = new RGB (80, 80, 80);
		
		// draw rectangle in the middle
		f.drawRectangle(pos, size.multiply(0.8f), innerColor);
		
		// define start point path for spikes at top side
		Vec2D startPt = pos.add(size.multiply(-0.8f / 2f));
		Vec2D[] relPts = new Vec2D[] {
				new Vec2D(0.5f * ((size.getX() * 0.8f) / 3f), -(size.getY() * 0.8f) / 3f),
				new Vec2D(1.0f * ((size.getX() * 0.8f) / 3f), 0),
				new Vec2D(1.5f * ((size.getX() * 0.8f) / 3f), -(size.getY() * 0.8f) / 3f),
				new Vec2D(2.0f * ((size.getX() * 0.8f) / 3f), 0),
				new Vec2D(2.5f * ((size.getX() * 0.8f) / 3f), -(size.getY() * 0.8f) / 3f),
				new Vec2D(3.0f * ((size.getX() * 0.8f) / 3f), 0),
				new Vec2D() // and back to start
		};
		
		for (Direction d : Direction.values()) {
			if (this.hasSide(d)) {
				
				double angle = d.getAngle();
				//System.out.println(d.toString() + " ->" + angle);
				Vec2D relStartPoint = startPt.rotate(angle, pos);
				
				f.drawPolygon(
					relStartPoint, // Rotate start point relative to enemy center
					rotatePath(relPts, angle), // Rotate path relative to start point
					spikeColor
					);
			}
		}
		
	}
	
	private Vec2D[] rotatePath(Vec2D[] toTurn, double angle) {
		Vec2D[] result = new Vec2D[toTurn.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = toTurn[i].rotate(angle);
		}
		return result;
	}

	public Vec2D getSize() {
		return new Vec2D(0.6f, 0.6f);
	}

	private Direction currentDirection;

	@Override
	public void logicLoop(float deltaTime) {
		
		// Do usual entity logic
		super.logicLoop(deltaTime);
		
		if (this.getPos().getY() <= 0 || this.getPos().getY() >= c.gridH - 1) {
			currentDirection = currentDirection.getOpposite();
		}
		
		// We dont want this guy to fall
		this.setVel(this.currentDirection.getVector().multiply(c.playerWalkMaxSpeed));
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
					element.setVel(element.getVel().setY(c.playerJumpBoost));
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
