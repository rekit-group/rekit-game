package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.RektKiller;
import edu.kit.informatik.ragnarok.logic.levelcreator.BossRoom;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class RektSmasher extends RektKiller implements Boss {

	private BossRoom bossRoom;
	private GameElement target;
	
	public RektSmasher(Vec2D startPos) {
		super(startPos, 1);
		this.setSize(new Vec2D(2.2f, 2.2f));
		this.prepare();
	}
	
	public void setBossRoom(BossRoom bossRoom) {
		this.bossRoom = bossRoom;
	}
	
	public void setTarget(GameElement target) {
		this.target = target;
	}
	
	public void destroy() {
		super.destroy();
		bossRoom.endBattle();
	}

	@Override
	public String getName() {
		return "RektSmasher";
	}
}
