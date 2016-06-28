package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import java.util.Set;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.levelcreator.BossRoom;
import edu.kit.informatik.ragnarok.logic.util.ReflectUtils;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class Boss extends Entity {

	private BossRoom bossRoom;
	private GameElement target;
	private boolean isHarmless = false;

	public static final Set<Boss> getBossPrototypes() {
		return ReflectUtils.get(Boss.class);
	}

	protected Boss(Vec startPos) {
		super(startPos, Team.ENEMY);
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
		this.bossRoom.endBattle();
		this.setHarmless(true);
	}
}
