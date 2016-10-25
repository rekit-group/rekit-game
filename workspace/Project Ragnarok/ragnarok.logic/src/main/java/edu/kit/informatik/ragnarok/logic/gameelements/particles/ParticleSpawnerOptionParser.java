package edu.kit.informatik.ragnarok.logic.gameelements.particles;

import java.lang.reflect.Field;

import home.fox.visitors.Visitable;
import home.fox.visitors.parser.Parser;

/**
 * This class realize a parser for a {@link ParticleSpawnerOption}.
 *
 * @author Dominik Fuchß
 *
 */
public final class ParticleSpawnerOptionParser implements Parser {
	/**
	 * The Float-Regex which will be used.
	 */
	private static final String FLOAT_REGEX = "(-|\\+)?[0-9]+\\.[0-9]+(f|F)";

	@Override
	public synchronized boolean parse(Visitable obj, Field field, String definition) throws Exception {
		if (!Parser.super.parse(obj, field, definition)) {
			return false;
		}
		ParticleSpawnerOption opt = null;
		if (definition.matches("(" + ParticleSpawnerOptionParser.FLOAT_REGEX + ",){3}" + ParticleSpawnerOptionParser.FLOAT_REGEX)) {
			// 4 Params
			String[] floats = definition.split(",");
			opt = new ParticleSpawnerOption( //
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

		if (opt != null) {
			field.set(obj, opt);
		}
		return opt != null;
	}

}