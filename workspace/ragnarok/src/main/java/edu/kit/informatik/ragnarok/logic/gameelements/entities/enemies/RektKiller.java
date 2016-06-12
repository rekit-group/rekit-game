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
	
	public RektKiller(Vec2D startPos) {
		super(startPos);
	}

	@Override
	public void render(Field f) {
		f.drawCircle(this.getPos(), this.getSize(), new RGB(50, 200, 50));
	}
	
	public Vec2D getSize() {
		return new Vec2D(0.8f, 0.8f);
	}
	
	private Direction currentDirection = Direction.RIGHT;
	
	private boolean hasCollidedWithBottom;
	
	@Override
	public void logicLoop(float deltaTime) {
		// Use this flag to check if enemy touches ground
		hasCollidedWithBottom = false;
		
		// Do usual entity logic
		super.logicLoop(deltaTime);
		
		// If he does touch the ground..
		//System.out.println(this.getPos().getY()  + " " + this.getLastPos().getY());
		
		/*
		if (!hasCollidedWithBottom) {
			// ...He probably fell off the cliffs and we dont want this
			
			// So reset his position
			this.setPos(this.getLastPos());
			// And turn around
			this.setVel(this.getVel().setX(0));
			this.currentDirection = currentDirection.getOpposite();
		}
		*/
		
		// Walk in current direction
		InputCommand cmd = new WalkCommand(currentDirection);
		cmd.setEntity(this);
		cmd.apply();		
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		
		if (this.isHostile(element)) {
			
			// Jumped on top of enemy
			if (dir == Direction.DOWN) {
				// give the player 40 points
				element.addPoints(40);
				
				// Let the player jump
				element.setVel(element.getVel().setY(c.playerJumpBoost));
				
				// kill the enemy
				this.addDamage(1);
			}
			// Touched Enemy on side of from bottom
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
	public void addPoints(int points) {
		// Do nothing, blocks cannot get points
	}
	
	@Override
	public int getPoints() {
		return 0;
	}

	@Override
	public void collidedWith(Frame collision, Direction dir) {
		super.collidedWith(collision, dir);
		if (dir == Direction.DOWN) {
			hasCollidedWithBottom = true;
		}
		
		if (dir == Direction.RIGHT || dir == Direction.LEFT) {
			this.currentDirection = dir.getOpposite();
		}
		
	}

}
