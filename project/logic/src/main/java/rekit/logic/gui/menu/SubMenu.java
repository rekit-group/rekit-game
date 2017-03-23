package rekit.logic.gui.menu;

import java.util.ArrayList;
import java.util.List;

import rekit.core.GameGrid;
import rekit.logic.IScene;
import rekit.primitives.geometry.Vec;

/**
 *
 * This class realizes a Menu displaying a list of MenuItems.
 *
 */
public abstract class SubMenu extends MenuItem {
	/**
	 * This bool indicates whether this Menu (false) or its Items (true) shall
	 * be rendered.
	 */
	protected boolean inMenu = false;

	/**
	 * The current index.
	 */
	protected int index = 0;

	/**
	 * The contained Items.
	 */
	protected List<MenuItem> menuItems;
	/**
	 * The size of the items.
	 */
	protected Vec itemSize;

	/**
	 * Create the submenu.
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text (name)
	 */
	public SubMenu(IScene scene, String text) {
		super(scene, text);
		this.menuItems = new ArrayList<>();
	}

	/**
	 * Add an Item.
	 *
	 * @param i
	 *            the item
	 * @param items
	 *            more items
	 */
	public void addItem(MenuItem i, MenuItem... items) {
		this.addItem(i);
		for (MenuItem it : items) {
			this.addItem(it);
		}
	}

	/**
	 * Add an Item.
	 *
	 * @param i
	 *            the item
	 */
	private void addItem(MenuItem i) {
		i.setParent(this);
		this.menuItems.add(i);
		this.calcItemPos();
	}

	/**
	 * Remove an Item.
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
	 * Set the index (only when the menu is currently shown).
	 * On if the index is < 0 or >= length it will be set to 0 or length-1
	 * 
	 * @param index the new index.
	 */
	public void setIndex(int index) {
		if (this.inMenu) {
			return;
		}
		
		
		if (index < 0) {
			this.index = 0;
		}
		else if (index >= this.menuItems.size()) {
			this.index = this.menuItems.size() - 1;
		}
		else {
			this.index = index;
		}
	}

	/**
	 * Set the size of Items.
	 *
	 * @param value
	 *            the new size
	 */
	public void setItemSize(Vec value) {
		this.itemSize = value;
		this.calcItemPos();
	}

	@Override
	public final void up() {
		if (this.selected) {
			this.menuUp();
		} else if (this.inMenu) {
			// pass up
			this.menuItems.get(this.index).up();
		}
	}

	@Override
	public final void down() {
		if (this.selected) {
			this.menuDown();
		} else if (this.inMenu) {
			// pass down
			this.menuItems.get(this.index).down();
		}
	}

	@Override
	public final void left() {
		if (this.selected) {
			this.menuLeft();
		} else if (this.inMenu) {
			// pass left
			this.menuItems.get(this.index).left();
		}
	}

	@Override
	public final void right() {
		if (this.selected) {
			this.menuRight();
		} else if (this.inMenu) {
			// pass right
			this.menuItems.get(this.index).right();
		}
	}

	/**
	 * Process up while this menu is selected.
	 */
	protected void menuUp() {

	}

	/**
	 * Process down while this menu is selected.
	 */
	protected void menuDown() {

	}

	/**
	 * Process left while this menu is selected.
	 */
	protected void menuLeft() {

	}

	/**
	 * Process right while this menu is selected.
	 */
	protected void menuRight() {

	}

	@Override
	public void select() {
		MenuItem currentItem = this.menuItems.get(this.index);
		if (this.selected && currentItem.isSelectable()) {
			// select a MenuItem && unselect me
			this.selected = false;
			this.inMenu = true;
			currentItem.select();
		} else if (this.inMenu) {
			// pass select
			currentItem.select();
		} else {
			// select me
			this.selected = true;
			currentItem.setHover(true);
		}
	}

	@Override
	public void unselect() {
		if (this.selected) {
			if (this.parent == null) {
				return;
			}
			this.selected = false;
			this.inMenu = false;
			this.parent.focus();
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
	protected final void internalRender(GameGrid f) {
		if (this.selected) {
			// render this menu as complete Menu
			this.renderMenu(f);
		} else if (this.inMenu) {
			// pass the render to the selected MenuItem
			this.menuItems.get(this.index).internalRender(f);
		}
	}

	/**
	 * Renders all items of this menu. Called when this menu is selected
	 * (displaying its children).
	 *
	 * @param f
	 *            the field for rendering
	 */
	protected void renderMenu(GameGrid f) {
		for (final MenuItem menuItem : this.menuItems) {
			menuItem.renderItem(f);
		}
	}

	/**
	 * Calculate the new Position of each Item.
	 */
	protected abstract void calcItemPos();

}
