package ragnarok.logic.gui.menu;

import ragnarok.logic.IScene;
import ragnarok.primitives.geometry.Vec;

/**
 *
 * This class realizes a Menu displaying a list of MenuItems.
 *
 */
public class MenuList extends SubMenu {
	/**
	 * Create a MenuList.
	 * 
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text
	 */
	public MenuList(IScene scene, String text) {
		super(scene, text);
		this.setItemSize(new Vec(400, 100));
	}

	@Override
	public void menuUp() {
		// hover up
		this.menuItems.get(this.index).setHover(false);
		this.index -= 1;
		if (this.index < 0) {
			this.index = this.menuItems.size() - 1;
		}
		this.menuItems.get(this.index).setHover(true);
	}

	@Override
	public void menuDown() {
		// hover down
		this.menuItems.get(this.index).setHover(false);
		this.index += 1;
		if (this.index >= this.menuItems.size()) {
			this.index = 0;
		}
		this.menuItems.get(this.index).setHover(true);
	}

	@Override
	protected void calcItemPos() {
		// render menu as list
		Vec offset = new Vec(0, -((this.menuItems.size() - 1) * this.itemSize.getY()) / 2);

		for (final MenuItem menuItem : this.menuItems) {
			menuItem.setPos(this.getPos().add(offset));
			offset = offset.addY(this.itemSize.getY());
		}
	}

}
