package de.selebrator.gui;

import de.selebrator.fetcher.ItemBuilder;
import de.selebrator.fetcher.LeatherArmorBuilder;
import de.selebrator.npc.inventory.FakeEquipment;
import de.selebrator.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EquipEditor implements Listener {

	private NPC npc;
	private String selected;
	private String subSelected;

	public EquipEditor(NPC npc, Plugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

		this.npc = npc;
	}

	public void open(Player player) {
		ItemStack filler = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 8, " ").build();
		ItemStack marker = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5, " ").build();

		Inventory inventory = Bukkit.getServer().createInventory(null, 6 * 9, npc.getName() + " - Equip");

		inventory.setItem(11, npc.getEquipment().getMainHand() == null ? new ItemBuilder(Material.STICK, 1, (short) 0, "§8Empty MainHand").build() : npc.getEquipment().getMainHand());
		inventory.setItem(13, npc.getEquipment().getHelmet() == null ? new LeatherArmorBuilder(Material.LEATHER_HELMET, 1, (short) 0, "§8No Helmet", Color.fromBGR(76, 76, 76)).build() : npc.getEquipment().getHelmet());
		inventory.setItem(14, npc.getEquipment().getChestplate() == null ? new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE, 1, (short) 0, "§8No Chestplate", Color.fromBGR(76, 76, 76)).build() : npc.getEquipment().getChestplate());
		inventory.setItem(15, npc.getEquipment().getLeggings() == null ? new LeatherArmorBuilder(Material.LEATHER_LEGGINGS, 1, (short) 0, "§8No Leggings", Color.fromBGR(76, 76, 76)).build() : npc.getEquipment().getLeggings());
		inventory.setItem(16, npc.getEquipment().getBoots() == null ? new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1, (short) 0, "§8No Boots", Color.fromBGR(76, 76, 76)).build() : npc.getEquipment().getBoots());

		if(selected != null) {
			ItemStack enchant = new ItemStack(Material.ENCHANTED_BOOK, 1);
			ItemStack clear = new ItemBuilder(Material.BARRIER, 1, (short) 0, "§cClear").build();

			inventory.setItem(21, enchant);
			inventory.setItem(23, clear);

			switch(selected) {
				case "MAIN_HAND":
					inventory.setItem(2, marker);
					break;
				case "HELMET":
					inventory.setItem(4, marker);
					break;
				case "CHESTPLATE":
					inventory.setItem(5, marker);
					break;
				case "LEGGINGS":
					inventory.setItem(6, marker);
					break;
				case "BOOTS":
					inventory.setItem(7, marker);
					break;
				default:
					break;

			}

			if(selected.equals("MAIN_HAND")) {
				inventory.setItem(29, new ItemBuilder(Material.IRON_SWORD, 1, (short) 0, "Sword").build());
				inventory.setItem(30, new ItemBuilder(Material.IRON_PICKAXE, 1, (short) 0, "Pickaxe").build());
				inventory.setItem(31, new ItemBuilder(Material.IRON_AXE, 1, (short) 0, "Axe").build());
				inventory.setItem(32, new ItemBuilder(Material.IRON_SPADE, 1, (short) 0, "Shovel").build());
				inventory.setItem(33, new ItemBuilder(Material.IRON_HOE, 1, (short) 0, "Hoe").build());
				inventory.setItem(40, new ItemStack(Material.BOW, 1));

				if(subSelected != null) {
					inventory.setItem(38, new ItemStack(Material.valueOf("WOOD_" + subSelected), 1));
					inventory.setItem(39, new ItemStack(Material.valueOf("GOLD_" + subSelected), 1));
					inventory.setItem(40, new ItemStack(Material.valueOf("STONE_" + subSelected), 1));
					inventory.setItem(41, new ItemStack(Material.valueOf("IRON_" + subSelected), 1));
					inventory.setItem(42, new ItemStack(Material.valueOf("DIAMOND_" + subSelected), 1));
				}
			} else if(selected.equals("HELMET") || selected.equals("CHESTPLATE") || selected.equals("LEGGINGS") || selected.equals("BOOTS")) {
				inventory.setItem(38, new ItemStack(Material.valueOf("LEATHER_" + selected), 1));
				inventory.setItem(39, new ItemStack(Material.valueOf("GOLD_" + selected), 1));
				inventory.setItem(40, new ItemStack(Material.valueOf("CHAINMAIL_" + selected), 1));
				inventory.setItem(41, new ItemStack(Material.valueOf("IRON_" + selected), 1));
				inventory.setItem(42, new ItemStack(Material.valueOf("DIAMOND_" + selected), 1));
			}
		}

		for(int i = 0; i < inventory.getSize(); i++) {
			if(inventory.getItem(i) == null)
				inventory.setItem(i, filler);
		}
		inventory.setItem(12, null);

		player.openInventory(inventory);
	}

	@EventHandler
	public void onGUIClick(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		if(inventory != null  && inventory.equals(event.getView().getTopInventory()))
			if(inventory.getName().contains(" - Equip")) {
				int slot = event.getSlot();
				ItemStack item = event.getCurrentItem();
				if(slot == 11) {
					selected = "MAIN_HAND";
					subSelected = null;
				} else if(slot == 13) {
					selected = "HELMET";
				} else if(slot == 14) {
					selected = "CHESTPLATE";
				} else if(slot == 15) {
					selected = "LEGGINGS";
				} else if(slot == 16) {
					selected = "BOOTS";
				} else if(slot == 29 && selected.equals("MAIN_HAND")) {
					subSelected = "SWORD";
				} else if(slot == 30 && selected.equals("MAIN_HAND")) {
					subSelected = "PICKAXE";
				} else if(slot == 31 && selected.equals("MAIN_HAND")) {
					subSelected = "AXE";
				} else if(slot == 32 && selected.equals("MAIN_HAND")) {
					subSelected = "SPADE";
				} else if(slot == 33 && selected.equals("MAIN_HAND")) {
					subSelected = "HOE";
				} else if(slot >= 38 && slot <=42  && item.getType() != Material.STAINED_GLASS_PANE) {
					npc.getEquipment().set(FakeEquipment.EquipmentSlot.valueOf(selected), item);
				} else if(slot == 23 && item.getType() != Material.STAINED_GLASS_PANE) {
					npc.getEquipment().set(FakeEquipment.EquipmentSlot.valueOf(selected), null);
				}
				event.setCancelled(true);
				this.open((Player) event.getWhoClicked());
			}
	}
}
