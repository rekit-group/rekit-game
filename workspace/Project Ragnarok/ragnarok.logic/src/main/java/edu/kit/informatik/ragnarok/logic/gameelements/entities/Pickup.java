package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import java.util.Set;

import edu.kit.informatik.ragnarok.logic.util.ReflectUtils;
import edu.kit.informatik.ragnarok.primitives.Vec;

public abstract class Pickup extends Entity {
	public static final Set<Pickup> getBossPrototypes() {
		return ReflectUtils.get(Pickup.class);
	}

	protected Pickup() {
		super(null);
	}

	protected Pickup(Vec startPos) {
		super(startPos);
	}

	@Override
	public void logicLoop(float deltaTime) {
		// no logic
	}
}
