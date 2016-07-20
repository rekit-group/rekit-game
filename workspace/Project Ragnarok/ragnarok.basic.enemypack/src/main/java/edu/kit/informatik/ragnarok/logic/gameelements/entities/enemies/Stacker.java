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
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;

@LoadMe
public class Stacker extends Enemy {

	private List<StackerElement> elements;
	
	private boolean init = false;
	
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
		for (int i = 0; i < 3; i++) {
			StackerElement elem = new StackerElement(rel, i);
			rel = rel.addY(-elem.getSize().getY() - 0.02f);
			elements.add(elem);
		}
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
				
		StackerElement (Vec relPos, int offset) {
			super(Stacker.this.getPos().add(relPos), new Vec(), new Vec(0.76f));
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
		}
		
		@Override
		public void internalRender(Field f) {
			f.drawCircle(this.getPos(), this.getSize(), this.col);
			f.drawImage(this.getPos(), this.getSize(), "stacker/stackerFaces_0" + this.faceId + ".png");
		}
		
		@Override
		public void reactToCollision(GameElement element, Direction dir) {
			if (this.getTeam().isHostile(element.getTeam())) {
				if (dir == Direction.DOWN || dir == Direction.UP) {
					element.setVel(element.getVel().setY(1.2f * GameConf.PLAYER_JUMP_BOOST));
					element.collidedWith(this.getCollisionFrame(), dir);
					this.addDamage(1);
				} else {
					element.addDamage(1);
					element.collidedWith(this.getCollisionFrame(), dir);
				}
			}	
		}
		
	}
}
