package edu.kit.informatik.ragnarok.logic.gameelements.type;

import java.util.Set;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.ReflectUtils;

public abstract class Enemy extends Entity {
	public static final Set<Enemy> getEnemyPrototypes() {
		return ReflectUtils.get("edu.kit.informatik", Enemy.class);
	}

	/**
	 * Prototype Constructor
	 */
	protected Enemy() {
		super(Team.ENEMY);
	}

	@Override
	public abstract GameElement create(Vec startPos, String[] options);

	protected Enemy(Vec startPos, Vec vel, Vec size) {
		super(startPos, vel, size, Team.ENEMY);
	}

}
