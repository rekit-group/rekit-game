package edu.kit.informatik.ragnarok.logic.gameelements.type;

import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.ReflectUtils;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

/**
 * This class is the parent class of all Pickups in the game.
 */
public abstract class Pickup extends Entity {
	/**
	 * Load all Pickups.
	 *
	 * @return a set of pickups
	 * @see LoadMe
	 */
	public static final Set<Pickup> getPickupPrototypes() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, Pickup.class);
	}

	/**
	 * Prototype constructor.
	 */
	protected Pickup() {
		super(Team.PICKUP);
	}

	/**
	 * Create a pickup.
	 *
	 * @param startPos
	 *            the start pos
	 * @param vel
	 *            the start velocity
	 * @param size
	 *            the size
	 */
	protected Pickup(Vec startPos, Vec vel, Vec size) {
		super(startPos, vel, size, Team.PICKUP);
	}

	@Override
	public abstract GameElement create(Vec startPos, String[] options);

	/**
	 * Template method that should be overwritten in concrete {@link PickUp
	 * PickUps} to add the action that is performed upon being collected by the
	 * Player.
	 *
	 * @param collector
	 *            the GameElement which collects this Pickup
	 */
	public abstract void perform(GameElement collector);

	@Override
	public final void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			this.perform(element);
		}
	}

	@Override
	protected void innerLogicLoop() {
		// no logic
	}
}
