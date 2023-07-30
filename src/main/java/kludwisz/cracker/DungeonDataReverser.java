package kludwisz.cracker;

import kludwisz.gui.DungeonCracker;

public class DungeonDataReverser implements Runnable{

	@Override
	public void run() {
		String sequence = DungeonCracker.floor.getSequence();
		
		// TODO:
		// - identify all unknown blocks
		// - use recursive function to generate possible layouts (without unknowns, limit possibilities to a set number)
		// - for each possible layout, run lattiCG code to find dungeon seeds
		// - reverse all dungeon seeds to structure seeds
		// - gather results and kill thread
	}
}
