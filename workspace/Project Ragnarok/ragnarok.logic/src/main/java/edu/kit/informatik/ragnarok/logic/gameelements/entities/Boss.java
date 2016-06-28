package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import java.util.Set;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.levelcreator.BossRoom;
import edu.kit.informatik.ragnarok.logic.util.ReflectUtils;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class Boss extends Entity {

	protected BossRoom bossRoom;
	protected GameElement target;
	protected boolean isHarmless = false;

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

	public void setTarget(GameElement target) {
		this.target = target;
	}

	@Override
	public final void destroy() {
		this.bossRoom.endBattle();
		this.isHarmless = true;
	}
}
