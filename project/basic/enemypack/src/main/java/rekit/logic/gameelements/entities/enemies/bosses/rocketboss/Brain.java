package rekit.logic.gameelements.entities.enemies.bosses.rocketboss;

import rekit.core.GameGrid;
import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.primitives.geometry.Vec;;

public class Brain extends GameElement {
	
	private Vec relPos;
	private RocketBoss parent;
	
	protected Brain(RocketBoss parent, Team team) {
		super(new Vec(), new Vec(), new Vec(), team);
		this.parent = parent;
		
		this.relPos = RocketBoss.BRAIN_SIZE.add(RocketBoss.HEAD_SIZE).scalar(-1/2f).setX(0);
		this.setSize(RocketBoss.BRAIN_SIZE);
		
		
	}
	
	@Override
	public void logicLoop() {
		this.setPos(this.parent.getPos().add(this.relPos));
	}

	
	public void internalRender(GameGrid f) {
		Vec absPos = this.parent.getPos().add(this.relPos);
		f.drawImage(absPos, this.getSize(), this.parent.getState().getBrainImgSrc());
	}

}
