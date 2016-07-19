package de.selebrator.gui;

import de.selebrator.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public abstract class GUI implements Listener {
	
	public NPC npc;

	public static final int[][] PATTERNS =
			{
					{},
					{4},
					{3, 5},
					{3, 4, 5},
					{2, 3, 5, 6},
					{2, 3, 4, 5, 6},
					{1, 2, 3, 5, 6, 7},
					{1, 2, 3, 4, 5, 6, 7},
					{0, 1, 2, 3, 5, 6, 7, 8},
					{0, 1, 2, 3, 4, 5, 6, 7, 8}
			};



	public GUI(NPC npc, Plugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

		this.npc = npc;
	}

	public abstract void open(Player player);
	
	public int[][] getSmartPattern(int items) {
		return new int[][] {
				PATTERNS[items >=  9 ? 9 : items % 9],
				PATTERNS[items >= 18 ? 9 : items >  9 ? items % 9 : 0],
				PATTERNS[items >= 27 ? 9 : items > 18 ? items % 9 : 0],
				PATTERNS[items >= 36 ? 9 : items > 27 ? items % 9 : 0],
				PATTERNS[items >= 45 ? 9 : items > 36 ? items % 9 : 0],
				PATTERNS[items >= 54 ? 9 : items > 45 ? items % 9 : 0]};
	}

	public Inventory getSmartInventory(int items, String name) {
		return Bukkit.getServer().createInventory(null, items + (9 - (items % 9)), name);
	}
}
