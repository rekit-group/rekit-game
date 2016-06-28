package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;


import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class InanimateFloor extends Inanimate {

	private ParticleSpawner dustParticles;
	private static final ParticleSpawnerOption dustParticleAngleLeft = new ParticleSpawnerOption((float) ((7/4f) * Math.PI), (float) ((5/4f) * Math.PI), -(float) ((1/4f) * Math.PI), 0);
	private static final ParticleSpawnerOption dustParticleAngleRight = new ParticleSpawnerOption((float) ((1/4f) * Math.PI), (float) ((3/4f) * Math.PI), 0, (float) ((1/4f) * Math.PI));
	private static final ParticleSpawnerOption dustParticleAngleTop = new ParticleSpawnerOption((float) (-(1/2f) * Math.PI), (float) ((1/2f) * Math.PI), 0, 0);
	
	public InanimateFloor(Vec pos, Vec size, RGBColor color) {
		super(pos, size, color);
		
		dustParticles = new ParticleSpawner();
		dustParticles.colorR = new ParticleSpawnerOption(this.color.red);
		dustParticles.colorG = new ParticleSpawnerOption(this.color.green);
		dustParticles.colorB = new ParticleSpawnerOption(this.color.blue);
		dustParticles.colorA = new ParticleSpawnerOption(255);
		
		
		dustParticles.amountMin = 8;
		dustParticles.amountMax = 15;
		
		dustParticles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
	}
	
	@Override
	public void internalRender(Field f) {
		Vec pos = this.getPos();
		Vec size = this.getSize();
		
		f.drawRectangle(pos, size, this.color);
		
		RGBColor darkColor = new RGBColor(color.red - 30, color.green - 30, color.blue - 30);
		
		float plateThickness = 0.1f;
		f.drawRectangle(
				pos.add(new Vec(0, -size.getY() / 2f + plateThickness/2f)),
				size.setY(plateThickness), darkColor);
		
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		
		if (Math.abs(element.getVel().getY()) > 6) {
			dustParticles.angle = dustParticleAngleTop;
			dustParticles.spawn(this.getScene(), this.getPos().addY(-this.getSize().getY()/2));
		}
		// if strong velocity in x direction
		else if (Math.abs(element.getVel().getX()) > 5) {
			// if moving right
			if (element.getVel().getX() > 0) {
				dustParticles.angle = dustParticleAngleLeft;
			} else {
				dustParticles.angle = dustParticleAngleRight;
			}
			
			Vec pos = this.getPos().addY(-this.getSize().getY()/2).setX(element.getPos().getX());
			
			dustParticles.spawn(this.getScene(), pos);
		}
		
		super.reactToCollision(element, dir);
	}
	
}
