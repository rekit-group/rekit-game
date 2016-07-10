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
import edu.kit.informatik.ragnarok.primitives.Vec;
import edu.kit.informatik.ragnarok.util.RGBAColor;

/**
 * This Box realizes an {@link Inanimate} which boosts the Player from time to
 * time
 *
 * @author Dominik Fuchß
 *
 */
public class BoostBox extends DynamicInanimate {

	protected InanimateBox innerBox;

	protected static final long PERIOD = 4500;

	protected long offset = 0;

	private Map<Integer, BoostBoxStrategy> strategies;

	private int current = 0;

	private BoostBoxStrategy strategy;

	private static ParticleSpawner particles = null;

	static {
		BoostBox.particles = new ParticleSpawner();
		BoostBox.particles.angle = new ParticleSpawnerOption(0, (float) (2 * Math.PI), (float) (2 * Math.PI), (float) (4 * Math.PI));
		BoostBox.particles.colorR = new ParticleSpawnerOption(250, 0);
		BoostBox.particles.colorG = new ParticleSpawnerOption(250, -250);
		BoostBox.particles.colorB = new ParticleSpawnerOption(150);
		BoostBox.particles.colorA = new ParticleSpawnerOption(220, -220);
		BoostBox.particles.timeMin = 0.2f;
		BoostBox.particles.timeMax = 0.4F;
		BoostBox.particles.amountMax = 1;

		BoostBox.particles.speed = new ParticleSpawnerOption(2, 3, -1, 1);
	}

	private boolean sparkling;

	/**
	 * Prototype Constructor
	 */
	public BoostBox() {
		super();
	}

	protected BoostBox(Vec pos, Vec size, RGBAColor color) {
		super(pos, size, color);

		// create inner InanimateBox with given position
		this.innerBox = (InanimateBox) InanimateBox.staticCreate(pos);
		// and set color
		this.innerBox.color = new RGBAColor(80, 80, 255, 255);

		// instantiate the two strategies
		this.strategies = new HashMap<>();
		this.strategies.put(0, new NoBoost(this));
		this.strategies.put(1, new BoostFirstState(this));
		this.strategies.put(2, new BoostMaxState(this));
		this.strategy = this.strategies.get(0);
	}

	private long lastTime = 0;

	@Override
	public void logicLoop(float deltaTime) {
		// Get new strategy from strategy map

		this.offset += (this.scene.getTime() - this.lastTime);
		this.lastTime = this.scene.getTime();
		if (this.offset > BoostBox.PERIOD - 750) {
			this.sparkling = true;
		} else {
			this.sparkling = false;
		}
		if (this.offset < BoostBox.PERIOD) {
			return;
		}
		this.offset = 0;
		this.current = (this.current + 1) % this.strategies.size();
		this.strategy = this.strategies.get(this.current);

	}

	@Override
	public void reactToCollision(GameElement element, Direction dir) {
		this.strategy.reactToCollision(element, dir);
	}

	@Override
	public void internalRender(Field f) {
		this.strategy.internalRender(f);
		if (this.sparkling) {
			BoostBox.particles.spawn(this.scene, this.pos);
		}
	}

	@Override
	public int getID() {
		return 85;
	}

	@Override
	public BoostBox create(Vec startPos, String[] options) {
		return new BoostBox(startPos, new Vec(1), new RGBAColor(80, 80, 255, 255));
	}

	private abstract class BoostBoxStrategy {
		protected BoostBox parent;

		BoostBoxStrategy(BoostBox parent) {
			this.parent = parent;
		}

		public void reactToCollision(GameElement element, Direction dir) {
			// Do nothing
		}

		public abstract RGBAColor getColor();

		public void internalRender(Field f) {
			this.parent.innerBox.color = this.getColor();
			// Call decorated InanimateBoxes internalRender
			this.parent.innerBox.internalRender(f);
		}
	}

	private class NoBoost extends BoostBoxStrategy {

		NoBoost(BoostBox parent) {
			super(parent);
		}

		@Override
		public RGBAColor getColor() {
			return new RGBAColor(80, 80, 80, 255);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			element.collidedWith(this.parent.getCollisionFrame(), dir);
		}

	}

	private class BoostFirstState extends BoostBoxStrategy {
		BoostFirstState(BoostBox parent) {
			super(parent);
		}

		@Override
		public RGBAColor getColor() {
			return new RGBAColor(30, 255, 30, 255);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			element.collidedWith(this.parent.getCollisionFrame(), dir);

			if (element.getTeam() == Team.PLAYER && dir == Direction.UP) {
				element.setVel(element.getVel().addY(GameConf.PLAYER_BOTTOM_BOOST));
			}

		}
	}

	private class BoostMaxState extends BoostBoxStrategy {
		BoostMaxState(BoostBox parent) {
			super(parent);
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			element.collidedWith(this.parent.getCollisionFrame(), dir);
			if (element.getTeam() == Team.PLAYER && dir == Direction.UP) {
				element.setVel(element.getVel().addY(4 * GameConf.PLAYER_BOTTOM_BOOST));
			}
		}

		@Override
		public RGBAColor getColor() {
			return new RGBAColor(255, 30, 30, 255);
		}

	}

}
