package edu.kit.informatik.ragnarok.logic.gameelements.inanimate;

import java.util.HashMap;
import java.util.Map;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.Team;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawner;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.particles.ParticleSpawnerOption;
import edu.kit.informatik.ragnarok.logic.gameelements.type.DynamicInanimate;
import edu.kit.informatik.ragnarok.primitives.Direction;
import edu.kit.informatik.ragnarok.primitives.TimeDependency;
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;
import edu.kit.informatik.ragnarok.util.RGBColor;

/**
 * This Box realizes an {@link Inanimate} which the Player can climb up
 *
 * @author Dominik Fuchß
 *
 */
public class ClimbUpBox extends DynamicInanimate {

	protected InanimateBox innerBox;

	protected static final long PERIOD = 4000;

	protected long offset = 0;

	private Map<Integer, ClimbBoxStrategy> strategies;

	private int current = 0;

	private ClimbBoxStrategy strategy;

	private RGBAColor darkCol;
	private static RGBColor energyCol = new RGBColor(255, 100, 0);
	private TimeDependency timer;

	private static ParticleSpawner particles = null;

	static {
		ClimbUpBox.particles = new ParticleSpawner();
		ClimbUpBox.particles.angle = new ParticleSpawnerOption(0);
		ClimbUpBox.particles.colorR = new ParticleSpawnerOption(ClimbUpBox.energyCol.red);
		ClimbUpBox.particles.colorG = new ParticleSpawnerOption(ClimbUpBox.energyCol.green);
		ClimbUpBox.particles.colorB = new ParticleSpawnerOption(ClimbUpBox.energyCol.blue);
		ClimbUpBox.particles.colorA = new ParticleSpawnerOption(0, 220);
		ClimbUpBox.particles.timeMin = 0.2f;
		ClimbUpBox.particles.timeMax = 0.4F;
		ClimbUpBox.particles.amountMax = 1;
		ClimbUpBox.particles.size = new ParticleSpawnerOption(0.3f, 0.5f, 0, 0);
		ClimbUpBox.particles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
	}

	/**
	 * Prototype Constructor
	 */
	public ClimbUpBox() {
		super();
	}

	protected ClimbUpBox(Vec pos, Vec size, RGBAColor color, long offset) {
		super(pos, size, color);

		// create inner InanimateBox with given position
		this.innerBox = (InanimateBox) InanimateBox.staticCreate(pos);

		// prepare colors for rendering
		this.darkCol = color.darken(0.8f);

		// instantiate the two strategies
		this.strategies = new HashMap<>();
		this.strategies.put(0, new NoClimb(this));
		this.strategies.put(1, new BoostClimb(this));
		this.strategy = this.strategies.get(0);
		
		this.offset = offset;
		System.out.println(this.scene);
		this.timer = new TimeDependency(ClimbUpBox.PERIOD);
	}

	private long lastTime = -1;

	@Override
	public void logicLoop(float deltaTime) {

		// get time
		long nowTime = this.scene.getTime();
		// init lastTime in first run
		if (this.lastTime == -1) {
			this.lastTime = nowTime - this.offset - ((nowTime) % ClimbUpBox.PERIOD);
			if ((nowTime / ClimbUpBox.PERIOD) % 2 == 0) {
				nextStrategy();
			}
			System.out.println("calced " + (nowTime - this.lastTime));
		}
		// update timer
		this.timer.removeTime(nowTime - this.lastTime);
		// save current time for next iteration
		this.lastTime = nowTime;

		// Get new strategy from strategy map
		if (this.timer.timeUp()) {
			nextStrategy();
			this.timer.reset();
		}
	}
	
	private void nextStrategy() {
		this.current = (this.current + 1) % this.strategies.size();
		this.strategy = this.strategies.get(this.current);
	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		this.strategy.reactToCollision(element, dir);
	}

	@Override
	public void internalRender(Field f) {
		f.drawRectangle(this.getPos(), this.getSize(), this.color);
		f.drawRectangle(this.getPos().addY(-0.1f), this.getSize().multiply(0.2f, 0.8f), this.darkCol);
		f.drawRectangle(this.getPos().addY(0.4f), this.getSize().multiply(1, 0.2f), this.darkCol);

		this.renderEnergy(f, this.strategy.getEnergyStart(this.timer.getProgress()), this.strategy.getEnergyEnd(this.timer.getProgress()));

		this.strategy.internalRender(f);
	}

	public void renderEnergy(Field f, float start, float end) {

		float h = end - start;
		f.drawRectangle(this.getPos().addY(h / 2f - this.getSize().getY() / 2f + start), this.getSize().multiply(0.2f, h), ClimbUpBox.energyCol);

		if (end == 1) {
			f.drawRectangle(this.getPos().addY(0.4f), this.getSize().multiply(1, 0.2f), ClimbUpBox.energyCol);
		}

	}

	@Override
	public ClimbUpBox create(Vec startPos, String[] options) {
		long offset = 0; 
		if (options.length >= 1 && options[0] != null && options[0].matches("(\\+|-)?[1]")) {
			offset = PERIOD;
		}
		return new ClimbUpBox(startPos, new Vec(1), new RGBAColor(110, 110, 110, 255), offset);
	}

	private abstract class ClimbBoxStrategy {
		protected ClimbUpBox parent;

		ClimbBoxStrategy(ClimbUpBox parent) {
			this.parent = parent;
		}

		public void reactToCollision(GameElement element, Direction dir) {
			this.parent.innerBox.reactToCollision(element, dir);
		}

		public void internalRender(Field f) {

		}

		public abstract float getEnergyStart(float progress);

		public abstract float getEnergyEnd(float progress);
	}

	private class NoClimb extends ClimbBoxStrategy {
		NoClimb(ClimbUpBox parent) {
			super(parent);
		}

		@Override
		public float getEnergyStart(float progress) {
			return 0;
		}

		@Override
		public float getEnergyEnd(float progress) {
			return progress;
		}
	}

	private class BoostClimb extends ClimbBoxStrategy {
		BoostClimb(ClimbUpBox parent) {
			super(parent);
		}

		@Override
		public void internalRender(Field f) {
			f.drawRectangle(ClimbUpBox.this.getPos().addY(0.4f), ClimbUpBox.this.getSize().multiply(1, 0.2f), ClimbUpBox.energyCol);

			Vec pos = this.parent.getPos().addY(this.parent.getSize().getY() / 2f + 1).addX(-0.5f);
			pos = pos.addX(this.parent.getSize().getX() * GameConf.PRNG.nextFloat());

			ClimbUpBox.particles.spawn(this.parent.scene, pos);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			element.collidedWith(this.parent.getCollisionFrame(), dir);
			if (element.getTeam() == Team.PLAYER && dir == Direction.DOWN) {
				element.setVel(element.getVel().addY(4 * GameConf.PLAYER_BOTTOM_BOOST));
			}

		}

		@Override
		public float getEnergyStart(float progress) {
			return progress;
		}

		@Override
		public float getEnergyEnd(float progress) {
			return 1;
		}
	}

}
