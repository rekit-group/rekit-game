package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.levelcreator.BossRoom;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class Boss extends Entity {

	private BossRoom bossRoom;
	private GameElement target;
	private boolean isHarmless = false;

	public Boss(Vec startPos) {
		super(Team.ENEMY, startPos);
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
		return this.target;
	}

	protected boolean isHarmless() {
		return this.isHarmless;
	}

	protected void setHarmless(boolean isHarmless) {
		this.isHarmless = isHarmless;
	}

	@Override
	public void destroy() {
		if (this.bossRoom != null) {
			this.bossRoom.endBattle();
		}
		this.setHarmless(true);
	}
}
