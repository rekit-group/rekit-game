package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.Canon;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.StateMachine;
import edu.kit.informatik.ragnarok.primitives.time.Timer;

public class IdleState extends State {
	
	@Override
	public void enter(StateMachine parent) {
		this.timer = new Timer(Canon.STATE_AIMING_DURATION);
	}
	
	@Override
	public State getNextState() {
		return new AimingState(parent.getScene().getPlayer());
	}
	
}
