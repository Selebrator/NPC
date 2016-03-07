package de.selebrator.npc.gui;

import java.util.HashMap;

import de.selebrator.fetcher.ItemBuilder;
import de.selebrator.fetcher.LeatherArmorBuilder;
import de.selebrator.npc.EnumEquipmentSlot;
import de.selebrator.npc.FakePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EquipEditor implements Listener {

	private FakePlayer npc;
	private Inventory inventory;
	private String selected;
	private String subSelected;
	
	private HashMap<EnumEquipmentSlot, ItemStack> equipment = new HashMap<>();

	public EquipEditor(FakePlayer npc, Plugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		
		this.npc = npc;
		
	}
	
	public void open(Player player) {

		ItemStack filler = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 8, " ").build();
		ItemStack marker = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5, " ").build();
		
		
		
		inventory = Bukkit.getServer().createInventory(null, 6 * 9, npc.getName() + " - Equip");
		
		ItemStack tool = new ItemBuilder(Material.STICK, 1, (short) 0, "�8Empty MainHand").build();
		
		ItemStack helmet = new LeatherArmorBuilder(Material.LEATHER_HELMET, 1, (short) 0, "�8No Helmet", Color.fromBGR(76, 76, 76)).build();
		ItemStack chestplate =new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE, 1, (short) 0, "�8No Chestplate", Color.fromBGR(76, 76, 76)).build();
		ItemStack leggings = new LeatherArmorBuilder(Material.LEATHER_LEGGINGS, 1, (short) 0, "�8No Leggings", Color.fromBGR(76, 76, 76)).build();
		ItemStack boots = new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1, (short) 0, "�8No Boots", Color.fromBGR(76, 76, 76)).build();

		
		if(npc.hasEquipment(EnumEquipmentSlot.MAIN_HAND))
			tool = npc.getEquipment(EnumEquipmentSlot.MAIN_HAND);
		if(npc.hasEquipment(EnumEquipmentSlot.HELMET))
			helmet = npc.getEquipment(EnumEquipmentSlot.HELMET);
		if(npc.hasEquipment(EnumEquipmentSlot.CHESTPLATE))
			chestplate = npc.getEquipment(EnumEquipmentSlot.CHESTPLATE);
		if(npc.hasEquipment(EnumEquipmentSlot.LEGGINGS))
			leggings = npc.getEquipment(EnumEquipmentSlot.LEGGINGS);
		if(npc.hasEquipment(EnumEquipmentSlot.BOOTS))
			boots = npc.getEquipment(EnumEquipmentSlot.BOOTS);
		
		
		inventory.setItem(11, tool);
		inventory.setItem(13, helmet);
		inventory.setItem(14, chestplate);
		inventory.setItem(15, leggings);
		inventory.setItem(16, boots);
		
		if(selected != null) {
			ItemStack enchant = new ItemStack(Material.ENCHANTED_BOOK, 1);
			ItemStack clear = new ItemBuilder(Material.BARRIER, 1, (short) 0, "�cClear").build();

			inventory.setItem(21, enchant);
			inventory.setItem(23, clear);
			
			if(selected.equals("MAIN_HAND")) {
				inventory.setItem(2, marker);
				
				ItemStack typeSword = new ItemBuilder(Material.IRON_SWORD, 1, (short) 0, "Sword").build();
				ItemStack typePickaxe = new ItemBuilder(Material.IRON_PICKAXE, 1, (short) 0, "Pickaxe").build();
				ItemStack typeAxe = new ItemBuilder(Material.IRON_AXE, 1, (short) 0, "Axe").build();
				ItemStack typeShovel = new ItemBuilder(Material.IRON_SPADE, 1, (short) 0, "Shovel").build();
				ItemStack typeHoe = new ItemBuilder(Material.IRON_HOE, 1, (short) 0, "Hoe").build();
				ItemStack specificBow = new ItemStack(Material.BOW, 1);
				
				inventory.setItem(29, typeSword);
				inventory.setItem(30, typePickaxe);
				inventory.setItem(31, typeAxe);
				inventory.setItem(32, typeShovel);
				inventory.setItem(33, typeHoe);
				inventory.setItem(40, specificBow);
				
				
				if(subSelected != null) {
					ItemStack specificToolWood = new ItemStack(Material.valueOf("WOOD_" + subSelected), 1);
					ItemStack specificToolGold = new ItemStack(Material.valueOf("GOLD_" + subSelected), 1);
					ItemStack specificToolStone = new ItemStack(Material.valueOf("STONE_" + subSelected), 1);
					ItemStack specificToolIron = new ItemStack(Material.valueOf("IRON_" + subSelected), 1);
					ItemStack specificToolDiamond = new ItemStack(Material.valueOf("DIAMOND_" + subSelected), 1);
					
					inventory.setItem(38, specificToolWood);
					inventory.setItem(39, specificToolGold);
					inventory.setItem(40, specificToolStone);
					inventory.setItem(41, specificToolIron);
					inventory.setItem(42, specificToolDiamond);
				}
				
			} else if(selected.equals("HELMET") || selected.equals("CHESTPLATE") || selected.equals("LEGGINGS") || selected.equals("BOOTS")) {
				
				switch (selected) {
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
				
				ItemStack specificArmorLeather = new ItemStack(Material.valueOf("LEATHER_" + selected), 1);
				ItemStack specificArmorGold = new ItemStack(Material.valueOf("GOLD_" + selected), 1);
				ItemStack specificArmorChain = new ItemStack(Material.valueOf("CHAINMAIL_" + selected), 1);
				ItemStack specificArmorIron = new ItemStack(Material.valueOf("IRON_" + selected), 1);
				ItemStack specificArmorDiamond = new ItemStack(Material.valueOf("DIAMOND_" + selected), 1);
				
				inventory.setItem(38, specificArmorLeather);
				inventory.setItem(39, specificArmorGold);
				inventory.setItem(40, specificArmorChain);
				inventory.setItem(41, specificArmorIron);
				inventory.setItem(42, specificArmorDiamond);
			}
		}
		
		for(int i = 0; i < inventory.getSize(); i++) {
			if(inventory.getItem(i) == null)
				inventory.setItem(i, filler);
		}
		inventory.setItem(12, null);
		
		
		player.openInventory(this.inventory);
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
					equipment.put(EnumEquipmentSlot.valueOf(selected), item);
				} else if(slot == 23 && item.getType() != Material.STAINED_GLASS_PANE) {
					equipment.put(EnumEquipmentSlot.valueOf(selected), null);
				}
				event.setCancelled(true);
				this.open((Player) event.getWhoClicked());
			}
	}
	
	
	@EventHandler
	public void onGUIClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		if(inventory != null)
			if(inventory.getName().contains(" - Equip")) {
				equipment.forEach( (slot, item) -> npc.equip(slot, item));
			}
	}
}
