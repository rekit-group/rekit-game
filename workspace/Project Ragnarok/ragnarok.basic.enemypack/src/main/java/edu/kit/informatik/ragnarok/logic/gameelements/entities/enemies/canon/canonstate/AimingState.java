package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.Canon;
import edu.kit.informatik.ragnarok.primitives.time.Timer;

public class AimingState extends State {
	
	private Player target;
	
	protected Timer timer = new Timer(Canon.STATE_AIMING_DURATION);
	
	public AimingState(Player target) {
		this.target = target;
	}
	
	public float getTargetAngle() {
		return this.parent.getPos().getAngleTo(this.target.getPos());
	}
	
	@Override
	public State getNextState() {
		return new ShootingState(this.getTargetAngle());
	}
	
	@Override
	public float getTimerTime() {
		return Canon.STATE_AIMING_DURATION;
	}
	
}
