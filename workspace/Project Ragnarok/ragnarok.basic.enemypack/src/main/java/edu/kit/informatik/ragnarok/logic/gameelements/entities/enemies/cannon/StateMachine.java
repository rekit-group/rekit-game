package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.cannon.state.State;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

public interface StateMachine {
	public void setNextState(State next);
	
	public Scene getScene();
	
	public Vec getPos();
}
