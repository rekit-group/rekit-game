package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.Vec2D;

public abstract class Entity extends GameElement {
	private Vec2D vel;
	
	protected int lifes;
	protected int points;
	
	public void setVel(Vec2D value) {
		this.vel = value;
	}

	public Vec2D getVel() {
		return this.vel;
	}
	
	@Override
	public void addDamage(int damage) {
		this.lifes -= damage;
	}
	
	@Override
	public int getLifes() {
		return this.lifes;
	}
	
	@Override
	public void addPoints(int points) {
		this.points += points;
	}
	
	@Override
	public int getPoints() {
		return this.points;
	}
	public abstract void logicLoop(float deltaTime);

}
