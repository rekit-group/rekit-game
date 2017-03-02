package ragnarok.logic.gameelements.entities.pickups;

import home.fox.configuration.Configurable;
import home.fox.configuration.annotations.NoSet;
import home.fox.configuration.annotations.SetterInfo;
import ragnarok.core.GameGrid;
import ragnarok.logic.gameelements.GameElement;
import ragnarok.logic.gameelements.entities.Player;
import ragnarok.logic.gameelements.type.Pickup;
import ragnarok.primitives.geometry.Direction;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;
import ragnarok.util.ReflectUtils.LoadMe;

/**
 * This class realizes a pickup which makes the Player invincible for a short
 * time.
 *
 * @author Dominik Fuchss
 *
 */
@LoadMe
@SetterInfo(res = "conf/bluepill")
public class BluePill extends Pickup implements Configurable {
	/**
	 * The size of the pickup.
	 */
	private static Vec SIZE;
	/**
	 * The current x for sinus.
	 */
	@NoSet
	private float x;
	/**
	 * The current sinus value.
	 */
	@NoSet
	private double sin;

	/**
	 * Prototype constructor.
	 */
	private BluePill() {
		super();
	}

	/**
	 * Create a new BluePill by position and options.
	 *
	 * @param startPos
	 *            the start pos
	 * @param options
	 *            the options
	 */
	private BluePill(Vec startPos, String[] options) {
		super(startPos, new Vec(), BluePill.SIZE);

	}

	@Override
	protected void innerLogicLoop() {
		this.x += this.deltaTime / 1000F;
		this.sin = Math.sin(this.x * 6);
		this.setSize(BluePill.SIZE.scalar((float) this.sin));
	}

	@Override
	public void internalRender(GameGrid f) {
		f.drawCircle(this.getPos(), this.getSize(), new RGBAColor(0, 0, 255, 150));
		f.drawCircle(this.getPos(), this.getSize().scalar(0.5F), new RGBAColor(0, 255, 0, 127));
		f.drawCircle(this.getPos(), this.getSize().scalar(0.3F), new RGBAColor(255, 0, 0, 88));
		f.drawCircle(this.getPos(), this.getSize().scalar(0.2F), new RGBAColor(127, 127, 23, 23));
	}

	@Override
	public GameElement create(Vec startPos, String[] options) {
		return new BluePill(startPos, options);
	}

	@Override
	public void perform(GameElement collector) {
		this.destroy();
		if (collector.getClass() == Player.class) {
			Player player = (Player) collector;
			player.setInvincible(4000);
			player.setTemporaryApperance((f) -> this.drawPlayer(f, player), 4000);
		}
	}

	/**
	 * Draw player after picked up blue pill.
	 *
	 * @param f
	 *            the field
	 * @param p
	 *            the player
	 */
	private void drawPlayer(GameGrid f, Player p) {
		Direction dir = Direction.RIGHT;
		// determine if direction needs to be changed +- delta: 0.15
		if (p.getVel().getX() < -0.15) {
			dir = Direction.LEFT;
		}
		f.drawRoundRectangle(p.getPos(), p.getSize(), new RGBAColor(148, 172, 236, 180), 0.45F, 0.45F);
		String src = dir == Direction.RIGHT //
				? "mrRekt_glasses_right.png" // facing right
				: "mrRekt_glasses_left.png"; // facing left
		f.drawImage(p.getPos().addY(-0.025f * p.getVel().getY()), p.getSize(), src);
	}
}
