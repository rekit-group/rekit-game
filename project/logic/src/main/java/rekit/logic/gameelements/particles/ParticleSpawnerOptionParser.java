package rekit.logic.gameelements.particles;

import org.fuchss.configuration.parser.Parser;

/**
 * This class realize a parser for a {@link ParticleSpawnerOption}.
 *
 * @author Dominik Fuchss
 *
 */
public final class ParticleSpawnerOptionParser implements Parser {
	/**
	 * The Float-Regex which will be used.
	 */
	private static final String FLOAT_REGEX = "(-|\\+)?[0-9]+\\.[0-9]+(f|F)";

	@Override
	public synchronized Object parseIt(String definition, String[] path) throws Exception {
		ParticleSpawnerOption opt = null;
		if (definition.matches("(" + ParticleSpawnerOptionParser.FLOAT_REGEX + ",){3}" + ParticleSpawnerOptionParser.FLOAT_REGEX)) {
			// 4 Params
			String[] floats = definition.split(",");
			opt = new ParticleSpawnerOption(//
					Float.parseFloat(floats[0]), Float.parseFloat(floats[1]), //
					Float.parseFloat(floats[2]), Float.parseFloat(floats[3]));
		} else if (definition.matches(ParticleSpawnerOptionParser.FLOAT_REGEX + "," + ParticleSpawnerOptionParser.FLOAT_REGEX)) {
			// 2 Params
			String[] floats = definition.split(",");
			opt = new ParticleSpawnerOption(Float.parseFloat(floats[0]), Float.parseFloat(floats[1]));
		} else if (definition.matches(ParticleSpawnerOptionParser.FLOAT_REGEX)) {
			// 1 Param
			opt = new ParticleSpawnerOption(Float.parseFloat(definition));
		}

		return opt;
	}

}