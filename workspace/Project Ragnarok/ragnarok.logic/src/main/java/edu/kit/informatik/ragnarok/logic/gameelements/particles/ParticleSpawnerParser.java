package edu.kit.informatik.ragnarok.logic.gameelements.particles;

import java.lang.reflect.Field;

import edu.kit.informatik.ragnarok.visitor.Visitable;
import edu.kit.informatik.ragnarok.visitor.Visitor;
import edu.kit.informatik.ragnarok.visitor.parser.OptionParser;
import edu.kit.informatik.ragnarok.visitor.visitors.MapVisitor;

/**
 *
 * @author Dominik Fuchß
 *
 */
public final class ParticleSpawnerParser extends OptionParser {

	@Override
	protected synchronized void apply(Visitable obj, Field field) throws Exception {
		ParticleSpawner spawner = new ParticleSpawner();
		Visitor v = new MapVisitor(this.mapping);
		v.setParser(ParticleSpawnerOption.class, new ParticleSpawnerOptionParser());
		v.visitMe(spawner);
		field.set(obj, spawner);
	}

}
