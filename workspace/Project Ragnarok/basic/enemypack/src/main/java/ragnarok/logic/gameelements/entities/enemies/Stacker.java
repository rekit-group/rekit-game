package ragnarok.logic.gameelements.entities.enemies;

import java.util.LinkedList;
import java.util.List;

import home.fox.configuration.Configurable;
import home.fox.configuration.annotations.AfterSetting;
import home.fox.configuration.annotations.NoSet;
import home.fox.configuration.annotations.SetterInfo;
import ragnarok.config.GameConf;
import ragnarok.core.GameGrid;
import ragnarok.core.GameTime;
import ragnarok.logic.gameelements.GameElement;
import ragnarok.logic.gameelements.type.Enemy;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBColor;
import ragnarok.primitives.operable.OpProgress;
import ragnarok.primitives.time.Timer;
import ragnarok.util.ReflectUtils.LoadMe;

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
	private static RGBColor COLOR;
	private static int FACES;

	private static Vec SIZE_REGULAR;
	private static Vec SIZE_DYING;

	private static float DIE_ANIMATION_TIME;

	/**
	 * Prototype Constructor.
	 */
	public Stacker() {
		super();
	}

	public Stacker(Vec startPos) {
		// We dont need vel and size yet
		super(startPos, new Vec(), new Vec());

		// Initialize list for inner elements
		this.elements = new LinkedList<>();

		// This will be used to calculate positions recursively
		Vec rel = new Vec(0, 0.5f - new StackerElement(new Vec(), 0).getSize().getY() / 2);

		// creation loop
		for (int i = 0; i < Stacker.ITERATIONS; i++) {
			StackerElement elem = new StackerElement(rel, i);
			rel = rel.addY(-elem.getSize().getY() - 0.02f);
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

	private class StackerElement extends Enemy {

		private Vec relPos;
		private final int offset;

		private final int faceId;

		private Timer timeToDie;

		StackerElement(Vec relPos, int offset) {
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
			this.setPos(Stacker.this.getPos().add(this.relPos).addX((float) (0.1 * Math.sin(0.1 * GameTime.getTime() / 30 + this.offset))));

			if (this.timeToDie != null) {
				this.timeToDie.logicLoop();
				// this.timeToDie.removeTime(this.deltaTime);
				this.setSize(Stacker.dimensions.getNow(this.timeToDie.getProgress()));
				this.setPos(this.getPos().addY((-this.getSize().getY() + Stacker.dimensions.getNow(0).getY()) / 2f));
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
						element.setVel(element.getVel().setY(GameConf.PLAYER_KILL_BOOST));
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

		public void customDie() {
			if (this.offset == Stacker.this.highestOffset) {
				this.timeToDie = new Timer((long) (1000 * Stacker.DIE_ANIMATION_TIME));
				Stacker.this.highestOffset--;
			}
		}

	}
}
