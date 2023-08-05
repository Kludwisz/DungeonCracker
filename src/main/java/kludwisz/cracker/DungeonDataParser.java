package kludwisz.cracker;

import java.util.ArrayList;
import java.util.List;

import kludwisz.gui.DungeonCracker;

// 0 mossy
// 1 cobble
// 2 air
// 3 unknown (aka the villain)
// 4 unknown solid

public class DungeonDataParser {
	public List<List<ReverserInstruction>> getAllPossibilities() {
		
		// identyfing blocks of unknowns (if there are any)
		String seq = DungeonCracker.floor.getSequence();
		ArrayList<ReverserInstruction> instr = new ArrayList<>();
		
		char lastChar = '_';
		for (int i=0; i<seq.length(); i++) {
			char c = seq.charAt(i);
			if (instr.size()>0 && c == lastChar && (lastChar == '3' || lastChar == '4'))
				instr.get(instr.size()-1).maxCallCount++;
			else {
				ReverserInstruction in = ReverserInstruction.get(Integer.parseInt(c+""));
				if (in != null)
					instr.add(in);
			}
			if (c != '2')
				lastChar = c; // air doesn't interrupt the sequence
		}
		
		// removing useless skips & mutable skips at the end of the list
		int ix = instr.size()-1;
		while (ix>0 && (instr.get(ix).type == ReverserInstruction.Type.SKIP || instr.get(ix).type == ReverserInstruction.Type.MUTABLE_SKIP)) {
			instr.remove(ix);
			ix--;
		}
		original = instr;
		
		// now that we have a list of instructions
		// we need to generate all possibilities to run latticg
		generateRecursive(new ArrayList<ReverserInstruction>(), 0);
		
		if (safetyCounter > 128)
			return null;
		
		//System.out.println(result.size());
		return result;
	}
	
	private List<ReverserInstruction> original;
	private ArrayList<List<ReverserInstruction>> result = new ArrayList<>();
	private int safetyCounter = 0;
	private void generateRecursive(ArrayList<ReverserInstruction> current, int ix) {
		if (safetyCounter > 128) return;
		
		while (ix < original.size()) {
			ReverserInstruction i = original.get(ix);
		
			if (i.type == ReverserInstruction.Type.MUTABLE_SKIP) {
				
				// converting mutable skip to branched skips
				for (int calls=i.minCallCount; calls<=i.maxCallCount; calls++) {
					ArrayList<ReverserInstruction> newList = new ArrayList<>(current);
					if (calls != 0)
						newList.add(new ReverserInstruction(ReverserInstruction.Type.SKIP, calls, calls));
					if (ix < original.size())
						generateRecursive(newList, ix+1);
					else {
						result.add(newList);
						safetyCounter++;
					}
				}
				break;
			}
			else {
				current.add(i);
				ix++;
				if (ix >= original.size()) {
					result.add(current);
					safetyCounter++;
				}
			}
		}
			
	}
}
