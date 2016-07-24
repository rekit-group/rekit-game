package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.primitives.operable.OpProgress;

public class InanimateFloor extends Inanimate {

	private static int LAYERS = 5;

	private static int STRAW_NUM = 10;

	private static RGBColor[] layerCols;
	private static Vec[] layerVecs;

	private GrassStraw[] straws;

	private static ParticleSpawner dustParticles;
	private static final ParticleSpawnerOption dustParticleAngleLeft = new ParticleSpawnerOption((float) ((7 / 4f) * Math.PI),
			(float) ((5 / 4f) * Math.PI), -(float) ((1 / 4f) * Math.PI), 0);
	private static final ParticleSpawnerOption dustParticleAngleRight = new ParticleSpawnerOption((float) ((1 / 4f) * Math.PI),
			(float) ((3 / 4f) * Math.PI), 0, (float) ((1 / 4f) * Math.PI));
	private static final ParticleSpawnerOption dustParticleAngleTop = new ParticleSpawnerOption((float) (-(1 / 2f) * Math.PI),
			(float) ((1 / 2f) * Math.PI), 0, 0);

	static {
		// define color range for blocks layers
		RGBColor startCol = new RGBColor(22, 140, 30);
		RGBColor endCol = new RGBColor(23, 210, 49);

		// define position range for blocks layers
		float size = 1;
		Vec startVec = new Vec(0, -size / 2 + (size / 2) / LAYERS);
		Vec endVec = new Vec(0, size / 2 - (size / 2) / LAYERS);

		// create Progresses from ranges.
		OpProgress<RGBColor> progCols = new OpProgress<>(startCol, endCol);
		OpProgress<Vec> progVecs = new OpProgress<>(startVec, endVec);

		// fill arrays for each layer
		layerCols = new RGBColor[LAYERS];
		layerVecs = new Vec[LAYERS];
		for (int i = 0; i < LAYERS; i++) {
			float prog = i / (float) (LAYERS - 1);
			layerCols[i] = progCols.getNow(prog);
			layerVecs[i] = progVecs.getNow(prog);
		}

		// prepare particles
		dustParticles = new ParticleSpawner();
		dustParticles.colorR = new ParticleSpawnerOption(layerCols[0].red);
		dustParticles.colorG = new ParticleSpawnerOption(layerCols[0].green);
		dustParticles.colorB = new ParticleSpawnerOption(layerCols[0].blue);
		dustParticles.colorA = new ParticleSpawnerOption(255);

		dustParticles.amountMin = 8;
		dustParticles.amountMax = 15;

		dustParticles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
	}

	protected InanimateFloor(Vec pos, Vec size) {
		super(pos, size, layerCols[0].toRGBA());

		this.straws = new GrassStraw[STRAW_NUM];
		for (int i = 0; i < STRAW_NUM; i++) {
			this.straws[i] = new GrassStraw(pos);
		}
	}

	public class GrassStraw {
		private Vec pos;
		private RGBColor col;
		private Vec size;

		public GrassStraw(Vec parentPos) {
			this.pos = parentPos.add(new Vec(GameConf.PRNG.nextFloat() * 0.8f - 0.4f, -0.3f - GameConf.PRNG.nextFloat() * 0.4f));
			this.size = new Vec(0.06f, 0.3f + GameConf.PRNG.nextFloat() * 0.4f);
			this.col = layerCols[GameConf.PRNG.nextInt(layerCols.length)];
		}

		public void internalRender(Field f) {
			f.drawRectangle(this.pos, this.size, this.col);
		}
	}

	@Override
	public void internalRender(Field f) {
		// Draw rectangles that this Floor is composed of
		for (int i = 0; i < LAYERS; i++) {
			f.drawRectangle(this.getPos().add(layerVecs[i]), this.getSize().setY(1 / (float) LAYERS), layerCols[i]);
		}
		// Draw GrassStraws
		for (GrassStraw straw : this.straws) {
			straw.internalRender(f);
		}
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {

		if (Math.abs(element.getVel().getY()) > 6) {
			dustParticles.angle = InanimateFloor.dustParticleAngleTop;
			dustParticles.spawn(this.getScene(), this.getPos().addY(-this.getSize().getY() / 2));
		}
		// if strong velocity in x direction
		else if (Math.abs(element.getVel().getX()) > 5) {
			// if moving right
			if (element.getVel().getX() > 0) {
				dustParticles.angle = InanimateFloor.dustParticleAngleLeft;
			} else {
				dustParticles.angle = InanimateFloor.dustParticleAngleRight;
			}

			Vec pos = this.getPos().addY(-this.getSize().getY() / 2).setX(element.getPos().getX());

			dustParticles.spawn(this.getScene(), pos);
		}

		super.reactToCollision(element, dir);
	}

	public static Inanimate staticCreate(Vec pos) {
		return new InanimateFloor(pos, new Vec(1, 1));
	}

	@Override
	protected int getOrderZ() {
		return 10;
	}

}
