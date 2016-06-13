package edu.kit.informatik.ragnarok.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.kit.informatik.ragnarok.controller.commands.*;
import edu.kit.informatik.ragnarok.logic.Direction;
import edu.kit.informatik.ragnarok.logic.GameModel;

public class Controller implements Observer {
	private Map<Integer, InputCommand> mpCmd;
	
	public Controller() {
		
	}
	
	public void setModel(GameModel model) {
		this.mpCmd = new HashMap<Integer, InputCommand>();
		
		// Initialize jump command
		InputCommand jmpCmd = new JumpCommand();
		jmpCmd.setEntity(model.getPlayer());
		
		// Initialize left walking command
		InputCommand leftCmd = new WalkCommand(Direction.LEFT);
		leftCmd.setEntity(model.getPlayer());
		
		// Initialize right walking command
		InputCommand rightCmd = new WalkCommand(Direction.RIGHT);
		rightCmd.setEntity(model.getPlayer());
		
		this.mpCmd.put(119, jmpCmd);
		this.mpCmd.put(97, leftCmd);
		this.mpCmd.put(100, rightCmd);
	}
	
	public void handleEvent(int id) {
		// return if we do not have a command defined for this key
		if (!this.mpCmd.containsKey(id)) {
			return;
		}
		this.mpCmd.get(id).apply();
	}

	public void start() {
		// Register me! --> update is called whenever something changes
		InputHelper.register(this);
	}
	
	@Override
	public void update() {
		Iterator<Integer> it = InputHelper.getKeyIterator();
		while (it.hasNext()) {
			int next = it.next();
			handleEvent(next);
		}
	}

}
