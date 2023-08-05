package kludwisz.cracker;

public class ReverserInstruction {
	public Type type;
	public int minCallCount;
	public int maxCallCount;
	
	public static enum Type {
		NEXTINT,
		FILTEREDSKIP,
		SKIP,
		MUTABLE_SKIP;
	}
	
	public ReverserInstruction(ReverserInstruction.Type type, int callsMin, int callsMax) {
		this.type = type;
		this.minCallCount = callsMin;
		this.maxCallCount = callsMax;
	}

	public ReverserInstruction(ReverserInstruction.Type type) {
		this(type, 1, 1);
	}

	public static ReverserInstruction get(int index) {
		if (index == 0) {
			return new ReverserInstruction(ReverserInstruction.Type.FILTEREDSKIP);
		} else if (index == 1){
			return new ReverserInstruction(ReverserInstruction.Type.NEXTINT);
		} else if (index == 3){
			return new ReverserInstruction(ReverserInstruction.Type.MUTABLE_SKIP, 0, 1);
		} else if (index == 4){
			return new ReverserInstruction(ReverserInstruction.Type.SKIP);
		}
		return null;
	}
}
