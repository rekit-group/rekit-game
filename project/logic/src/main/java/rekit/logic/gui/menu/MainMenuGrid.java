package rekit.logic.gui.menu;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.IScene;
import rekit.primitives.geometry.Vec;
import rekit.primitives.time.Timer;

/**
 *
 * This class realizes the Main Menu displaying a list of MenuItems.
 *
 */
public class MainMenuGrid extends MenuGrid {
	/**
	 * The timer to switch between the different logo images.
	 */
	private Timer frameTimer;
	
	/**
	 * The number of the currently shown logo image.
	 */
	private int currentFrame;
	
	public MainMenuGrid(IScene scene, String text, int colCount) {
		super(scene, text, colCount, 425, 100);
		this.setItemSize(new Vec(425, 100));
		this.frameTimer = new Timer(120);
	}
	
	@Override
	public void logicLoop() {
		super.logicLoop();
		this.frameTimer.logicLoop();
		if (this.frameTimer.timeUp()) {
			this.frameTimer.reset();
			currentFrame = (currentFrame + 1) % 3;
		}
	}
	
	@Override
	protected void internalRender(GameGrid f) {
		super.internalRender(f);
		if (this.isVisible()) {
			f.drawImage(new Vec(GameConf.GRID_W / 2f, 2.2f), new Vec(8, 5), "logo_" + this.currentFrame + ".png");
		}
	}

}
