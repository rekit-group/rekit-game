package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.RektKiller;
import edu.kit.informatik.ragnarok.logic.levelcreator.BossRoom;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class RektSmasher extends RektKiller implements Boss {

	private BossRoom bossRoom;
	private GameElement target;
	
	
	public RektSmasher(Vec2D startPos) {
		super(startPos, 1);
		this.setSize(new Vec2D(2f, 2f));
		this.prepare();
		
		this.setLifes(5);
		
		this.sides = 15;
	}
	
	@Override
	public void render(Field f) {
		super.render(f);
		f.drawImage(this.getPos(), this.getSize().multiply(0.8f), "rektSmasher.png");
	}
	
	@Override
	public void collidedWith(Frame collision, final Direction dir) {
		
		Vec2D dif = this.getPos().add(target.getPos().multiply(-1));
			
		super.collidedWith(collision, dir);
		// Undo RektKillers direction mirroring
		this.currentDirection = this.currentDirection.getOpposite();
		
		Direction newDir;
		
		if (Math.abs(dif.getX()) > Math.abs(dif.getY())) {
			if (dif.getX() > 0) {
				newDir = Direction.LEFT;
			} else {
				newDir = Direction.RIGHT;
			}
		} else {
			if (dif.getY() > 0) {
				newDir = Direction.UP;
			} else {
				newDir = Direction.DOWN;
			}
		}
		
		while (this.currentDirection == newDir) {
			newDir = Direction.values()[(int) (Math.random() * Direction.values().length)];
		}
		this.currentDirection = newDir;
		
		if (Math.random() > 0.9) {
			this.setSide(dir.getOpposite(), false);
			Thread spikeRespawnThread = new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					RektSmasher.this.setSide(dir.getOpposite(), true);
				}
			};
			
			spikeRespawnThread.start();
		}
	}
	
	@Override
	public void reactToCollision(GameElement element, Direction dir) {
			
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		this.setVel(this.getVel().multiply(1/2f));
		super.logicLoop(deltaTime);
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
