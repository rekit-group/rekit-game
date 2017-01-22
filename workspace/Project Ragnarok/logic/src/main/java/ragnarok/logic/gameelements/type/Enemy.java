package ragnarok.logic.gameelements.type;

import java.util.Set;

import ragnarok.config.GameConf;
import ragnarok.core.GameElement;
import ragnarok.core.Team;
import ragnarok.logic.gameelements.entities.Entity;
import ragnarok.primitives.geometry.Vec;
import ragnarok.util.ReflectUtils;
import ragnarok.util.ReflectUtils.LoadMe;

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
