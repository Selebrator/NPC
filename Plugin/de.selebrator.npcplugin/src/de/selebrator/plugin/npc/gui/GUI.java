package de.selebrator.plugin.npc.gui;

import de.selebrator.npc.entity.EntityNPC;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class GUI implements Listener {

	public EntityNPC npc;

	public GUI(EntityNPC npc, Plugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

		this.npc = npc;
	}

	public static boolean[] getPattern(int items) {
		return new boolean[] {
				items >= 8,
				items >= 6,
				items >= 4,
				items >= 2,
				items % 2 == 1 || items >= 10,
				items >= 2,
				items >= 4,
				items >= 6,
				items >= 8
		};
	}

	public static int getInventorySize(int items) {
		return items + (9 - (items % 9 == 0 ? 9 : items % 9));
	}
}
