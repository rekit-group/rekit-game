package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class Slurp extends Entity {
	
	private List<SlurpDurp> slurpDurps;
	
	private class SlurpDurp extends Entity {
		private Vec2D parentPos;
		private Vec2D innerPos;
		private float frequency;
		private float baseSize;
		private float amplitude;
		private float phase;
		
		private float currentX = 0;
		private float currentSize = 0;
		
		public SlurpDurp(Vec2D parentPos, Vec2D innerPos, float baseSize, float frequency, float amplitude, float phase) {
			super(parentPos.add(innerPos));
			this.parentPos = parentPos;
			this.innerPos = innerPos;
			this.baseSize = baseSize;
			this.frequency = frequency;
			this.amplitude = amplitude;
			this.phase = phase;
		}
		
		public Vec2D getPos() {
			return this.parentPos.add(this.innerPos);
		}
		
		public void setParentPos(Vec2D parentPos) {
			this.parentPos = parentPos;
		}

		@Override
		public void logicLoop(float deltaTime) {
			this.currentX += deltaTime;
			this.currentSize = this.baseSize + (float)(amplitude * Math.sin(this.currentX * frequency + phase));
		}
		
		@Override
		public void render(Field f) {
			f.drawCircle(this.getPos(), new Vec2D(this.currentSize), new RGB(94, 233, 101));
		}
	}

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
			System.out.println("no contact -> " + this.currentDirection);
		}
		
		// calculate velocity (by currentDirection)
		if (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT) {
			this.setVel(new Vec2D(this.currentDirection.getVector().getX() * GameConf.slurpSpeed, this.currentDirection.getNextAntiClockwise().getVector().getY() * 3));
		} else {
			this.setVel(new Vec2D(this.currentDirection.getNextAntiClockwise().getVector().getX() * 3, this.currentDirection.getVector().getY() * GameConf.slurpSpeed));
		}
		
		super.logicLoop(deltaTime);
		
		for (SlurpDurp slurpDurp : this.slurpDurps) {
			slurpDurp.setParentPos(this.getPos());
			slurpDurp.logicLoop(deltaTime);
		}
		
		this.hasWallContact = false;
	}
	
	@Override
	public void collidedWith(Frame collision, Direction dir) {
		if (this.currentDirection == dir.getNextAntiClockwise()) {
			this.hasWallContact = true;
		}
		if (this.currentDirection == dir.getOpposite()) {
			this.currentDirection = this.currentDirection.getNextClockwise();
			this.hasWallContact = true;
		}
		System.out.println(dir + " -> " + this.currentDirection);
		super.collidedWith(collision, dir);
	}
	
	@Override
	public void render(Field f) {
		for (SlurpDurp slurpDurp : this.slurpDurps) {
			slurpDurp.render(f);
		}
	}

}
