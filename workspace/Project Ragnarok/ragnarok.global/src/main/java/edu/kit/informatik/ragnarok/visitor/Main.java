package edu.kit.informatik.ragnarok.visitor;

import edu.kit.informatik.ragnarok.config.GameConf;

public class Main {

	public static void main(String[] args) {
		Visitor.visitStatic(GameConf.class);

	}

}
