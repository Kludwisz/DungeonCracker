package kludwisz.cracker;

import java.util.List;

public class TaskOutput {
	public final List<Long> dungeonSeeds;
	public final List<Long> structureSeeds;
	public final List<Long> worldSeeds;
	public final int [] size;
	
	public TaskOutput(List<Long> ds, List<Long> ss, List<Long> ws) {
		this.dungeonSeeds = ds;
		this.structureSeeds = ss;
		this.worldSeeds = ws;
		
		this.size = new int[3];
		this.size[0] = ds.size();
		this.size[1] = ss.size();
		this.size[2] = ws.size();
	}
	
	public List<Long> get(int index){
		if (index==0)
			return this.dungeonSeeds;
		else if (index==1)
			return this.structureSeeds;
		else if (index==2)
			return this.worldSeeds;
		else return null;
	}
}
