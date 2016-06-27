package edu.kit.informatik.ragnarok.logic.gameelements;

import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class GameElement implements Collidable {

	protected boolean deleteMe;
	private Vec vel;
	private Vec pos;
	private Vec lastPos;
	private Scene scene;

	protected Vec size;

	protected Team team;

	protected GameElement(Team team) {
		this(team, Vec.create(1, 1));

	}

	protected GameElement(Team team, Vec size) {
		this.team = team;
		this.size = size;
		this.deleteMe = false;
		this.vel = new Vec();
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

	public void setVel(Vec value) {

		this.vel = value;
	}

	public Vec getVel() {
		return this.vel;
	}

	public void setPos(Vec value) {

		if (this.pos == null) {
			this.setLastPos(value);
		} else {
			this.setLastPos(this.pos);
		}
		this.pos = value;
	}

	public Vec getPos() {
		return this.pos.clone();
	}

	protected void setLastPos(Vec value) {
		this.lastPos = value;
	}

	public Vec getLastPos() {
		return this.lastPos.clone();
	}

	public void setScene(Scene value) {
		this.scene = value;
	}

	public Scene getScene() {
		return this.scene;
	}

	public final Frame getCollisionFrame() {
		Vec v1 = this.getPos().add(this.size.multiply(-0.5f));
		Vec v2 = this.getPos().add(this.size.multiply(0.5f));
		return new Frame(v1, v2);
	}

	public int getOrderZ() {
		return 0;
	}

	public boolean getDelete() {
		return this.deleteMe;
	}

	public final boolean canDelete(float cameraOffset) {
		return this.getPos().translate2D(cameraOffset).getX() + this.getSize().getX() / 2 < cameraOffset;
	}

	public Vec getSize() {
		return this.size.clone();
	}

}
