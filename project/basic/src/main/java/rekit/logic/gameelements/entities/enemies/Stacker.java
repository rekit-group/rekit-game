package rekit.logic.gameelements.entities.enemies;

import java.util.LinkedList;
import java.util.List;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.annotations.AfterSetting;
import org.fuchss.configuration.annotations.NoSet;
import org.fuchss.configuration.annotations.SetterInfo;

import net.jafama.FastMath;
import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.core.GameTime;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gameelements.type.Enemy;
import rekit.primitives.geometry.Direction;
import rekit.primitives.geometry.Vec;
import rekit.primitives.image.RGBAColor;
import rekit.primitives.operable.OpProgress;
import rekit.primitives.time.Timer;
import rekit.util.ReflectUtils.LoadMe;

/**
 * This class realizes a Enemy called Stacker.<br>
 * This enemy contains out of 3 faces which can take damage to the player.
 *
 * @author Angelo Aracri
 *
 */
@LoadMe
@SetterInfo(res = "conf/stacker")
public final class Stacker extends Enemy implements Configurable {

	@NoSet
	private List<StackerElement> elements;

	@NoSet
	private boolean init = false;

	@NoSet
	private static OpProgress<Vec> dimensions;

	@NoSet
	private int highestOffset;

	private static int ITERATIONS;
	private static RGBAColor COLOR;
	private static int FACES;

	private static Vec SIZE_REGULAR;
	private static Vec SIZE_DYING;

	private static float DIE_ANIMATION_TIME;

	/**
	 * The score the {@link Player} receives upon killing this {@link Enemy}
	 */
	public static int POINTS;

	/**
	 * Prototype Constructor.
	 */
	public Stacker() {
		super();
	}

	private Stacker(Vec startPos) {
		// We dont need vel and size yet
		super(startPos, new Vec(), new Vec());

		// Initialize list for inner elements
		this.elements = new LinkedList<>();

		// This will be used to calculate positions recursively
		Vec rel = new Vec(0, 0.5f - new StackerElement(new Vec(), 0).getSize().y / 2);

		// creation loop
		for (int i = 0; i < Stacker.ITERATIONS; i++) {
			StackerElement elem = new StackerElement(rel, i);
			rel = rel.addY(-elem.getSize().y - 0.02f);
			this.elements.add(elem);
		}

		this.highestOffset = Stacker.ITERATIONS - 1;
	}

	@AfterSetting
	private static void afterVisit() {
		Stacker.dimensions = new OpProgress<>(Stacker.SIZE_REGULAR, Stacker.SIZE_DYING);
	}

	@Override
	protected void innerLogicLoop() {
		if (!this.init) {
			for (StackerElement elem : this.elements) {
				this.getScene().addGameElement(elem);
			}
			// We dont need this actual GameElement anymore
			this.getScene().markForRemove(this);
		}
	}

	@Override
	public GameElement create(Vec startPos, String[] options) {
		return new Stacker(startPos);
	}

	private final class StackerElement extends Enemy {

		private Vec relPos;
		private final int offset;

		private final int faceId;

		private Timer timeToDie;

		private StackerElement(Vec relPos, int offset) {
			super(Stacker.this.getPos().add(relPos), new Vec(), Stacker.dimensions.getNow(0));
			this.relPos = relPos;
			this.offset = offset;

			this.faceId = GameConf.PRNG.nextInt(Stacker.FACES) + 1;
		}

		@Override
		public GameElement create(Vec startPos, String[] options) {
			// Not required since Stacker handles StackerElement instantiation .
			return null;
		}

		@Override
		protected void innerLogicLoop() {
			this.setPos(Stacker.this.getPos().add(this.relPos).addX((float) (0.1 * FastMath.sin(0.1 * GameTime.getTime() / 30 + this.offset))));

			if (this.timeToDie != null) {
				this.timeToDie.logicLoop();
				// this.timeToDie.removeTime(this.deltaTime);
				this.setSize(Stacker.dimensions.getNow(this.timeToDie.getProgress()));
				this.setPos(this.getPos().addY((-this.getSize().y + Stacker.dimensions.getNow(0).y) / 2f));
				if (this.timeToDie.timeUp()) {
					this.addDamage(1);
				}
			}
		}

		@Override
		public void internalRender(GameGrid f) {
			if (this.timeToDie != null) {
				f.drawCircle(this.getPos(), this.getSize(), Stacker.COLOR);
			} else {
				f.drawCircle(this.getPos(), this.getSize(), Stacker.COLOR);
				f.drawImage(this.getPos(), this.getSize(), "stacker/stackerFaces_0" + this.faceId + ".png");
			}
		}

		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			if (this.getTeam().isHostile(element.getTeam())) {
				if (dir == Direction.DOWN || dir == Direction.UP) {
					if (this.timeToDie == null) {
						element.collidedWith(this.getCollisionFrame(), dir);
						element.killBoost();
						// give the player points
						this.getScene().getPlayer().addPoints(Stacker.POINTS);
						this.customDie();
					} else {
						element.collidedWith(this.getCollisionFrame(), dir);
					}
				} else {
					element.addDamage(1);
					element.collidedWith(this.getCollisionFrame(), dir);
				}
			}
		}

		/**
		 * Will be called upon death.
		 */
		private void customDie() {
			if (this.offset == Stacker.this.highestOffset) {
				this.timeToDie = new Timer((long) (1000 * Stacker.DIE_ANIMATION_TIME));
				Stacker.this.highestOffset--;
			}
		}

	}
}
