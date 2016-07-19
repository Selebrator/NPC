package de.selebrator.gui;

import de.selebrator.fetcher.ItemBuilder;
import de.selebrator.npc.NPC;
import de.selebrator.npc.event.NPCAnimationEvent;
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
		Inventory inventory = getSmartInventory(set.length, npc.getName() + " - Animation");
		int[][] pattern = getSmartPattern(set.length);

		for(int line = 0; line < pattern.length; line++) {
			for(int i = 0; i < pattern[line].length; i++) {
				inventory.setItem((9 * line) + pattern[line][i], new ItemBuilder(Material.INK_SACK, 1, (short) 10, set[(9 * line) + i].name()).build());
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
