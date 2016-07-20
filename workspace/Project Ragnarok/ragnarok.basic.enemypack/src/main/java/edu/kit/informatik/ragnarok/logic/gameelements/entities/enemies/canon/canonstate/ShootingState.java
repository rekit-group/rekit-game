package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.Canon;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.StateMachine;
import edu.kit.informatik.ragnarok.primitives.time.Timer;

public class ShootingState extends State {
	
	private float angle;
	
	public ShootingState(float angle) {
		this.angle = angle;
	}
	
	@Override
	public void enter(StateMachine parent) {
		this.timer = new Timer(Canon.STATE_SHOOTING_DURATION);
	}
	
	public float getTargetAngle() {
		return angle;
	}
	
	@Override
	public State getNextState() {
		return new IdleState();
	}
	
}
