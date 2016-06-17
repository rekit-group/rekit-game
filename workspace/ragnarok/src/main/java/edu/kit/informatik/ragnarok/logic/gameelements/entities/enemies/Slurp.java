package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class Slurp extends Entity {
	
	private List<SlurpDurp> slurpDurps;
	
	private class SlurpDurp extends Entity {
		private Vec2D pos;
		private float frequency;
		private float baseSize;
		private float amplitude;
		private float phase;
		
		private float currentX = 0;
		private float currentSize = 0;
		
		public SlurpDurp(Vec2D pos, float baseSize, float frequency, float amplitude, float phase) {
			super(pos);
			this.pos = pos;
			this.baseSize = baseSize;
			this.frequency = frequency;
			this.amplitude = amplitude;
			this.phase = phase;
		}

		@Override
		public void logicLoop(float deltaTime) {
			this.currentX += deltaTime;
			this.currentSize = this.baseSize + (float)(amplitude * Math.sin(this.currentX * frequency + phase));
		}
		
		@Override
		public void render(Field f) {
			f.drawCircle(pos, new Vec2D(this.currentSize), new RGB(94, 233, 101));
		}
	}
	
	public Slurp(Vec2D startPos) {
		super(startPos);
		
		Random r = new Random();
		float posX = this.getPos().getX();
		float posY = this.getPos().getY();
		float sizeX = this.getSize().getX();
		float sizeY = this.getSize().getX();
		
		this.slurpDurps = new ArrayList<SlurpDurp>();
		for (int i = 0; i < 15; i++) {
			// randomize position, and pulsing options
			float randX = posX - (sizeX / 2.0f) + r.nextFloat() * (sizeX);
			float randY = posY - (sizeY / 2.0f) + r.nextFloat() * (sizeY);
			float baseSize = 0.2f + r.nextFloat() / 2f;
			float frequency = r.nextFloat() * 10f;
			float amplitude = r.nextFloat() / 4.0f;
			float phase = (float) (r.nextFloat() * 2 * Math.PI);
			// add SlurpDurp to List
			this.slurpDurps.add(new SlurpDurp(new Vec2D(randX, randY), baseSize, frequency, amplitude, phase));
		}
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		for (SlurpDurp slurpDurp : this.slurpDurps) {
			slurpDurp.logicLoop(deltaTime);
		}
	}
	
	@Override
	public void render(Field f) {
		for (SlurpDurp slurpDurp : this.slurpDurps) {
			slurpDurp.render(f);
		}
	}

}
