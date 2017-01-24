package ragnarok.logic.gameelements.entities.pickups;

import home.fox.visitors.Visitable;
import home.fox.visitors.annotations.NoVisit;
import home.fox.visitors.annotations.VisitInfo;
import ragnarok.core.GameElement;
import ragnarok.core.GameGrid;
import ragnarok.logic.gameelements.entities.Player;
import ragnarok.logic.gameelements.type.Pickup;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;
import ragnarok.primitives.image.RGBColor;
import ragnarok.util.ReflectUtils.LoadMe;

@LoadMe
@VisitInfo(res = "conf/bluepill")
public class BluePill extends Pickup implements Visitable {

	private static Vec SIZE;
	@NoVisit
	private float x;
	@NoVisit
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
	private void drawPlayer(GameGrid f, Player p) {
		// TODO Better graphical style.
		f.drawRoundRectangle(p.getPos(), p.getSize(), new RGBAColor(148, 172, 236, 180), 0.45F, 0.45F);
		Vec left = p.getPos().sub(p.getSize().scalar(0.15F));
		f.drawCircle(left, p.getSize().scalar(0.11F), new RGBColor(0, 0, 0));
		f.drawCircle(left.addX((p.getPos().getX() - left.getX()) * 2.5F), p.getSize().scalar(0.11F), new RGBColor(0, 0, 0));

	}
}
