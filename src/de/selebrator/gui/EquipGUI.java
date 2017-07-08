package de.selebrator.gui;

import de.selebrator.fetcher.ItemBuilder;
import de.selebrator.fetcher.LeatherArmorBuilder;
import de.selebrator.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EquipGUI extends GUI implements Listener {

	private EquipmentSlot selected;
	private String subSelected;

	public EquipGUI(NPC npc, Plugin plugin) {
		super(npc, plugin);
	}

	public void open(Player player) {
		ItemStack marker = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5, " ").build();

		Inventory inventory = Bukkit.getServer().createInventory(null, 6 * 9, npc.getName() + " - Equip");

		inventory.setItem(10, npc.getEquipment().getMainHand() == null ? new ItemBuilder(Material.STICK, "§8Empty MainHand").build() : npc.getEquipment().getMainHand());
		inventory.setItem(11, npc.getEquipment().getOffHand() == null ? new ItemBuilder(Material.PAPER, "§8Empty OffHand").build() : npc.getEquipment().getOffHand());
		inventory.setItem(13, npc.getEquipment().getHelmet() == null ? new LeatherArmorBuilder(EquipmentSlot.HEAD, "§8No Helmet", Color.fromBGR(76, 76, 76)).build() : npc.getEquipment().getHelmet());
		inventory.setItem(14, npc.getEquipment().getChestplate() == null ? new LeatherArmorBuilder(EquipmentSlot.CHEST, "§8No Chestplate", Color.fromBGR(76, 76, 76)).build() : npc.getEquipment().getChestplate());
		inventory.setItem(15, npc.getEquipment().getLeggings() == null ? new LeatherArmorBuilder(EquipmentSlot.LEGS, "§8No Leggings", Color.fromBGR(76, 76, 76)).build() : npc.getEquipment().getLeggings());
		inventory.setItem(16, npc.getEquipment().getBoots() == null ? new LeatherArmorBuilder(EquipmentSlot.FEET, "§8No Boots", Color.fromBGR(76, 76, 76)).build() : npc.getEquipment().getBoots());

		if(selected != null) {
			ItemStack enchant = new ItemStack(Material.ENCHANTED_BOOK, 1);
			ItemStack clear = new ItemBuilder(Material.BARRIER, "§cClear").build();

			inventory.setItem(21, enchant);
			inventory.setItem(23, clear);

			switch(selected) {
				case HAND:
					inventory.setItem(1, marker);
					break;
				case OFF_HAND:
					inventory.setItem(2, marker);
					break;
				case HEAD:
					inventory.setItem(4, marker);
					break;
				case CHEST:
					inventory.setItem(5, marker);
					break;
				case LEGS:
					inventory.setItem(6, marker);
					break;
				case FEET:
					inventory.setItem(7, marker);
					break;
				default:
					break;

			}

			if(selected == EquipmentSlot.HAND || selected == EquipmentSlot.OFF_HAND) {
				inventory.setItem(29, new ItemBuilder(Material.IRON_SWORD, "Sword").build());
				inventory.setItem(30, new ItemBuilder(Material.IRON_PICKAXE, "Pickaxe").build());
				inventory.setItem(31, new ItemBuilder(Material.IRON_AXE, "Axe").build());
				inventory.setItem(32, new ItemBuilder(Material.IRON_SPADE, "Shovel").build());
				inventory.setItem(33, new ItemBuilder(Material.IRON_HOE, "Hoe").build());
				inventory.setItem(39, new ItemStack(Material.BOW));
				inventory.setItem(41, new ItemStack(Material.SHIELD));

				if(subSelected != null) {
					inventory.setItem(38, new ItemStack(Material.valueOf("WOOD_" + subSelected)));
					inventory.setItem(39, new ItemStack(Material.valueOf("GOLD_" + subSelected)));
					inventory.setItem(40, new ItemStack(Material.valueOf("STONE_" + subSelected)));
					inventory.setItem(41, new ItemStack(Material.valueOf("IRON_" + subSelected)));
					inventory.setItem(42, new ItemStack(Material.valueOf("DIAMOND_" + subSelected)));
				}
			} else if(selected == EquipmentSlot.HEAD || selected == EquipmentSlot.CHEST || selected == EquipmentSlot.LEGS || selected == EquipmentSlot.FEET) {
				inventory.setItem(38, new de.selebrator.fetcher.ArmorBuilder(selected, Material.LEATHER).build());
				inventory.setItem(39, new de.selebrator.fetcher.ArmorBuilder(selected, Material.GOLD_INGOT).build());
				inventory.setItem(40, new de.selebrator.fetcher.ArmorBuilder(selected, Material.FIRE).build());
				inventory.setItem(41, new de.selebrator.fetcher.ArmorBuilder(selected, Material.IRON_INGOT).build());
				inventory.setItem(42, new de.selebrator.fetcher.ArmorBuilder(selected, Material.DIAMOND).build());
			}
		}

		player.openInventory(inventory);
	}

	@EventHandler
	public void onGUIClick(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		if(inventory != null && inventory.equals(event.getView().getTopInventory()))
			if(inventory.getName().contains(" - Equip")) {
				int slot = event.getSlot();
				ItemStack item = event.getCurrentItem();
				if(slot == 10) {
					selected = EquipmentSlot.HAND;
					subSelected = null;
				} else if(slot == 11) {
					selected = EquipmentSlot.OFF_HAND;
					subSelected = null;
				} else if(slot == 13) {
					selected = EquipmentSlot.HEAD;
				} else if(slot == 14) {
					selected = EquipmentSlot.CHEST;
				} else if(slot == 15) {
					selected = EquipmentSlot.LEGS;
				} else if(slot == 16) {
					selected = EquipmentSlot.FEET;
				} else if(slot == 29 && (selected == EquipmentSlot.HAND || selected == EquipmentSlot.OFF_HAND)) {
					subSelected = "SWORD";
				} else if(slot == 30 && (selected == EquipmentSlot.HAND || selected == EquipmentSlot.OFF_HAND)) {
					subSelected = "PICKAXE";
				} else if(slot == 31 && (selected == EquipmentSlot.HAND || selected == EquipmentSlot.OFF_HAND)) {
					subSelected = "AXE";
				} else if(slot == 32 && (selected == EquipmentSlot.HAND || selected == EquipmentSlot.OFF_HAND)) {
					subSelected = "SPADE";
				} else if(slot == 33 && (selected == EquipmentSlot.HAND || selected == EquipmentSlot.OFF_HAND)) {
					subSelected = "HOE";
				} else if(slot >= 38 && slot <= 42 && item != null) {
					npc.getEquipment().set(selected, item);
				} else if(slot == 23 && item != null) {
					npc.getEquipment().set(selected, null);
				}
				event.setCancelled(true);
				this.open((Player) event.getWhoClicked());
			}
	}
}
