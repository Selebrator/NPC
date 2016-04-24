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

public class AnimationGUI implements Listener {

	private NPC npc;

	public AnimationGUI(NPC npc, Plugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

		this.npc = npc;

		if(NPCAnimationEvent.Animation.values().length != 8) {
			throw new UnsupportedClassVersionError(this.getClass().getSimpleName() + "is out of date");
		}
	}

	public void open(Player player) {
		Inventory inventory = Bukkit.getServer().createInventory(null, 9, npc.getName() + " - Animation");

		inventory.setItem(0, new ItemBuilder(Material.INK_SACK, 1, (short) 10, NPCAnimationEvent.Animation.values()[0].name()).build());
		inventory.setItem(1, new ItemBuilder(Material.INK_SACK, 1, (short) 10, NPCAnimationEvent.Animation.values()[1].name()).build());
		inventory.setItem(2, new ItemBuilder(Material.INK_SACK, 1, (short)  8, NPCAnimationEvent.Animation.values()[2].name()).build());
		inventory.setItem(3, new ItemBuilder(Material.INK_SACK, 1, (short) 10, NPCAnimationEvent.Animation.values()[3].name()).build());
		inventory.setItem(5, new ItemBuilder(Material.INK_SACK, 1, (short) 10, NPCAnimationEvent.Animation.values()[4].name()).build());
		inventory.setItem(6, new ItemBuilder(Material.INK_SACK, 1, (short) 10, NPCAnimationEvent.Animation.values()[5].name()).build());
		inventory.setItem(7, new ItemBuilder(Material.INK_SACK, 1, (short)  8, NPCAnimationEvent.Animation.values()[6].name()).build());
		inventory.setItem(8, new ItemBuilder(Material.INK_SACK, 1, (short)  8, NPCAnimationEvent.Animation.values()[7].name()).build());

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
