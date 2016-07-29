package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.LinkedList;
import java.util.List;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.primitives.operable.OpProgress;
import edu.kit.informatik.ragnarok.primitives.time.Timer;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import edu.kit.informatik.ragnarok.visitor.Visitable;
import edu.kit.informatik.ragnarok.visitor.annotations.AfterVisit;
import edu.kit.informatik.ragnarok.visitor.annotations.NoVisit;
import edu.kit.informatik.ragnarok.visitor.annotations.VisitInfo;

@LoadMe
@VisitInfo(res = "conf/stacker", visit = true)
public class Stacker extends Enemy implements Visitable {

	@NoVisit
	private List<StackerElement> elements;

	@NoVisit
	private boolean init = false;

	@NoVisit
	private static OpProgress<Vec> dimensions;

	@NoVisit
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

	@AfterVisit
	private static void afterVisit() {
		Stacker.dimensions = new OpProgress<>(Stacker.SIZE_REGULAR, Stacker.SIZE_DYING);
	}

	@Override
	public void logicLoop(float deltaTime) {
		if (!this.init) {
			for (StackerElement elem : this.elements) {
				this.getScene().addGameElement(elem);
			}
			// We dont need this actual GameElement anymore
			this.getScene().removeGameElement(this);
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
		public void logicLoop(float deltaTime) {
			this.setPos(Stacker.this.getPos().add(this.relPos).addX((float) (0.1 * Math.sin(0.1 * this.getScene().getTime() / 30 + this.offset))));

			if (this.timeToDie != null) {
				this.timeToDie.removeTime(deltaTime);
				this.setSize(Stacker.dimensions.getNow(this.timeToDie.getProgress()));
				this.setPos(this.getPos().addY((-this.getSize().getY() + Stacker.dimensions.getNow(0).getY()) / 2f));
				if (this.timeToDie.timeUp()) {
					this.addDamage(1);
				}
			}
		}

		@Override
		public void internalRender(Field f) {
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
				this.timeToDie = new Timer(Stacker.DIE_ANIMATION_TIME);
				Stacker.this.highestOffset--;
			}
		}

	}
}
