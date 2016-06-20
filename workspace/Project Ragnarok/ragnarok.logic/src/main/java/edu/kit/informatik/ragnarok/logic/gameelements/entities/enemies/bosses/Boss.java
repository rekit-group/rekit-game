package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.levelcreator.BossRoom;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public interface Boss {
	public String getName();

	public void setPos(Vec2D vec2d);
	
	public void setBossRoom(BossRoom bossRoom);
	
	public void setTarget(GameElement target);
}
