package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class SlurpDurp extends Entity {
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
		
		@Override
		public Vec2D getPos() {
			return this.parentPos.add(this.innerPos);
		}
		
		@Override
		public Vec2D getSize() {
			return new Vec2D(this.currentSize); 
		}
		
		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			if (this.isHostile(element)) {
				element.setVel(element.getVel().multiply(0.9f));
			}
		}
		
		public void setParentPos(Vec2D parentPos) {
			this.parentPos = parentPos.clone();
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