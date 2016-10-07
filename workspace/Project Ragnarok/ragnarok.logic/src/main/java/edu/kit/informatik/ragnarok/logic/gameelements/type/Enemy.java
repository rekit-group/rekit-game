package edu.kit.informatik.ragnarok.logic.gameelements.type;

import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.ReflectUtils;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 * This class is the parent class of all Enemies in the game.
 */
public abstract class Enemy extends Entity {
	/**
	 * Load all Enemies.
	 *
	 * @return a set of enemies
	 * @see LoadMe
	 */
	public static final Set<Enemy> getEnemyPrototypes() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, Enemy.class);
	}

	/**
	 * Prototype Constructor.
	 */
	protected Enemy() {
		super(Team.ENEMY);
	}

	@Override
	public abstract GameElement create(Vec startPos, String[] options);

	/**
	 * Create an Enemy.
	 * 
	 * @param startPos
	 *            the start pos
	 * @param vel
	 *            the start velocity
	 * @param size
	 *            the size
	 */
	protected Enemy(Vec startPos, Vec vel, Vec size) {
		super(startPos, vel, size, Team.ENEMY);
	}

}
