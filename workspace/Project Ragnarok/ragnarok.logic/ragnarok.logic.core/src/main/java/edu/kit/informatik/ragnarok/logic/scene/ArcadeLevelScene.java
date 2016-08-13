package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.level.LevelManager;

public class ArcadeLevelScene extends LevelScene {

	private final int arcadeLevelId;

	public ArcadeLevelScene(GameModel model, int arcadeLevelId) {
		super(model, LevelManager.getArcadeLevel(arcadeLevelId));

		this.arcadeLevelId = arcadeLevelId;
	}

	public static Scene create(GameModel model, String[] options) {
		// options[0] should be an Integer with the arcadeLevelId
		if (options == null || options.length < 1) {
			throw new IllegalArgumentException("cant switch to unspecified arcade level");
		}
		int arcadeId = Integer.parseInt(options[0]);
		return new ArcadeLevelScene(model, arcadeId);
	}

	public int getArcadeId() {
		return this.arcadeLevelId;
	}

}
