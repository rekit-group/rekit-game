package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses;

import edu.kit.informatik.ragnarok.config.GameConf;
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
	
	private boolean isHarmless = false;
	
	public RektSmasher(Vec2D startPos) {
		super(startPos, 1);
		this.setSize(new Vec2D(2f, 2f));
		this.prepare();
		this.currentDirection = Direction.DOWN;
		this.setLifes(3);
		
		this.sides = 15;
	}
	
	private float speed = 0.5f;
	
	@Override
	public void render(Field f) {
		super.render(f);
		int lifes = this.getLifes() > 3 ? 3 : this.getLifes(); 
		f.drawImage(this.getPos(), this.getSize().multiply(0.8f), "rektSmasher_" + lifes + ".png");		
	}
	
	@Override
	public void collidedWith(Frame collision, final Direction dir) {
		
		Vec2D dif = this.getPos().add(target.getPos().multiply(-1));
			
		super.collidedWith(collision, dir);
		// Undo RektKillers direction mirroring
		this.currentDirection = this.currentDirection.getOpposite();
		
		Direction newDir;
		
		//if (Math.abs(dif.getX()) / GameConf.gridW > Math.abs(dif.getY()) / GameConf.gridH) {
		if (dir == Direction.UP || dir == Direction.DOWN) {
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
		if (this.isHarmless) {
			return;
		}
		
		if (this.isHostile(element)) {
			// Touched harmless side
			if (!this.hasSide(dir)) {
				// Let the player jump if he landed on top
				if (dir == Direction.UP) {
					element.setVel(element.getVel().setY(GameConf.PLAYER_JUMP_BOOST));
				}

				// kill the enemy
				this.addDamage(1);
				
			}
			// Touched dangerous side
			else {
				// Give player damage
				element.addDamage(1);
			}
		}
	}
	
	@Override
	public void logicLoop(float deltaTime) {
		// if no invincibility of invincibility time is up
		if (this.invincibility == null || (this.invincibility != null && this.invincibility.timeUp())) {
			this.isHarmless = false;
			speed = 0.5f;
		}
		// if no invincibility or invincibility time is up
		if (this.invincibility != null && !this.invincibility.timeUp()) {
			this.isHarmless = true;
			speed = 1f;
		}
		if (this.getLifes() <= 0) {
			this.isHarmless = true;
		}
		this.setVel(this.getVel().multiply(speed));
		super.logicLoop(deltaTime);
	}
	
	public void setBossRoom(BossRoom bossRoom) {
		this.bossRoom = bossRoom;
	}
	
	public void setTarget(GameElement target) {
		this.target = target;
	}
	
	public void destroy() {
		bossRoom.endBattle();
		isHarmless = true;
		// super.destroy();
	}

	@Override
	public String getName() {
		return "RektSmasher";
	}
}
