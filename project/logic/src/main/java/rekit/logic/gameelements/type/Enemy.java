package rekit.logic.gameelements.type;

import java.util.Set;

import rekit.config.GameConf;
import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Entity;
import rekit.persistence.JarManager;
import rekit.primitives.geometry.Vec;
import rekit.util.ReflectUtils;
import rekit.util.ReflectUtils.LoadMe;

/**
 * This class is the parent class of all Enemies in the game.
 */
@Group
public abstract class Enemy extends Entity {
	/**
	 * Load all Enemies.
	 *
	 * @return a set of enemies
	 * @see LoadMe
	 */
	public static final Set<? extends GameElement> getPrototypes() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, JarManager.SYSLOADER, Enemy.class);
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
