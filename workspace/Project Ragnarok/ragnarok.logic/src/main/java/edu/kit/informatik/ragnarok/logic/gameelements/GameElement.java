package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public abstract class GameElement implements Collidable {

	protected boolean deleteMe;
	private Vec2D vel;
	private Vec2D pos;
	private Vec2D lastPos;
	private Scene scene;

	protected Vec2D size;

	protected Team team;

	protected GameElement(Team team) {
		this(team, Vec2D.create(1, 1));
	}

	protected GameElement(Team team, Vec2D size) {
		this.team = team;
		this.size = size;
		this.deleteMe = false;
		this.vel = new Vec2D();
	}

	public abstract void render(Field f);

	public void logicLoop(float deltaTime) {
		// Do nothing
		return;
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		// Do nothing
		return;
	}

	public void destroy() {
		this.deleteMe = true;
	}

	public Team getTeam() {
		return this.team;
	}

	public void setVel(Vec2D value) {
		this.vel = value;
	}

	public Vec2D getVel() {
		return this.vel;
	}

	public void setPos(Vec2D value) {
		if (this.pos == null) {
			this.setLastPos(value);
		} else {
			this.setLastPos(this.pos);
		}
		this.pos = value;
	}

	public Vec2D getPos() {
		return this.pos.clone();
	}

	protected void setLastPos(Vec2D value) {
		this.lastPos = value;
	}

	public Vec2D getLastPos() {
		return this.lastPos.clone();
	}

	public void setScene(Scene value) {
		this.scene = value;
	}

	public Scene getScene() {
		return this.scene;
	}

	public final Frame getCollisionFrame() {
		Vec2D v1 = this.getPos().add(this.size.multiply(-0.5f));
		Vec2D v2 = this.getPos().add(this.size.multiply(0.5f));
		return new Frame(v1, v2);
	}

	public int getZ() {
		return 0;
	}

	public boolean getDelete() {
		return this.deleteMe;
	}

	public final boolean canDelete(float cameraOffset) {
		return this.pos.getX() + this.size.getX() / 2 < cameraOffset;
	}

	public Vec2D getSize() {
		return this.size.clone();
	}

}
