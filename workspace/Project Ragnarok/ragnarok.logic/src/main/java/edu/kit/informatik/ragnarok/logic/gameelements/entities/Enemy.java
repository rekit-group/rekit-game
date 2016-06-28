package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import java.util.Set;

import edu.kit.informatik.ragnarok.logic.util.ReflectUtils;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class Enemy extends Entity {
	public static final Set<Enemy> getBossPrototypes() {
		return ReflectUtils.get(Enemy.class);
	}

	protected Enemy() {
		super(null);
	}

	protected Enemy(Vec startPos) {
		super(startPos);
	}

}
