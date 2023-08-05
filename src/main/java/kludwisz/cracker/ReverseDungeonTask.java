package kludwisz.cracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.seedfinding.latticg.reversal.DynamicProgram;
import com.seedfinding.latticg.reversal.calltype.java.JavaCalls;
import com.seedfinding.latticg.util.LCG;
import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mccore.util.math.NextLongReverser;
import com.seedfinding.mccore.util.math.Vec3i;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcmath.util.Mth;
import com.seedfinding.mcreversal.ChunkRandomReverser;

import javafx.concurrent.Task;
import kludwisz.gui.GenerationOptions.BiomeType;
import kludwisz.gui.LootFilterOptions;

public class ReverseDungeonTask extends Task<TaskOutput>{
	private final Vec3i spawner;
	private final List<Long> salts;
	private final MCVersion version;
	
	public ReverseDungeonTask(int x, int y, int z, MCVersion version, BiomeType biome) {
		this.spawner = new Vec3i(x,y,z);
		this.version = version;
		
		if (version.isNewerThan(MCVersion.v1_15)) {
			if (biome == BiomeType.DESERT) {
				this.salts = List.of(30003L);
			} else if (biome == BiomeType.NOTDESERT) {
				this.salts = List.of(30002L);
			} else {
				this.salts = List.of(30002L, 30003L);
			}
		}
		else {
			this.salts = List.of(20003L);
		}
			
	}
	
	protected TaskOutput call() throws Exception {
		DungeonDataParser ddp = new DungeonDataParser();
		
		List<List<ReverserInstruction>> possibilities = ddp.getAllPossibilities();
		if (possibilities == null) {
			this.updateMessage("Error: too many possibilities");
			return null;
		}
		
		int taskCount=0;
		LootFilter filter = null;
		
		if (LootFilterOptions.instance.filteringEnabled) {
			try {
				filter = new LootFilter(LootFilterOptions.instance.filterInputArea.getText());
			} catch (Exception ex) {
				System.err.println("WARN: Failed to compile loot filter: " + ex.getMessage());
				this.updateMessage("Failed to compile loot filter");
				return null;
			}
		}
		
		String filterInfo = LootFilterOptions.instance.filteringEnabled ? " (loot filter ON)" : " (loot filter OFF)";
		HashSet<Long> structSeedsNoDupes = new HashSet<>();
		HashSet<Long> dungeonSeedsNoDupes = new HashSet<>();
		
		try {
		for (List<ReverserInstruction> program : possibilities) {
			this.updateMessage("Running... " +(++taskCount)+ "/" + possibilities.size() + filterInfo);
			
			DynamicProgram device = DynamicProgram.create(LCG.JAVA);
			
			// spawner position calls
			int offsetX = this.spawner.getX() & 15;
			int y = this.spawner.getY();
			int offsetZ = this.spawner.getZ() & 15;
			
			if (this.version.isBetween(MCVersion.v1_8, MCVersion.v1_14)) {
				device.add(JavaCalls.nextInt(16).equalTo(offsetX));
				device.add(JavaCalls.nextInt(256).equalTo(y));
				device.add(JavaCalls.nextInt(16).equalTo(offsetZ));
			} else {
				device.add(JavaCalls.nextInt(16).equalTo(offsetX));
				device.add(JavaCalls.nextInt(16).equalTo(offsetZ));
				device.add(JavaCalls.nextInt(256).equalTo(y));
			}
			
			device.skip(2);
			
			// dungeon floor calls
			float info = prepareReverser(device, program);
			if (info <= 32.0F) { // only taking floors that have at least 16 bits of info
				//System.err.println("Too little information!");
				this.updateMessage("ERROR: Not enough information!");
				return null;
			}
			
			List<Long> dungeonSeedsXored = device.reverse().boxed().collect(Collectors.toList());
			ChunkRand rand = new ChunkRand();
			
			ArrayList<Long> filteredSeeds;
			if (!LootFilterOptions.instance.filteringEnabled) {
				// skipping loot filtering
				filteredSeeds = new ArrayList<>(dungeonSeedsXored);
			}
			else {
				// filtering loot
				filteredSeeds = new ArrayList<>();
				for (long internalSeed : dungeonSeedsXored) {			
					boolean lootMatches = false;
					for (int calls=17; calls<=100; calls++) { // 17 is the theoretical lowest amount of calls before loot gen
						rand.setSeed(internalSeed, false);	  // 95 is the theoretical max, 100 accounts for
						rand.advance(calls);				  // any potential bounded nextInt extra calls
							
						lootMatches = filter.test(rand.nextLong());
						if (lootMatches) break;
					}
					if (lootMatches)
						filteredSeeds.add(internalSeed);
				}
			}
			//System.out.println(filteredSeeds.size());
			
			for (long seed : filteredSeeds) {
				dungeonSeedsNoDupes.add(seed);
				
				for (long salt : this.salts) {
					rand.setSeed(seed, false);
					
					for (int i=0; i<8; i++) {
						long popseed = (rand.getSeed() ^ LCG.JAVA.multiplier) - salt;
						List<Long> partialStructSeeds = ChunkRandomReverser.reversePopulationSeed(popseed, (spawner.getX()>>4)<<4, (spawner.getZ()>>4)<<4, MCVersion.v1_14);
						
						for (long ss : partialStructSeeds) {
							ss &= Mth.MASK_48;
							structSeedsNoDupes.add(ss);
						}
						
						rand.advance(-5);
					}
				}
			}
		}
		
		HashSet<Long> worldSeedsNoDupes = new HashSet<>();
		for (long structseed : structSeedsNoDupes) {
			worldSeedsNoDupes.addAll(NextLongReverser.getNextLongEquivalents(structseed));
		}

		this.updateMessage("Finished.");
		return new TaskOutput(List.copyOf(dungeonSeedsNoDupes), List.copyOf(structSeedsNoDupes), List.copyOf(worldSeedsNoDupes));
		}
		catch (OutOfMemoryError err) {
			this.updateMessage("RUNTIME ERROR: Out of memory!");
			return null;
		}
	}
	
	public static float prepareReverser(DynamicProgram device, List<ReverserInstruction> program) {
		float infoBits = 16.0F;
		
		for (ReverserInstruction i : program) {
			//System.out.println(i.type.name() + " " + i.minCallCount + " " + i.maxCallCount);
			switch (i.type) {
			case NEXTINT:
				device.add(JavaCalls.nextInt(4).equalTo(0));
				infoBits += 2.0F;
				break;
			case FILTEREDSKIP:
				device.filteredSkip(r -> r.nextInt(4)!=0, 1);
				infoBits += 0.4F;
				break;
			case SKIP:
				device.skip(i.maxCallCount);
				break;
			case MUTABLE_SKIP:
				System.err.println("ERROR: Mutable skip call encountered during reverser setup");
				break;
			}
		}
		
		return infoBits;
	}
}
