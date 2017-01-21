package ragnarok.logic.gui.menu;

import ragnarok.config.GameConf;
import ragnarok.core.Field;
import ragnarok.core.GuiElement;
import ragnarok.core.IScene;
import ragnarok.primitives.geometry.Vec;

/**
 * This class realizes an Item of a menu.
 *
 */
public abstract class MenuItem extends GuiElement {
	/**
	 * Indicates whether the item is selected.
	 */
	protected boolean selected = false;
	/**
	 * The text (name) of the item.
	 */
	private String text;
	/**
	 * The parent of the Item or {@code null} if none exist.
	 */
	protected MenuItem parent;
	/**
	 * Indicates a hover (over the item).
	 */
	private boolean hover;

	/**
	 * Create an Item.
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text (name)
	 */
	protected MenuItem(IScene scene, String text) {
		this(scene, text, new Vec(400, 80));
		this.setPos(new Vec(GameConf.PIXEL_W / 2f, GameConf.PIXEL_H / 2f));
	}

	/**
	 * Create an Item.
	 *
	 * @param scene
	 *            the scene
	 * @param text
	 *            the text
	 * @param size
	 *            the size
	 */
	protected MenuItem(IScene scene, String text, Vec size) {
		super(scene, size);
		this.text = text;
	}

	/**
	 * Get the text of the item.
	 *
	 * @return the text
	 */
	protected String getText() {
		return this.text;
	}

	/**
	 * Set a hover (e.g. a mouseover)
	 *
	 * @param value
	 *            the hover
	 */
	public void setHover(boolean value) {
		this.hover = value;
	}

	/**
	 * Select the Item.
	 */
	public void select() {
		this.selected = true;
	}

	/**
	 * Deselect the Item.
	 */
	public void unselect() {
		this.selected = false;
		if (this.parent != null) {
			this.parent.focus();
		}
	}

	/**
	 * This method will be invoked to indicate an UP request.
	 */
	public void up() {

	}

	/**
	 * This method will be invoked to indicate an DOWN request.
	 */
	public void down() {

	}

	/**
	 * This method will be invoked to indicate an LEFT request.
	 */

	public void left() {

	}

	/**
	 * This method will be invoked to indicate an RIGHT request.
	 */
	public void right() {

	}

	/**
	 * This method will be invoked to indicate an focus request.
	 */
	protected void focus() {

	}

	/**
	 * Set the parent of the MenuItem.
	 *
	 * @param i
	 *            the parent
	 */
	public void setParent(MenuItem i) {
		this.parent = i;
	}

	@Override
	protected void internalRender(Field f) {
		this.renderItem(f);
	}

	/**
	 * Render the Item itself.
	 *
	 * @param f
	 *            the field
	 */
	protected void renderItem(Field f) {
		f.drawRectangle(this.getPos(), this.getSize(), this.hover ? GameConf.MENU_BOX_SELECT_COLOR : GameConf.MENU_BOX_COLOR, false);
		f.drawText(this.getPos(), this.getText(), GameConf.MENU_TEXT, false);
	}

	@Override
	public String toString() {
		return this.text;
	}

}
