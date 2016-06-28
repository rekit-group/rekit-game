package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class SlurpDurp extends Entity {
	private Vec parentPos;
	private Vec innerPos;
	private float frequency;
	private float baseSize;
	private float amplitude;
	private float phase;

	private float currentX = 0;
	private float currentSize = 0;

	/**
	 * Prototype Constructor
	 */
	public SlurpDurp() {
		super(null);
	}

	public SlurpDurp(Vec parentPos, Vec innerPos, float baseSize, float frequency, float amplitude, float phase) {
		super(parentPos.add(innerPos));
		this.parentPos = parentPos;
		this.innerPos = innerPos;
		this.baseSize = baseSize;
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.phase = phase;
	}

	@Override
	public Vec getPos() {
		return this.parentPos.add(this.innerPos);
	}

	@Override
	public Vec getSize() {
		return new Vec(this.currentSize);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.isHostile(element)) {
			element.setVel(element.getVel().multiply(0.9f));
		}
	}

	public void setParentPos(Vec parentPos) {
		this.parentPos = parentPos.clone();
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.currentX += deltaTime;
		this.currentSize = this.baseSize + (float) (this.amplitude * Math.sin(this.currentX * this.frequency + this.phase));
	}

	@Override
	public void render(Field f) {
		f.drawCircle(this.getPos(), new Vec(this.currentSize), new RGBColor(94, 233, 101));
	}

	@Override
	public Entity create(Vec startPos) {
		throw new UnsupportedOperationException("Create not supported for SlurpDurps");
	}

}