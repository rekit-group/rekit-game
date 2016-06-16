package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.gui.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Frame;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class Warper extends Entity {

	private TimeDependency warpAction = new TimeDependency(1);
	private ParticleSpawner warpParticles = null;
	
	public Warper(Vec2D startPos) {
		super(startPos);
		
		// if this is first instantiated warper: create explosion Particle
		if (warpParticles == null) {
			warpParticles = new ParticleSpawner();
			
			warpParticles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), (float) (2 * Math.PI), (float) (4 * Math.PI));
			
			warpParticles.colorR = new ParticleSpawnerOption(250, 0);
			warpParticles.colorG = new ParticleSpawnerOption(250, -250);
			warpParticles.colorB = new ParticleSpawnerOption(150);
			warpParticles.colorA = new ParticleSpawnerOption(220, -220);
			
			warpParticles.timeMin = 1f;
			warpParticles.timeMin = 1f;
			
			warpParticles.amountMin = 5;
			warpParticles.amountMax = 8;
			
			warpParticles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
		}
	}

	@Override
	public void render(Field f) {
		float progress = warpAction.getProgress();
		
		for (float i = 1; i >= 0.2; i-=0.1) {
			RGB innerColor = new RGB((int)(250 * i), (int)(250 * (1-progress)), (int)(150));
			
			// draw body
			f.drawCircle(this.getPos(), this.getSize().multiply(i), innerColor);
		}
		
	}
	
	public Vec2D getSize() {
		return new Vec2D(0.6f, 0.6f);
	}

	
	
	@Override
	public void logicLoop(float deltaTime) {
		// decrease time left
		warpAction.removeTime(deltaTime);
		
		// if time is up
		if (warpAction.timeUp()) {
			// reset time
			warpAction.reset();
			
			// get target (player)
			Vec2D target = this.getGameModel().getPlayer().getPos();
			
			// animate particles
			this.warpParticles.spawn(this.getGameModel(), this.getPos());
			
			// determine if x or y is greater in distance  
			Vec2D dif = this.getPos().add(target.multiply(-1)); 
			if (Math.abs(dif.getX()) > Math.abs(dif.getY())) {
				this.setPos(this.getPos().addX(-Math.signum(dif.getX())));
			} else {
				this.setPos(this.getPos().addY(-Math.signum(dif.getY())));
			}
		}
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		if (this.deleteMe) {
			return;
		}
		
		if (this.isHostile(element)) {
			
			
			// Give player damage
			element.addDamage(1);

			// Kill the warper itself
			this.addDamage(1);
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
