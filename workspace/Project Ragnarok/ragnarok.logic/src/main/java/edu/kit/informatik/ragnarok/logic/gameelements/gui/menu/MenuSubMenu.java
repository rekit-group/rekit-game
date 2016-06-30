package edu.kit.informatik.ragnarok.logic.gameelements.gui.menu;

import java.util.ArrayList;
import java.util.List;

import edu.kit.informatik.ragnarok.logic.gameelements.Field;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class MenuSubMenu extends MenuItem {

	private List<MenuItem> menuItems;

	private int index = 0;

	private boolean inMenu = false;

	public MenuSubMenu(Scene scene, String text) {
		super(scene, text);
		this.menuItems = new ArrayList<>();
	}

	public void addItem(MenuItem i) {
		i.setParent(this);
		this.menuItems.add(i);
	}

	public void removeItem(MenuItem i) {
		i.setParent(null);
		this.removeItem(i);
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
	public void select() {
		if (this.selected) {
			this.unselect();
			this.inMenu = true;
			this.menuItems.get(this.index).select();
		} else {
			this.selected = true;
			this.index = 0;
			this.menuItems.get(this.index).setHover(true);
		}
	}

	@Override
	public void unselect() {
		if (this.selected) {
			this.selected = false;
			this.inMenu = false;
		} else if (this.inMenu) {
			// pass unselect
			this.menuItems.get(this.index).unselect();
		} else {
			this.back();
		}
	}

	@Override
	protected void internalRender(Field f) {
		if (this.selected) {
			// render this menu as complete Menu
			int itemSize = 100;
			Vec offset = new Vec(0, -(this.menuItems.size() * itemSize) / 2);

			for (MenuItem menuItem : this.menuItems) {
				menuItem.setPos(this.getPos().add(offset));
				menuItem.internalRender(f);
				offset = offset.addY(itemSize);
			}
		} else if (this.inMenu) {
			// pass the render to the selected MenuItem
			this.menuItems.get(this.index).internalRender(f);
		} else {
			// render this menu as MenuItem
			super.internalRender(f);
		}
	}

}
