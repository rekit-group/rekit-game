package edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies;

import java.util.LinkedList;
import java.util.List;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Enemy;
import edu.kit.informatik.ragnarok.primitives.geometry.Direction;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.primitives.operable.OpProgress;
import edu.kit.informatik.ragnarok.primitives.time.TimeDependency;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

@LoadMe
public class Stacker extends Enemy {

	private List<StackerElement> elements;
	
	private boolean init = false;
	
	private final OpProgress<Vec> dimensions = new OpProgress<>(new Vec(0.76f), new Vec(0.76f, 0));
	
	private int highestOffset;
	
	private static final int ITERATIONS = 3;
	
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
		private final RGBAColor col = new RGBAColor(249, 185, 22, 255);
		private final int offset;
		
		private final int faceId;
		
		private TimeDependency timeToDie;
				
		StackerElement (Vec relPos, int offset) {
			super(Stacker.this.getPos().add(relPos), new Vec(), dimensions.getNow(0));
			this.relPos = relPos;
			this.offset = offset;
			
			this.faceId = GameConf.PRNG.nextInt(5) + 1;
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
				f.drawCircle(this.getPos().addY((this.getSize().getY() - size.getY()) / 2), size, this.col);
			} else {
				f.drawCircle(this.getPos(), this.getSize(), this.col);
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
				this.timeToDie = new TimeDependency(0.5f);
				Stacker.this.highestOffset --;
			}
		}
		
	}
}
