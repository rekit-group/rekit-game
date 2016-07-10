package edu.kit.informatik.ragnarok.logic.gameelements.type;

import java.util.Random;
import java.util.Set;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.util.ReflectUtils;
import edu.kit.informatik.ragnarok.primitives.Vec;

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

	protected static final Random PRNG = new Random();

	@Override
	public abstract GameElement create(Vec startPos, String[] options);

	protected Enemy(Vec startPos, Vec vel, Vec size) {
		super(startPos, vel, size, Team.ENEMY);
	}

}
