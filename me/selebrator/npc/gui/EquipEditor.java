package me.selebrator.npc.gui;

import java.util.HashMap;

import me.selebrator.fetcher.ItemBuilder;
import me.selebrator.npc.EquipmentSlot;
import me.selebrator.npc.FakePlayer;

import org.bukkit.Bukkit;
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

	@SuppressWarnings("unused")
	private Plugin plugin;
	private FakePlayer npc;
	private Inventory inventory;
	private String selected;
	private String subSelected;
	
	private HashMap<EquipmentSlot, ItemStack> equipment = new HashMap<>();

	public EquipEditor(FakePlayer npc, Plugin plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		
		this.npc = npc;
		
	}
	
	public void open(Player player) {
		

		ItemStack filler = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 8, " ").build();
		ItemStack selector = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5, " ").build();
		
		
		
		inventory = Bukkit.getServer().createInventory(null, 6 * 9, npc.getName() + " - Equip");
		
		ItemStack tool = new ItemBuilder(Material.IRON_SWORD, 1, (short) 0, "Tool").build();
		ItemStack bow = new ItemBuilder(Material.BOW, 1, (short) 0, "Bow").build();
		
		ItemStack helmet = new ItemBuilder(Material.IRON_HELMET, 1, (short) 0, "Helmet").build();
		ItemStack chestplate = new ItemBuilder(Material.IRON_CHESTPLATE, 1, (short) 0, "Chestplate").build();
		ItemStack leggings = new ItemBuilder(Material.IRON_LEGGINGS, 1, (short) 0, "Leggings").build();
		ItemStack boots = new ItemBuilder(Material.IRON_BOOTS, 1, (short) 0, "Boots").build();
		
		ItemStack enchant = new ItemStack(Material.ENCHANTED_BOOK, 1);
		
		inventory.setItem(10, tool);
		inventory.setItem(11, bow);
		inventory.setItem(13, helmet);
		inventory.setItem(14, chestplate);
		inventory.setItem(15, leggings);
		inventory.setItem(16, boots);
		inventory.setItem(22, enchant);
		
		
		if(selected == "Tools") {
			inventory.setItem(1, selector);
			
			ItemStack typeSword = new ItemBuilder(Material.IRON_SWORD, 1, (short) 0, "Sword").build();
			ItemStack typePickaxe = new ItemBuilder(Material.IRON_PICKAXE, 1, (short) 0, "Pickaxe").build();
			ItemStack typeAxe = new ItemBuilder(Material.IRON_AXE, 1, (short) 0, "Axe").build();
			ItemStack typeShovel = new ItemBuilder(Material.IRON_SPADE, 1, (short) 0, "Shovel").build();
			ItemStack typeHoe = new ItemBuilder(Material.IRON_HOE, 1, (short) 0, "Hoe").build();
			
			inventory.setItem(29, typeSword);
			inventory.setItem(30, typePickaxe);
			inventory.setItem(31, typeAxe);
			inventory.setItem(32, typeShovel);
			inventory.setItem(33, typeHoe);
			
			
			if(subSelected != null) {
				ItemStack specificToolWood = new ItemStack(Material.valueOf("WOOD_" + subSelected.toUpperCase()), 1);
				ItemStack specificToolGold = new ItemStack(Material.valueOf("GOLD_" + subSelected.toUpperCase()), 1);
				ItemStack specificToolStone = new ItemStack(Material.valueOf("STONE_" + subSelected.toUpperCase()), 1);
				ItemStack specificToolIron = new ItemStack(Material.valueOf("IRON_" + subSelected.toUpperCase()), 1);
				ItemStack specificToolDiamond = new ItemStack(Material.valueOf("DIAMOND_" + subSelected.toUpperCase()), 1);
				
				inventory.setItem(38, specificToolWood);
				inventory.setItem(39, specificToolGold);
				inventory.setItem(40, specificToolStone);
				inventory.setItem(41, specificToolIron);
				inventory.setItem(42, specificToolDiamond);
			}
		} else if(selected == "Bow") {
			inventory.setItem(2, selector);
			
			ItemStack specificBow = new ItemStack(Material.BOW, 1);
			
			inventory.setItem(40, specificBow);
		} else if(selected == "Helmet" || selected == "Chestplate" || selected == "Leggings" || selected == "Boots") {
			if(selected == "Helmet")
				inventory.setItem(4, selector);
			else if(selected == "Chestplate")
				inventory.setItem(5, selector);
			else if(selected == "Leggings")
				inventory.setItem(6, selector);
			else if(selected == "Boots")
				inventory.setItem(6, selector);
			
			ItemStack specificArmorLeather = new ItemStack(Material.valueOf("LEATHER_" + selected.toUpperCase()), 1);
			ItemStack specificArmorGold = new ItemStack(Material.valueOf("GOLD_" + selected.toUpperCase()), 1);
			ItemStack specificArmorChain = new ItemStack(Material.valueOf("CHAINMAIL_" + selected.toUpperCase()), 1);
			ItemStack specificArmorIron = new ItemStack(Material.valueOf("IRON_" + selected.toUpperCase()), 1);
			ItemStack specificArmorDiamond = new ItemStack(Material.valueOf("DIAMOND_" + selected.toUpperCase()), 1);
			
			inventory.setItem(38, specificArmorLeather);
			inventory.setItem(39, specificArmorGold);
			inventory.setItem(40, specificArmorChain);
			inventory.setItem(41, specificArmorIron);
			inventory.setItem(42, specificArmorDiamond);
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
				if(slot == 10) {
					selected = "Tools";
				} else if(slot == 11) {
					selected = "Bow";
				} else if(slot == 13) {
					selected = "Helmet";
				} else if(slot == 14) {
					selected = "Chestplate";
				} else if(slot == 15) {
					selected = "Leggings";
				} else if(slot == 16) {
					selected = "Boots";
				} else if(slot == 29 && selected == "Tools") {
					subSelected = "Sword";
				} else if(slot == 30 && selected == "Tools") {
					subSelected = "Pickaxe";
				} else if(slot == 31 && selected == "Tools") {
					subSelected = "Axe";
				} else if(slot == 32 && selected == "Tools") {
					subSelected = "Spade";
				} else if(slot == 33 && selected == "Tools") {
					subSelected = "Hoe";
				} else if(slot >= 38 && slot <=42  && item.getType() != Material.STAINED_GLASS_PANE) {
					if(selected == "Tools" || selected == "Bow") {
						equipment.put(EquipmentSlot.HAND, item);
					} else if(selected == "Helmet" || selected == "Chestplate" || selected == "Leggings" || selected == "Boots") {
						equipment.put(EquipmentSlot.valueOf(selected.toUpperCase()), item);
					}
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
				for(EquipmentSlot slot : equipment.keySet()) {
					npc.equip(slot, equipment.get(slot));
				}
			}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
