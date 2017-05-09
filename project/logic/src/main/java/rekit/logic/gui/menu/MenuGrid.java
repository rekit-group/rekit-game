package rekit.logic.gui.menu;

import rekit.logic.IScene;
import rekit.primitives.geometry.Vec;

/**
 *
 * This class realizes a {@link MenuList}. In this SubMenu Items will be
 * arranged as Grid
 *
 */
public class MenuGrid extends SubMenu {
	/**
	 * Number of columns.
	 */
	private int colCount;
	/**
	 * Number of rows.
	 */
	private int rowCount;

	/**
	 * Create a MenuGrid.
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text (name)
	 * @param colCount
	 *            the column count
	 */
	public MenuGrid(IScene scene, String text, int colCount) {
		super(scene, text);
		this.colCount = colCount;
		this.setItemSize(new Vec(100));
	}

	/**
	 * Create a MenuGrid.
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text (name)
	 * @param colCount
	 *            the column count
	 * @param sizeX
	 *            the itemSize X
	 * @param sizeY
	 *            the itemSize Y
	 */
	public MenuGrid(IScene scene, String text, int colCount, int sizeX, int sizeY) {
		this(scene, text, colCount);
		this.setItemSize(new Vec(sizeX, sizeY));
	}

	@Override
	public void menuUp() {
		this.menuItems.get(this.index).setHover(false);
		int newIndex = this.index - this.colCount;

		this.index -= this.colCount;

		if (newIndex < 0) {
			newIndex += this.rowCount * this.colCount;
			if (newIndex >= this.menuItems.size()) {
				newIndex -= this.colCount;
			}
		}
		if (this.menuItems.get(newIndex).isSelectable()) {
			this.index = newIndex;
		}
		this.menuItems.get(this.index).setHover(true);
	}

	@Override
	public void menuDown() {
		this.menuItems.get(this.index).setHover(false);

		int newIndex = this.index + this.colCount;
		if (newIndex >= this.menuItems.size()) {
			newIndex %= this.colCount;
		}
		if (this.menuItems.get(newIndex).isSelectable()) {
			this.index = newIndex;
		}
		this.menuItems.get(this.index).setHover(true);
	}

	@Override
	public void menuLeft() {
		this.menuItems.get(this.index).setHover(false);
		int newIndex = this.index - 1;
		if (newIndex < 0) {
			newIndex = this.menuItems.size() - 1;
		}
		if (this.menuItems.get(newIndex).isSelectable()) {
			this.index = newIndex;
		}
		this.menuItems.get(this.index).setHover(true);
	}

	@Override
	public void menuRight() {
		this.menuItems.get(this.index).setHover(false);
		int newIndex = this.index + 1;
		if (newIndex >= this.menuItems.size()) {
			newIndex = 0;
		}
		if (this.menuItems.get(newIndex).isSelectable()) {
			this.index = newIndex;
		}
		this.menuItems.get(this.index).setHover(true);
	}

	@Override
	protected void calcItemPos() {
		// render menu as grid with max colCount items per row
		this.rowCount = this.menuItems.size() / this.colCount + 1;
		final Vec startOffset = new Vec(-((this.colCount - 1) * this.itemSize.x) / 2, -((this.rowCount - 1) * this.itemSize.y) / 2);
		Vec offset = startOffset.clone();

		int curCol = 0;
		for (final MenuItem menuItem : this.menuItems) {
			menuItem.setPos(this.getPos().add(offset));
			if (++curCol >= this.colCount) {
				offset = offset.setX(startOffset.x);
				offset = offset.addY(this.itemSize.y);
				curCol = 0;
			} else {
				offset = offset.addX(this.itemSize.x);
			}
		}
	}

}
