package edu.kit.informatik.ragnarok.logic.gameelements.gui.menu;

import java.util.ArrayList;
import java.util.List;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.Field;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;

/**
 *
 * This class realizes a Menu of SubMenus
 *
 */
public class MenuSubMenu extends MenuItem {
	/**
	 * The contained Items
	 */
	protected List<MenuItem> menuItems;
	/**
	 * The size of each item
	 */
	protected Vec itemSize;
	/**
	 * The current index
	 */
	protected int index = 0;
	/**
	 * This bool indicates whether this Menu (false) or its Items (true) shall
	 * be rendered
	 */
	protected boolean inMenu = false;

	/**
	 * Create the submenu
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text (name)
	 */
	public MenuSubMenu(Scene scene, String text) {
		super(scene, text);
		this.menuItems = new ArrayList<>();
		this.setPos(new Vec(GameConf.PIXEL_W / 2f, GameConf.PIXEL_H / 2f));
		this.itemSize = new Vec(400, 100);
	}

	/**
	 * Add an Item
	 *
	 * @param i
	 *            the item
	 */
	public void addItem(MenuItem i) {
		i.setParent(this);
		this.menuItems.add(i);
		this.calcItemPos();
	}

	/**
	 * Remove an Item
	 *
	 * @param i
	 *            the item
	 */
	public void removeItem(MenuItem i) {
		i.setParent(null);
		this.menuItems.remove(i);
		this.index = 0;
		this.calcItemPos();
	}

	/**
	 * Set the size of Items
	 *
	 * @param value
	 *            the new size
	 */
	public void setItemSize(Vec value) {
		this.itemSize = value;
		this.calcItemPos();
	}

	@Override
	public void up() {
		if (this.selected) {
			this.menuItems.get(this.index).setHover(false);
			this.index -= 1;
			if (this.index < 0) {
				this.index = this.menuItems.size() - 1;
			}
			this.menuItems.get(this.index).setHover(true);
		} else {
			this.menuItems.get(this.index).up();
		}
	}

	@Override
	public void down() {
		if (this.selected) {
			this.menuItems.get(this.index).setHover(false);
			this.index += 1;
			if (this.index >= this.menuItems.size()) {
				this.index = 0;
			}
			this.menuItems.get(this.index).setHover(true);
		} else {
			this.menuItems.get(this.index).down();
		}
	}

	@Override
	public void left() {
		if (this.selected) {

		} else {
			this.menuItems.get(this.index).left();
		}
	}

	@Override
	public void right() {
		if (this.selected) {

		} else {
			this.menuItems.get(this.index).right();
		}
	}

	@Override
	public void select() {
		if (this.selected) {
			this.selected = false;
			this.inMenu = true;
			this.menuItems.get(this.index).select();
		} else if (this.inMenu) {
			this.menuItems.get(this.index).select();
		} else {
			this.selected = true;
			this.menuItems.get(this.index).setHover(true);
		}
	}

	@Override
	public void unselect() {
		if (this.selected) {
			this.selected = false;
			this.inMenu = false;

			if (this.parent != null) {
				this.parent.focus();
			}
		} else if (this.inMenu) {
			// pass unselect
			this.menuItems.get(this.index).unselect();
		}
	}

	@Override
	protected void focus() {
		this.selected = true;
		this.inMenu = false;
	}

	@Override
	protected void internalRender(Field f) {
		if (this.selected) {
			// render this menu as complete Menu
			for (final MenuItem menuItem : this.menuItems) {
				menuItem.internalRender(f);
			}
		} else if (this.inMenu) {
			// pass the render to the selected MenuItem
			this.menuItems.get(this.index).internalRender(f);
		} else {
			// render this menu as MenuItem
			this.renderItem(f);
		}
	}

	/**
	 * Calculate the new Position of each Item
	 */
	protected void calcItemPos() {
		// render menu as list
		Vec offset = new Vec(0, -((this.menuItems.size() - 1) * this.itemSize.getY()) / 2);

		for (final MenuItem menuItem : this.menuItems) {
			menuItem.setPos(this.getPos().add(offset));
			offset = offset.addY(this.itemSize.getY());
		}
	}

}
