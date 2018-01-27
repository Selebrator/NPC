package de.selebrator.plugin.npc.gui;

import de.selebrator.npc.entity.EntityNPC;
import de.selebrator.plugin.npc.fetcher.ItemBuilder;
import org.apache.commons.lang3.ClassUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;

public class EntityEffectGUI extends GUI {

	public EntityEffectGUI(EntityNPC npc, Plugin plugin) {
		super(npc, plugin);
	}

	public void open(Player player) {
		EntityEffect[] set = EntityEffect.values();
		Inventory inventory = Bukkit.getServer().createInventory(null, getInventorySize(set.length), npc.getCustomName() + " - EntityEffect");
		int item = 0;

		for(int row = 0; row < inventory.getSize() / 9; row++) {
			boolean[] pattern = getPattern(set.length - (9 * row));
			for(int cell = 0; cell < pattern.length; cell++) {
				if(pattern[cell]) {
					inventory.setItem((9 * row) + cell, new ItemBuilder(Material.INK_SACK, 1, (short) (ClassUtils.isAssignable(npc.getType().getEntityClass(), set[item].getApplicable()) ? 10 : 8), set[item++].name()).build());
				}
			}
		}
		player.openInventory(inventory);
	}

	@EventHandler
	public void onGUIClick(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		if(inventory != null && inventory.equals(event.getView().getTopInventory()))
			if(inventory.getName().contains(" - EntityEffect")) {
				ItemStack item = event.getCurrentItem();
				if(item != null && item.getType() == Material.INK_SACK) {
					npc.playEffect(EntityEffect.valueOf(item.getItemMeta().getDisplayName()));
				}
				event.setCancelled(true);
				this.open((Player) event.getWhoClicked());
			}
	}
}
