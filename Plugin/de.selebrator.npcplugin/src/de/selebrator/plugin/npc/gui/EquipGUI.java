package de.selebrator.plugin.npc.gui;

import de.selebrator.npc.entity.PlayerNPC;
import de.selebrator.plugin.npc.fetcher.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class EquipGUI extends GUI implements Listener {

	public static final Color DARK_GRAY = Color.fromBGR(76, 76, 76);

	public static final ItemStack MARKER = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5, " ").build();
	public static final ItemStack DEFAULT_MAIN_HAND = new ItemBuilder(Material.STICK, "§8Empty MainHand").build();
	public static final ItemStack DEFAULT_OFF_HAND = new ItemBuilder(Material.PAPER, "§8Empty OffHand").build();
	public static final ItemStack DEFAULT_HELMET = new LeatherArmorBuilder(EquipmentSlot.HEAD, "§8No Helmet", DARK_GRAY).build();
	public static final ItemStack DEFAULT_CHESTPLATE = new LeatherArmorBuilder(EquipmentSlot.CHEST, "§8No Chestplate", DARK_GRAY).build();
	public static final ItemStack DEFAULT_LEGGINGS = new LeatherArmorBuilder(EquipmentSlot.LEGS, "§8No Leggings", DARK_GRAY).build();
	public static final ItemStack DEFAULT_FEET = new LeatherArmorBuilder(EquipmentSlot.FEET, "§8No Boots", DARK_GRAY).build();

	public static final ItemStack SWORD = new ItemBuilder(Material.IRON_SWORD, "Sword").build();
	public static final ItemStack PICKAXE = new ItemBuilder(Material.IRON_PICKAXE, "Pickaxe").build();
	public static final ItemStack AXE = new ItemBuilder(Material.IRON_AXE, "Axe").build();
	public static final ItemStack SPADE = new ItemBuilder(Material.IRON_SPADE, "Shovel").build();
	public static final ItemStack HOE = new ItemBuilder(Material.IRON_HOE, "Hoe").build();
	public static final ItemStack BOW = new ItemStack(Material.BOW);
	public static final ItemStack SHIELD = new ItemStack(Material.SHIELD);

	public PlayerNPC npc;
	private EquipmentSlot selected;
	private String subSelected;

	public EquipGUI(PlayerNPC npc, Plugin plugin) {
		super(npc, plugin);
	}

	public void open(Player player) {
		Inventory inventory = Bukkit.getServer().createInventory(null, 6 * 9, this.npc.getCustomName() + " - Equip");

		inventory.setItem(10, Optional.ofNullable(this.npc.getEquipment().getMainHand()).orElse(DEFAULT_MAIN_HAND));
		inventory.setItem(11, Optional.ofNullable(this.npc.getEquipment().getOffHand()).orElse(DEFAULT_OFF_HAND));
		inventory.setItem(13, Optional.ofNullable(this.npc.getEquipment().getHelmet()).orElse(DEFAULT_HELMET));
		inventory.setItem(14, Optional.ofNullable(this.npc.getEquipment().getChestplate()).orElse(DEFAULT_CHESTPLATE));
		inventory.setItem(15, Optional.ofNullable(this.npc.getEquipment().getLeggings()).orElse(DEFAULT_LEGGINGS));
		inventory.setItem(16, Optional.ofNullable(this.npc.getEquipment().getBoots()).orElse(DEFAULT_FEET));

		if(selected != null) {
			ItemStack enchant = new ItemStack(Material.ENCHANTED_BOOK, 1);
			ItemStack clear = new ItemBuilder(Material.BARRIER, "§cClear").build();

			inventory.setItem(21, enchant);
			inventory.setItem(23, clear);

			switch(selected) {
				case HAND:
					inventory.setItem(1, MARKER);
					break;
				case OFF_HAND:
					inventory.setItem(2, MARKER);
					break;
				case HEAD:
					inventory.setItem(4, MARKER);
					break;
				case CHEST:
					inventory.setItem(5, MARKER);
					break;
				case LEGS:
					inventory.setItem(6, MARKER);
					break;
				case FEET:
					inventory.setItem(7, MARKER);
					break;
				default:
					break;

			}

			if(selected == EquipmentSlot.HAND || selected == EquipmentSlot.OFF_HAND) {
				inventory.setItem(29, SWORD);
				inventory.setItem(30, PICKAXE);
				inventory.setItem(31, AXE);
				inventory.setItem(32, SPADE);
				inventory.setItem(33, HOE);
				inventory.setItem(39, BOW);
				inventory.setItem(41, SHIELD);

				if(subSelected != null) {
					inventory.setItem(38, new ItemStack(Material.valueOf("WOOD_" + subSelected)));
					inventory.setItem(39, new ItemStack(Material.valueOf("GOLD_" + subSelected)));
					inventory.setItem(40, new ItemStack(Material.valueOf("STONE_" + subSelected)));
					inventory.setItem(41, new ItemStack(Material.valueOf("IRON_" + subSelected)));
					inventory.setItem(42, new ItemStack(Material.valueOf("DIAMOND_" + subSelected)));
				}
			} else if(selected == EquipmentSlot.HEAD || selected == EquipmentSlot.CHEST || selected == EquipmentSlot.LEGS || selected == EquipmentSlot.FEET) {
				inventory.setItem(38, new ArmorBuilder(selected, Material.LEATHER).build());
				inventory.setItem(39, new ArmorBuilder(selected, Material.GOLD_INGOT).build());
				inventory.setItem(40, new ArmorBuilder(selected, Material.FIRE).build());
				inventory.setItem(41, new ArmorBuilder(selected, Material.IRON_INGOT).build());
				inventory.setItem(42, new ArmorBuilder(selected, Material.DIAMOND).build());
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
