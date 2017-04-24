package rekit.logic.gameelements.entities.enemies.bosses.rocketboss;

import rekit.core.Team;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Player;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;;

public class Brain extends GameElement {

	private Vec relPos;
	private RocketBoss parent;

	protected Brain(RocketBoss parent, Team team) {
		super(new Vec(), new Vec(), new Vec(), team);
		this.parent = parent;

		this.relPos = RocketBoss.BRAIN_SIZE.add(RocketBoss.HEAD_SIZE).scalar(-1 / 2f).setX(0);
		this.setSize(RocketBoss.BRAIN_SIZE);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.parent.getTeam().isHostile(element.getTeam())) {
			this.parent.addDamage(1);
			// Let Player collide
			element.collidedWith(this.getCollisionFrame(), dir);

			// Bounce Player off
			element.setVel(new Vec(Player.KILL_BOOST, Player.KILL_BOOST));

			if (this.parent.getLives() == 0) {
				element.setVel(new Vec(0, 0));
			}

		}
	}

	@Override
	public void logicLoop() {
		this.setPos(this.parent.getPos().add(this.relPos));
	}

}
