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
import edu.kit.informatik.ragnarok.visitor.AfterVisit;
import edu.kit.informatik.ragnarok.visitor.NoVisit;
import edu.kit.informatik.ragnarok.visitor.VisitInfo;
import edu.kit.informatik.ragnarok.visitor.Visitable;

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
		elements = new LinkedList<>();
		
		// This will be used to calculate positions recursively
		Vec rel = new Vec(0, 0.5f - new StackerElement(new Vec(), 0).getSize().getY() / 2);
		
		// creation loop
		for (int i = 0; i < ITERATIONS; i++) {
			StackerElement elem = new StackerElement(rel, i);
			rel = rel.addY(-elem.getSize().getY() - 0.02f);
			elements.add(elem);
		}
		
		highestOffset = ITERATIONS - 1;
	}
	
	
	@AfterVisit
	private static void afterVisit() {
		dimensions = new OpProgress<>(SIZE_REGULAR, SIZE_DYING);
	}

	public void logicLoop(float deltaTime) {
		if (!init) {
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
				
		StackerElement (Vec relPos, int offset) {
			super(Stacker.this.getPos().add(relPos), new Vec(), dimensions.getNow(0));
			this.relPos = relPos;
			this.offset = offset;
			
			this.faceId = GameConf.PRNG.nextInt(FACES) + 1;
		}
		
		@Override
		public GameElement create(Vec startPos, String[] options) {
			// Not required since Stacker handles StackerElement instantiation .
			return null;
		}
		
		@Override
		public void logicLoop(float deltaTime) {
			this.setPos(Stacker.this.getPos().add(relPos).addX((float)(0.1*Math.sin(0.1 * this.getScene().getTime() / 30 + offset))));
			
			if (timeToDie != null) {
				timeToDie.removeTime(deltaTime);
				if (timeToDie.timeUp()) {
					this.addDamage(1);
				}
			}
		}
		
		@Override
		public void internalRender(Field f) {
			if (timeToDie != null) {
				Vec size = dimensions.getNow(timeToDie.getProgress());
				f.drawCircle(this.getPos().addY((this.getSize().getY() - size.getY()) / 2), size, COLOR);
			} else {
				f.drawCircle(this.getPos(), this.getSize(), COLOR);
				f.drawImage(this.getPos(), this.getSize(), "stacker/stackerFaces_0" + this.faceId + ".png");
			}
		}
		
		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			if (this.getTeam().isHostile(element.getTeam())) {
				if (dir == Direction.DOWN || dir == Direction.UP) {
					if (this.timeToDie == null) {
						element.setVel(element.getVel().setY(1.2f * GameConf.PLAYER_JUMP_BOOST));
						element.collidedWith(this.getCollisionFrame(), dir);
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
				this.timeToDie = new Timer(DIE_ANIMATION_TIME);
				Stacker.this.highestOffset --;
			}
		}
		
	}
}
