package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.levelcreator.BossRoom;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public abstract class Boss extends Entity {
	
	private BossRoom bossRoom;
	private GameElement target;
	private boolean isHarmless = false;
	
	public Boss(Vec2D startPos) {
		super(startPos);
	}

	public abstract String getName();
	
	public void setBossRoom(BossRoom bossRoom) {
		this.bossRoom = bossRoom;
	}
	
	protected BossRoom getBossRoom() {
		return this.bossRoom;
	}
	
	public void setTarget(GameElement target) {
		this.target = target;
	}

	protected GameElement getTarget() {
		return target;
	}

	protected boolean isHarmless() {
		return isHarmless;
	}

	protected void setHarmless(boolean isHarmless) {
		this.isHarmless = isHarmless;
	}
	
	public void destroy() {
		bossRoom.endBattle();
		setHarmless(true);
	}
}
