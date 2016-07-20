package edu.kit.informatik.ragnarok.logic.gameelements.type;

import java.util.Set;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.level.bossstructure.BossStructure;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.ReflectUtils;

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
		String[][] struct = new String[9][];
		for (int i = 0; i < struct.length; i++) {
			struct[i] = new String[] { Inanimate.class.getName() };
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
		this.bossStructure.endBattle(this.getScene());
		this.isHarmless = true;
	}
}
