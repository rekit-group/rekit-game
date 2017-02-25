package ragnarok.logic.gui.menu;

import ragnarok.config.GameConf;
import ragnarok.core.GameGrid;
import ragnarok.core.IScene;
import ragnarok.primitives.geometry.Vec;
import ragnarok.primitives.image.RGBAColor;

/**
 * This class realizes a TextField for a Menu.
 *
 * @author Dominik Fuchss
 *
 */
public class TextMenu extends MenuItem {
	/**
	 * Vector for spacing.
	 */
	private final Vec space = new Vec(GameConf.PIXEL_W * 0.95F, GameConf.PIXEL_H * 0.9F).scalar(0.05F, 0.1F);

	/**
	 * Create the MenuItem.
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text
	 */
	public TextMenu(IScene scene, String text) {
		super(scene, text, new Vec(GameConf.PIXEL_W * 0.95F, GameConf.PIXEL_H * 0.9F));
	}

	/**
	 * Render the Item itself.
	 *
	 * @param f
	 *            the field
	 */
	@Override
	protected void renderItem(GameGrid f) {
		f.drawRectangle(this.getPos(), this.getSize(), new RGBAColor(0, 0, 0, 200), false);
		f.drawText(this.getPos().sub(this.getSize().scalar(0.5F)).add(this.space), this.getText(), GameConf.ABOUT_TEXT, false);
	}

	@Override
	public boolean isSelectable() {
		return false;
	}
}
