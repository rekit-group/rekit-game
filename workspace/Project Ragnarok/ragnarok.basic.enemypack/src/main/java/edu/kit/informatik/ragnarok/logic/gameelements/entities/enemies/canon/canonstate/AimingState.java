package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.Canon;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.StateMachine;
import edu.kit.informatik.ragnarok.primitives.time.Timer;

public class AimingState extends State {
	
	private Player target;
	
	public AimingState(Player target) {
		this.target = target;
	}
	
	@Override
	public void enter(StateMachine parent) {
		this.timer = new Timer(Canon.STATE_AIMING_DURATION);
	}
	
	public float getTargetAngle() {
		return this.parent.getPos().getAngleTo(this.target.getPos());
	}
	
	@Override
	public State getNextState() {
		return new ShootingState(this.getTargetAngle());
	}
	
}
