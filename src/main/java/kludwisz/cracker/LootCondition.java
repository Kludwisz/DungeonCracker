package kludwisz.cracker;

import com.seedfinding.mcfeature.loot.item.ItemStack;

public class LootCondition {
	private final String itemName;
	private final int itemCount;
	private final ConditionType type;
	
	public LootCondition(String operator, int count, String name) throws Exception {
		this.itemCount = count;
		this.itemName = name;
		switch (operator) {
		case ">":
			this.type = ConditionType.GREATER_THAN;
			break;
		case "<":
			this.type = ConditionType.LESS_THAN;
			break;
		case "=":
			this.type = ConditionType.EQUAL;
			break;
		default:
			throw new Exception("Illegal operator in filter instruction: " + operator);
		}
	}
	
	public boolean test(ItemStack is) {
		if (!is.getItem().getName().equals(this.itemName))
			return false;
		if (this.type == ConditionType.EQUAL && is.getCount() == this.itemCount)
			return true;
		if (this.type == ConditionType.GREATER_THAN && is.getCount() > this.itemCount)
			return true;
		if (this.type == ConditionType.LESS_THAN && is.getCount() < this.itemCount)
			return true;
		return false;
	}
	
	private static enum ConditionType {
		EQUAL,
		GREATER_THAN,
		LESS_THAN;
	}
}
