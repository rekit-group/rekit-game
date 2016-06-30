package edu.kit.informatik.ragnarok.logic.gameelements.entities.type;

import java.util.Set;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.levelcreator.bossstructure.BossStructure;
import edu.kit.informatik.ragnarok.logic.util.ReflectUtils;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class Boss extends Entity {

	protected BossStructure bossStructure;
	protected GameElement target;
	protected boolean isHarmless = false;

	public static final Set<Boss> getBossPrototypes() {
		return ReflectUtils.get("edu.kit.informatik", Boss.class);
	}

	protected Boss() {
		super(Team.ENEMY);
	}

	protected Boss(Vec startPos, Vec vel, Vec size) {
		super(startPos, vel, size, Team.ENEMY);
	}

	public abstract String getName();

	public void setBossStructure(BossStructure bossStructure) {
		this.bossStructure = bossStructure;
	}

	public void setTarget(GameElement target) {
		this.target = target;
	}

	@Override
	public final void destroy() {
		this.bossStructure.endBattle(this.scene);
		this.isHarmless = true;
	}
}
