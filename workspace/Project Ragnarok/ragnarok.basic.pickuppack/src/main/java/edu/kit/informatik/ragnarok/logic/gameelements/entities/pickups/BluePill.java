package edu.kit.informatik.ragnarok.logic.gameelements.entities.pickups;

import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.type.Pickup;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.primitives.image.RGBAColor;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.util.ReflectUtils.LoadMe;
import home.fox.visitors.Visitable;
import home.fox.visitors.annotations.VisitInfo;

@LoadMe
@VisitInfo(res = "conf/bluepill", visit = true)
public class BluePill extends Pickup implements Visitable {

	private static Vec SIZE;
	private float x;
	private double sin;

	private BluePill() {
		super();
	}

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
	public void internalRender(Field f) {
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
		Player player = (Player) collector;
		player.setInvincible(4000);
		player.setTemporaryApperance((f) -> this.drawPlayer(f, player), 4000);

	}

	/**
	 * Draw player after picked up blue pill.
	 *
	 * @param f
	 *            the field
	 * @param p
	 *            the player
	 */
	private void drawPlayer(Field f, Player p) {
		// TODO Better graphical style.
		f.drawRoundRectangle(p.getPos(), p.getSize(), new RGBAColor(148, 172, 236, 180), 0.45F, 0.45F);
		Vec left = p.getPos().sub(p.getSize().scalar(0.15F));
		f.drawCircle(left, p.getSize().scalar(0.11F), new RGBColor(0, 0, 0));
		f.drawCircle(left.addX((p.getPos().getX() - left.getX()) * 2.5F), p.getSize().scalar(0.11F), new RGBColor(0, 0, 0));

	}
}
