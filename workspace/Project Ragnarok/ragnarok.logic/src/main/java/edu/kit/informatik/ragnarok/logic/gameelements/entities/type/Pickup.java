package edu.kit.informatik.ragnarok.logic.gameelements.entities.type;

import java.util.Set;

import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.util.ReflectUtils;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class Pickup extends Entity {
	public static final Set<Pickup> getPickupPrototypes() {
		return ReflectUtils.get(Pickup.class);
	}

	protected Pickup() {
		super(Team.PICKUP);
	}

	protected Pickup(Vec startPos, Vec vel, Vec size) {
		super(startPos, vel, size, Team.PICKUP);
	}

	@Override
	public void logicLoop(float deltaTime) {
		// no logic
	}
}
