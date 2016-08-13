package edu.kit.informatik.ragnarok.logic.gameelements.type;

import java.util.Set;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.core.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.ReflectUtils;

public abstract class Pickup extends Entity {
	public static final Set<Pickup> getPickupPrototypes() {
		return ReflectUtils.loadInstances(GameConf.SEARCH_PATH, Pickup.class);
	}

	protected Pickup() {
		super(Team.PICKUP);
	}

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
	 */
	public abstract void perform(GameElement collector);

	@Override
	public final void reactToCollision(GameElement element, Direction dir) {
		if (this.getTeam().isHostile(element.getTeam())) {
			this.perform(element);
		}
	}

	@Override
	public void logicLoop(float deltaTime) {
		// no logic
	}
}