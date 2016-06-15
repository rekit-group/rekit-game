package edu.kit.informatik.ragnarok.logic.gameelements.entities.particles;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.primitives.Polygon;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class ParticleSpawner {
	
	public Polygon polygon;

	public ParticleSpawnerOption colorR;
	public ParticleSpawnerOption colorG;
	public ParticleSpawnerOption colorB;
	public ParticleSpawnerOption colorA;
	
	public ParticleSpawnerOption size;
	public ParticleSpawnerOption speed;
	public ParticleSpawnerOption angle;
	
	public int amountMin;
	public int amountMax;
	
	public float timeMin;
	public float timeMax;

	public ParticleSpawner() {
		
		// use default values
		
		// default polygon is square wit a = 0.2
		this.polygon = new Polygon(new Vec2D(), new Vec2D[]{new Vec2D(0.2f, 0), new Vec2D(0.2f, 0.2f), new Vec2D(0, 0.2f), new Vec2D()});
		
		// default color is fully opaque black
		colorR = new ParticleSpawnerOption(0, 0, 0, 0);
		colorG = new ParticleSpawnerOption(0, 0, 0, 0);
		colorB = new ParticleSpawnerOption(0, 0, 0, 0);
		colorA = new ParticleSpawnerOption(255, 255, 0, 0);
		
		// default size is factor 1 with no variation
		size = new ParticleSpawnerOption(1, 1, 0, 0);
		// default speed is factor 1 with no variation
		speed = new ParticleSpawnerOption(4, 5, -1, 1);
		// default angle is between 0 and 2PI
		angle = new ParticleSpawnerOption(0, (float) Math.PI * 2, 0, 0);
		
		// some spawns between 8 and 12
		amountMin = 8;
		amountMax = 12;
		
		// animation takes between 0.3s and 0.5s
		timeMin = 0.4f;
		timeMax = 0.6f;
	}
	
	public void spawn(GameModel model, Vec2D pos) {
		
		int randomAmount = (int) (amountMin + Math.random() * (amountMax - amountMin));
		
		for (int i = 0; i < randomAmount; i++) {
			float randomTime = (float) (timeMin + Math.random() * (timeMax - timeMin));
			
			polygon.moveTo(pos);
			
			Particle p = new Particle(polygon, pos, randomTime,
					size.randomizeProgressDependency(), 
					speed.randomizeProgressDependency(), 
					angle.randomizeProgressDependency(), 
					colorR.randomizeProgressDependency(),
					colorG.randomizeProgressDependency(),
					colorB.randomizeProgressDependency(),
					colorA.randomizeProgressDependency());
			
			model.addGameElement(p);
		}
		
	}
}
