package rekit.logic.scene;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import rekit.config.GameConf;
import rekit.logic.GameModel;
import rekit.logic.ILevelScene;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.GameElementFactory;
import rekit.logic.level.LevelFactory;
import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.LevelType;
import rekit.primitives.geometry.Vec;
import rekit.tests.Pizza;
import rekit.util.LambdaUtil;

/**
 * A test scene which can be used in {@link GameConf#DEBUG} context.
 *
 * @author Matthias Schmitt
 *
 */
public final class TestScene extends LevelScene {

	private TestScene(GameModel model) {
		super(model, LevelFactory.createLevel(LambdaUtil.invoke(TestScene::getTestLevel)));
	}

	private static LevelDefinition getTestLevel() throws IOException {
		PathMatchingResourcePatternResolver resolv = new PathMatchingResourcePatternResolver();
		Resource res = resolv.getResource("/levels/test.dat");
		return new LevelDefinition(res.getInputStream(), LevelType.Test);
	}

	/**
	 * Create the scene by model and options
	 *
	 * @param model
	 *            the model
	 * @param options
	 *            the options
	 * @return the new scene
	 */
	public static ILevelScene create(GameModel model, String... options) {
		return new TestScene(model);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void start() {
		super.start();
		GameElement protoPizza = GameElementFactory.getPrototype("Pizza");
		Pizza pizza = (Pizza) protoPizza.create(new Vec(12, 4));
		// TODO Sth more useful ..
		this.addGameElement(pizza);
	}
}
