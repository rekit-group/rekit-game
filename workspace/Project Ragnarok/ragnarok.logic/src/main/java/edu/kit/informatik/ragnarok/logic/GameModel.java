package edu.kit.informatik.ragnarok.logic;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.GuiElement;

public interface GameModel extends Model {

	void addGameElement(GameElement element);

	@Override
	Player getPlayer();

	void setCameraTarget(CameraTarget fixedCameraTarget);

	void addBossText(String name);

	void removeGameElement(GameElement element);

	void removeGuiElement(GuiElement element);

}
