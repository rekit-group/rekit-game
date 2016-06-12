package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.Frame;
import edu.kit.informatik.ragnarok.logic.Vec2D;
import edu.kit.informatik.ragnarok.logic.gameelements.Entity;

public class Player extends Entity {

	public Player(Vec2D startPos) {
		super(startPos);

		this.lifes = c.playerLifes;
		this.setTeam(0);
	}

	
	
	public Vec2D getSize() {
		return new Vec2D(0.8f, 0.8f);
	}
	
	
	private Direction currentDirection = Direction.RIGHT;
	@Override
	public void render(Field f) {
		
		// determine if direction needs to be changed
		if (this.getVel().getX() > 0) {
			this.currentDirection = Direction.RIGHT;
		} else if (this.getVel().getX() < 0) {
			this.currentDirection = Direction.LEFT;
		}
		
		// draw player image
		f.drawImage(this.getPos(), this.getSize(),
				this.currentDirection == Direction.RIGHT
				? "resources/mrRekt_right.png" // When facing right
				: "resources/mrRekt_left.png" // When facing right
		);
		
		
	}
	
	@Override
	public void collidedWith(Frame collision, Direction dir) {
		super.collidedWith(collision, dir);
		if (dir == Direction.UP) {
			this.setVel(this.getVel().setY(c.playerBottomBoost));
		}
	}

}
