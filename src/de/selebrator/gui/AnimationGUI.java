package de.selebrator.gui;

import de.selebrator.fetcher.ItemBuilder;
import de.selebrator.npc.NPC;
import de.selebrator.npc.event.NPCAnimationEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class AnimationGUI extends GUI implements Listener {

	public AnimationGUI(NPC npc, Plugin plugin) {
		super(npc, plugin);
	}

	public void open(Player player) {
		NPCAnimationEvent.Animation[] set = NPCAnimationEvent.Animation.values();
		Inventory inventory = Bukkit.getServer().createInventory(null, getInventorySize(set.length), npc.getName() + " - Animation");
		int item = 0;

		for(int row = 0; row < inventory.getSize() / 9; row++) {
			boolean[] pattern = getPattern(set.length - (9 * row));
			for(int cell = 0; cell < pattern.length; cell++) {
				if(pattern[cell]) {
					inventory.setItem((9 * row) + cell, new ItemBuilder(Material.INK_SACK, 1, (short) 10, set[item++].name()).build());
				}
			}
		}
		player.openInventory(inventory);
	}

	@EventHandler
	public void onGUIClick(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		if(inventory != null && inventory.equals(event.getView().getTopInventory()))
			if(inventory.getName().contains(" - Animation")) {
				ItemStack item = event.getCurrentItem();
				if(item != null && item.getType() == Material.INK_SACK) {
					npc.playAnimation(NPCAnimationEvent.Animation.valueOf(item.getItemMeta().getDisplayName()));
				}
				event.setCancelled(true);
				this.open((Player) event.getWhoClicked());
			}
	}
}
