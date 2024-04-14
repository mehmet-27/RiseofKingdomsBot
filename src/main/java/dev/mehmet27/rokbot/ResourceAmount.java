package dev.mehmet27.rokbot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResourceAmount {

	private int food;
	private int wood;
	private int stone;
	private int gold;

	public ResourceAmount(int food, int wood, int stone, int gold) {
		this.food = food;
		this.wood = wood;
		this.stone = stone;
		this.gold = gold;
	}

	public Map<ResourceType, Integer> getAsSortedMap() {
		Map<ResourceType, Integer> map = Map.of(ResourceType.FOOD, food, ResourceType.WOOD, wood, ResourceType.STONE, stone, ResourceType.GOLD, gold);
		LinkedHashMap<ResourceType, Integer> sortedMap = new LinkedHashMap<>();
		ArrayList<Integer> list = new ArrayList<>();
		for (Map.Entry<ResourceType, Integer> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		list.sort(Comparator.naturalOrder());
		for (Integer integer : list) {
			for (Map.Entry<ResourceType, Integer> entry : map.entrySet()) {
				if (entry.getValue().equals(integer)) {
					sortedMap.put(entry.getKey(), integer);
				}
			}
		}
		return sortedMap;
	}

	public int getFood() {
		return food;
	}

	public int getWood() {
		return wood;
	}

	public int getStone() {
		return stone;
	}

	public int getGold() {
		return gold;
	}

	public enum ResourceType {
		FOOD, WOOD, STONE, GOLD
	}
}
