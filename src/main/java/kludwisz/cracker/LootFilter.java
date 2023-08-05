package kludwisz.cracker;

import java.util.ArrayList;
import java.util.List;

import com.seedfinding.mcfeature.loot.LootContext;
import com.seedfinding.mcfeature.loot.MCLootTables;
import com.seedfinding.mcfeature.loot.item.ItemStack;

public class LootFilter {
	private final LootContext ctx = new LootContext(0L);
	public ArrayList<LootCondition> conditions = new ArrayList<>();
	
	public LootFilter(String rawCode) throws Exception {
		this.compile(rawCode);
	}
	
	public boolean test(long lootSeed) {
		if (this.conditions.size() == 0)
			return true;
		this.ctx.setSeed(lootSeed);
		List<ItemStack> items = MCLootTables.SIMPLE_DUNGEON_CHEST.generate(ctx);
		//System.out.println(items);
		boolean flag = false;
		
		for (LootCondition cond : this.conditions) {
			flag = false;
			for (ItemStack is : items) {
				flag |= cond.test(is);
			}
			if (!flag)
				break;
		}
		//System.out.println(flag);
		
		return flag;
	}
	
	private void compile(String rawCode) throws Exception {
		String [] lines = rawCode.split("\n");
		
		for (String line : lines) {
			if (line.equals(""))
				continue;
			if (line.charAt(0) == '/' && line.charAt(1) == '/')
				continue;
			String [] phrases = line.split(" ");
			if (phrases.length != 3)
				throw new Exception("Invalid instruction: " + line);
			
			LootCondition cond = new LootCondition(phrases[0], Integer.parseInt(phrases[1]), phrases[2]);
			this.conditions.add(cond);
		}
	}
}
