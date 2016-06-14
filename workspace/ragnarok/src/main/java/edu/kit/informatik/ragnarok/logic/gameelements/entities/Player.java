package edu.kit.informatik.ragnarok.logic.gameelements.entities;

import java.util.Random;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.Particle;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.ProgressDependency;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class Player extends Entity {

	private Vec2D startPos;
	
	public Player(Vec2D startPos) {
		super(startPos);
		this.startPos = startPos;
		this.init();
	}
	
	public void init() {
		this.setPos(this.startPos);
		this.lifes = c.playerLifes;
		this.points = 0;
		this.setTeam(0);
		this.currentDirection = Direction.RIGHT;
		this.setVel(new Vec2D(0, 0));
		this.deleteMe = false;
	}
	
	public Vec2D getSize() {
		return new Vec2D(0.8f, 0.8f);
	}
	
	
	private Direction currentDirection;
	@Override
	public void render(Field f) {
		
		// determine if direction needs to be changed
		if (this.getVel().getX() > 0) {
			this.currentDirection = Direction.RIGHT;
		} else if (this.getVel().getX() < 0) {
			this.currentDirection = Direction.LEFT;
		}
		
		// draw player background image
		f.drawImage(this.getPos(), this.getSize(), "resources/mrRekt_background.png");
		// draw player glasses image
		String src = this.currentDirection == Direction.RIGHT
				? "resources/mrRekt_glasses_right.png" // When facing right
				: "resources/mrRekt_glasses_left.png"; // When facing right
		f.drawImage(this.getPos().addY(-0.025f * this.getVel().getY()), this.getSize(), src);
		
	}
	
	@Override
	public void collidedWith(Frame collision, Direction dir) {
		super.collidedWith(collision, dir);
		if (dir == Direction.UP) {
			this.setVel(this.getVel().setY(c.playerBottomBoost));
		}
	}
	
	public void addDamage(int damage) {
		Random r = new Random();
		
		for (int i = 0; i < 15; i++) {
			
			float speed = r.nextFloat() -0.5f + 5f;
			float angle = r.nextFloat() * 2 * (float)Math.PI;
			
			
			Particle p = new Particle (
				
				new Polygon(new Vec2D(), new Vec2D[]{new Vec2D(0.2f, 0), new Vec2D(0.2f, 0.2f), new Vec2D(0, 0.2f), new Vec2D()}),
					this.getPos(),
					0.4f,
					new ProgressDependency(1, 1),
					new ProgressDependency(speed, speed),
					new ProgressDependency(angle, angle),
					
					new ProgressDependency(232, 232),
					new ProgressDependency(148, 148),
					new ProgressDependency(16, 16),
					new ProgressDependency(255, 0)
			);
			this.getGameModel().addGameElement(p);
		}
		
		// Do usual life logic
		super.addDamage(damage);
	}
	
	public int getZ() {
		return 10;
	}

}
