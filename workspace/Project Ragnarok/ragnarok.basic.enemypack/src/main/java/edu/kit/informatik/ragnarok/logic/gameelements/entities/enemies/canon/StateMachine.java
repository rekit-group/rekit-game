package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.canon.canonstate.State;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

public interface StateMachine {
	public void setNextState(State next);
	
	public Scene getScene();
	
	public Vec getPos();
}
