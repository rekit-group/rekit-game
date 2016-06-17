package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class Slurp extends Entity {
	
	private List<SlurpDurp> slurpDurps;

	public Slurp(Vec2D startPos) {
		super(startPos);
		
		Random r = new Random();
		float sizeX = this.getSize().getX();
		float sizeY = this.getSize().getX();
		
		this.slurpDurps = new ArrayList<SlurpDurp>();
		for (int i = 0; i < 15; i++) {
			// randomize position, and pulsing options
			float randX = r.nextFloat() * (sizeX) - (sizeX / 2.0f);
			float randY = r.nextFloat() * (sizeY) - (sizeY / 2.0f);
			float baseSize = 0.2f + r.nextFloat() / 2f;
			float frequency = r.nextFloat() * 10f;
			float amplitude = r.nextFloat() / 4.0f;
			float phase = (float) (r.nextFloat() * 2 * Math.PI);
			// add SlurpDurp to List
			this.slurpDurps.add(new SlurpDurp(this.getPos(), new Vec2D(randX, randY), baseSize, frequency, amplitude, phase));
		}
	}
	
	private Direction currentDirection = Direction.LEFT;
	
	private boolean hasWallContact = true;
	
	@Override
	public void logicLoop(float deltaTime) {
		
		// prevent Slurp from flying upwards to infinity and beyond
		if (!hasWallContact) {
			this.currentDirection = this.currentDirection.getNextAntiClockwise();
		}
		
		// calculate velocity (by currentDirection)
		if (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT) {
			this.setVel(new Vec2D(this.currentDirection.getVector().getX() * GameConf.slurpSpeed, this.currentDirection.getNextAntiClockwise().getVector().getY() * 3));
		} else {
			this.setVel(new Vec2D(this.currentDirection.getNextAntiClockwise().getVector().getX() * 3, this.currentDirection.getVector().getY() * GameConf.slurpSpeed));
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
		if (Math.random() >= (1.0 - GameConf.slurpPopOffsPerSec * deltaTime)) {
			// get and remove one SlurpDurp from list
			SlurpDurp poppedOf = this.slurpDurps.remove(0);
			
			// add this SlurpDurp as regular Entity to GameModel
			this.getGameModel().addGameElement(poppedOf);
		}
		
		// If every SlurpDurp has popped off
		if (this.slurpDurps.size() == 0) {
			this.getGameModel().removeGameElement(this);
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
	
	public void reactToCollision(GameElement element, Direction dir) {
		element.setVel(element.getVel().multiply(0.6f));
	}
	
	@Override
	public void render(Field f) {
		for (SlurpDurp slurpDurp : this.slurpDurps) {
			slurpDurp.render(f);
		}
	}

}
