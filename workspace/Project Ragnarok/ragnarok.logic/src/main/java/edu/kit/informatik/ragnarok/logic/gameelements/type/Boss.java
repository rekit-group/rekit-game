package edu.kit.informatik.ragnarok.logic.gameelements.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.level.bossstructure.BossStructure;
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

	public BossStructure getBossStructure() {
		List<String[]> struct = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			struct.add(new String[] { "edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate" });
		}

		BossStructure structure = new BossStructure(struct, this);
		System.err.println("Error while spawning Boss: " + this.getClass().getName() + " did not specify getBossStructure()");
		this.setBossStructure(structure);
		return structure;
	}

	@Override
	public abstract GameElement create(Vec startPos, String[] options);

	@Override
	public final void destroy() {
		this.bossStructure.endBattle(this.scene);
		this.isHarmless = true;
	}
}
