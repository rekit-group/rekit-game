package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate.State;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.visitor.NoVisit;
import edu.kit.informatik.ragnarok.visitor.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.Visitable;

@LoadMe
@VisitInfo(res = "conf/canon", visit = true)
public class Canon extends Enemy implements Visitable, StateMachine {

	private static Vec SIZE;
	public static float STATE_IDLE_DURATION;
	public static float STATE_AIMING_DURATION;
	public static float STATE_SHOOTING_DURATION;
	
	public static RGBColor COLOR_BASE;
	public static RGBColor COLOR_CANON;
	
	@NoVisit
	private State currentState;
	
	public Canon(Vec pos) {
		super(pos, new Vec(), SIZE);
	}
	
	@Override
	public GameElement create(Vec startPos, String[] options) {
		return new Canon(startPos);
	}


	@Override
	public void setNextState(State next) {
		currentState.leave();
		currentState = next;
		currentState.enter(this);
	}
	
}
