package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

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

		if (sides < 0 || sides > 14) {
			throw new IllegalArgumentException(
					"RektKiller must be give a number between 0 and 14");
		}

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
		
		RGB color = new RGB(200, 30, 30);
		RGB darkColor = new RGB (150, 20, 20);
		
		f.drawRectangle(pos, size, color);
		
		if (this.hasSide(Direction.UP)) {
			f.drawRectangle(pos.add(new Vec2D(0, 0)), size.setY(0.1f), darkColor);
		}
		if (this.hasSide(Direction.RIGHT)) {
			f.drawRectangle(pos.add(new Vec2D(size.getX() - 0.1f, 0)), size.setX(0.1f), darkColor);
		}
		if (this.hasSide(Direction.DOWN)) {
			f.drawRectangle(pos.add(new Vec2D(0, size.getY() - 0.1f)), size.setY(0.1f), darkColor);
		}
		if (this.hasSide(Direction.LEFT)) {
			f.drawRectangle(pos.add(new Vec2D(0, 0)), size.setX(0.1f), darkColor);
		}
	}

	public Vec2D getSize() {
		return new Vec2D(0.8f, 0.8f);
	}

	private Direction currentDirection = Direction.LEFT;

	@Override
	public void logicLoop(float deltaTime) {
		
		// We dont want this guy to fall
		this.setVel(this.getVel().setY(0));
		
		// Do usual entity logic
		super.logicLoop(deltaTime);
		
		// Walk in current direction
		InputCommand cmd = new WalkCommand(currentDirection);
		cmd.setEntity(this);
		cmd.apply();
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
				element.addPoints(40);

				// Let the player jump if he landed on top
				if (dir == Direction.DOWN) {
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

		if (dir == Direction.RIGHT || dir == Direction.LEFT) {
			this.currentDirection = dir.getOpposite();
		}

	}

}
