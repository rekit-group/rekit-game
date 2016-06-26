package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBColor;

public class InanimateFloor extends Inanimate {

	private ParticleSpawner dustParticles;
	private static final ParticleSpawnerOption dustParticleAngleLeft = new ParticleSpawnerOption((float) ((7 / 4f) * Math.PI),
			(float) ((5 / 4f) * Math.PI), -(float) ((1 / 4f) * Math.PI), 0);
	private static final ParticleSpawnerOption dustParticleAngleRight = new ParticleSpawnerOption((float) ((1 / 4f) * Math.PI),
			(float) ((3 / 4f) * Math.PI), 0, (float) ((1 / 4f) * Math.PI));
	private static final ParticleSpawnerOption dustParticleAngleTop = new ParticleSpawnerOption((float) (-(1 / 2f) * Math.PI),
			(float) ((1 / 2f) * Math.PI), 0, 0);

	public InanimateFloor(Vec2D pos, Vec2D size, RGBColor color) {
		super(pos, size, color);

		this.dustParticles = new ParticleSpawner();
		this.dustParticles.colorR = new ParticleSpawnerOption(this.color.red);
		this.dustParticles.colorG = new ParticleSpawnerOption(this.color.green);
		this.dustParticles.colorB = new ParticleSpawnerOption(this.color.blue);
		this.dustParticles.colorA = new ParticleSpawnerOption(255);

		this.dustParticles.amountMin = 8;
		this.dustParticles.amountMax = 15;

		this.dustParticles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
	}

	@Override
	public void render(Field f) {
		Vec2D pos = this.getPos();
		f.drawRectangle(pos, this.size, this.color);
		RGBColor darkColor = new RGBColor(this.color.red - 30, this.color.green - 30, this.color.blue - 30);
		float plateThickness = 0.1f;
		f.drawRectangle(pos.add(new Vec2D(0, -this.size.getY() / 2f + plateThickness / 2f)), this.size.setY(plateThickness), darkColor);

	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {

		if (Math.abs(element.getVel().getY()) > 6) {
			this.dustParticles.angle = InanimateFloor.dustParticleAngleTop;
			this.dustParticles.spawn(this.getScene(), this.getPos().addY(-this.size.getY() / 2));
		}
		// if strong velocity in x direction
		else if (Math.abs(element.getVel().getX()) > 5) {
			// if moving right
			if (element.getVel().getX() > 0) {
				this.dustParticles.angle = InanimateFloor.dustParticleAngleLeft;
			} else {
				this.dustParticles.angle = InanimateFloor.dustParticleAngleRight;
			}

			Vec2D pos = this.getPos().addY(-this.size.getY() / 2).setX(element.getPos().getX());

			this.dustParticles.spawn(this.getScene(), pos);
		}

		super.reactToCollision(element, dir);
	}

}
