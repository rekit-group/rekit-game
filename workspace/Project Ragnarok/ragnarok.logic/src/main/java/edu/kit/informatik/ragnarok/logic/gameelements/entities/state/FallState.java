package edu.kit.informatik.ragnarok.logic.gameelements.entities.state;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;

public class FallState extends EntityState {

	public FallState(Entity entity) {
		super(entity);
	}
	
	@Override
	public boolean canJump() {
		return false;
	}
	
	@Override
	public void floorCollision() {
		this.entity.setEntityState(new DefaultState(this.entity));
	}

}
