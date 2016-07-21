package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.Cannon;
import edu.kit.informatik.ragnarok.primitives.time.Timer;

public class AimingState extends CannonState {

	private Player target;

	protected Timer timer = new Timer(Cannon.STATE_AIMING_DURATION);

	public AimingState(Player target) {
		this.target = target;
	}

	public float getTargetAngle() {
		return this.parentCannon.getPos().getAngleTo(this.target.getPos());
	}

	@Override
	public CannonState getNextState() {
		return new ChargingState(this.getTargetAngle());
	}

	@Override
	public float getTimerTime() {
		return Cannon.STATE_AIMING_DURATION;
	}

}
