package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.c;
import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class Rocket extends Entity {
	
	private static RGB innerColor = new RGB(90, 90, 90);
	private static RGB frontColor = new RGB(150, 30, 30);
	private static RGB outerColor = new RGB(50, 50, 50);
	
	private static ParticleSpawner sparkParticles = null;
	private static ParticleSpawner explosionParticles = null;
	
	public Rocket(Vec2D startPos) {
		super(startPos);
		
		// if this is first instantiated rocket: create spark Particle
		if (sparkParticles == null) {
			sparkParticles = new ParticleSpawner();
			
			sparkParticles.angle = new ParticleSpawnerOption((float) ((1/4f) * Math.PI), (float) ((3/4f) * Math.PI), 0, 0);;
			
			sparkParticles.colorR = new ParticleSpawnerOption(200, 230, 10, 25);
			sparkParticles.colorG = new ParticleSpawnerOption(200, 250, -140, -120);
			sparkParticles.colorB = new ParticleSpawnerOption(150, 200, -140, -120);
			sparkParticles.colorA = new ParticleSpawnerOption(230, 250, -150, -230);
			
			sparkParticles.timeMin = 0.1f;
			sparkParticles.timeMin = 0.1f;
			
			sparkParticles.amountMin = 1;
			sparkParticles.amountMax = 3;
			
			sparkParticles.speed = new ParticleSpawnerOption(3, 6, -1, 1);
		}
		
		// if this is first instantiated rocket: create explosion Particle
		if (explosionParticles == null) {
			explosionParticles = new ParticleSpawner();
			
			explosionParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), 0, 0);
			
			explosionParticles.colorR = new ParticleSpawnerOption(200, 230, 10, 25);
			explosionParticles.colorG = new ParticleSpawnerOption(200, 250, -130, -110);
			explosionParticles.colorB = new ParticleSpawnerOption(150, 200, -130, -110);
			explosionParticles.colorA = new ParticleSpawnerOption(230, 250, -120, -200);
			
			explosionParticles.timeMin = 0.1f;
			explosionParticles.timeMin = 0.2f;
			
			explosionParticles.amountMin = 40;
			explosionParticles.amountMax = 50;
			
			explosionParticles.speed = new ParticleSpawnerOption(4, 9, -1, 1);
		}
	}
	

	
	
	@Override
	public void render(Field f) {
		
		sparkParticles.spawn(this.getGameModel(), this.getPos().addX(this.getSize().getX()/2));
		
		// draw body
		f.drawRectangle(this.getPos(), this.getSize().multiply(0.8f, 0.6f), innerColor);
		
		// draw spike at front
		Vec2D startPt = this.getPos().addX(-this.getSize().multiply(0.5f).getX());
		Vec2D[] relPts = new Vec2D[] {
				new Vec2D(this.getSize().multiply(0.1f).getX(), -this.getSize().multiply(0.5f).getY()),
				new Vec2D(this.getSize().multiply(0.1f).getX(), this.getSize().multiply(0.5f).getY()),
				new Vec2D()
		};
		f.drawPolygon(new Polygon(startPt, relPts), frontColor);
		
		// draw stripes
		Vec2D stripeStart = this.getPos().addX(-this.getSize().multiply(0.4f - 0.05f - 0.025f).getX());
		for (int x = 0; x < 9; x++) {
			f.drawRectangle(
					stripeStart.addX(0.15f * x),
					this.getSize().multiply(0.05f, 0.75f), outerColor);
		}
		
		// draw drive at back
		startPt = this.getPos().addX(this.getSize().multiply(0.5f).getX()).addY(-this.getSize().multiply(0.5f).getY());
		relPts = new Vec2D[] {
				new Vec2D(0, this.getSize().getY()),
				new Vec2D(-this.getSize().getX() * 0.1f, this.getSize().getY() * 0.8f),
				new Vec2D(-this.getSize().getX() * 0.1f, this.getSize().getY() * 0.2f),
				new Vec2D()
		};
		f.drawPolygon(new Polygon(startPt, relPts), outerColor);
	}
	
	public Vec2D getSize() {
		return new Vec2D(1.8f, 0.5f);
	}

	
	@Override
	public void logicLoop(float deltaTime) {
		// move ahead with player max speed
		this.setPos(this.getPos().addX(-c.playerWalkMaxSpeed * deltaTime));
		
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.deleteMe) {
			return;
		}
		
		if (this.isHostile(element)) {
			
			if (dir == Direction.UP) {
				element.setVel(element.getVel().setY(c.playerJumpBoost));
				element.addPoints(20);
				
				// Kill the rocket itself
				this.addDamage(1);
			} else {
				// Give player damage
				element.addDamage(1);

				// Kill the rocket itself
				this.addDamage(1);
				
				explosionParticles.spawn(this.getGameModel(), this.getPos());
			}
		}
	}
	
	@Override
	public void collidedWith(Frame collision, Direction dir) {
		// Do nothing, pass right through everything
	}

	@Override
	public void addDamage(int damage) {
		this.destroy();
	}

}
