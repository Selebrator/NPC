package me.selebrator;

import java.util.HashMap;
import java.util.Map;

import me.selebrator.npc.Animation;
import me.selebrator.npc.EquipmentSlot;
import me.selebrator.npc.FakePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, CommandExecutor {
	private Map<Integer, FakePlayer> fakePlayers = new HashMap<Integer, FakePlayer>();
	private FakePlayer npc;
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("npc").setExecutor(this);
		
		npc = new FakePlayer(new GameProfileBuilder("Selebrator").build(), this);
		fakePlayers.put(1, npc);
		
		npc = new FakePlayer(new GameProfileBuilder("sukram706").build(), this);
		fakePlayers.put(2, npc);
		
		npc = new FakePlayer(new GameProfileBuilder("Cooooks").build(), this);
		fakePlayers.put(3, npc);
		
		/*
		 * Optifine capes will override Mojangs.
		 * 
		 * 
		 */
		
	}
	
	@Override
	public void onDisable() {
		for(FakePlayer npc : fakePlayers.values()) {
			npc.despawn();
			npc.removeFromTabList();
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		//spawn
		if(event.getItem() != null && event.getItem().getType() == Material.STICK) {
			if(event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				npc.addToTabList();
				npc.spawn(event.getClickedBlock().getLocation().add(0.5, 1, 0.5));
			} else if(event.getAction() == Action.RIGHT_CLICK_AIR) {
				npc.addToTabList();
				npc.spawn(event.getPlayer().getLocation());
			}
		} 
		//despawn
		else if(event.getItem() != null && event.getItem().getType() == Material.BLAZE_ROD) {
			npc.despawn();
		}  
		//setTarget
		else if(event.getItem() != null && event.getItem().getType() == Material.EMERALD) {
			npc.setTarget(event.getPlayer());
		}
		//playAnimation
		else if(event.getItem() != null && event.getItem().getType() == Material.MAGMA_CREAM) {
			npc.playAnimation(Animation.SWING_ARM);
		} 
		//equip
		else if(event.getItem() != null && event.getItem().getType() == Material.GOLD_HELMET) {
			npc.equip(EquipmentSlot.HELMET, new ItemStack(Material.GOLD_HELMET, 1));
			npc.equip(EquipmentSlot.CHESTPLATE, new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
			npc.equip(EquipmentSlot.LEGGINGS, new ItemStack(Material.DIAMOND_LEGGINGS, 1));
			npc.equip(EquipmentSlot.BOOTS, new ItemStack(Material.DIAMOND_BOOTS, 1));
			npc.equip(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD, 1));
			event.setCancelled(true);
		} 
		//kill
		else if(event.getItem() != null && event.getItem().getType() == Material.LEVER) {
				npc.setHealth(0);
				event.setCancelled(true);
		} 
		//respawn
		else if(event.getItem() != null && event.getItem().getType() == Material.REDSTONE_TORCH_ON) {
			npc.setHealth(20);
			event.setCancelled(true);
		} 
		//update
		else if(event.getItem() != null && event.getItem().getType() == Material.SIGN) {
			npc.updateGameProfile(new GameProfileBuilder("Selebrator", "Selebrator").build());
			event.setCancelled(true);
			
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(args.length != 0)
		switch (args[0]) {
		case "select":
			npc = fakePlayers.get(Integer.parseInt(args[1]));
			return true;
		case "spawn":
			npc.addToTabList();
			npc.spawn(player.getLocation());
			return true;
		case "despawn":
			npc.despawn();
			npc.removeFromTabList();
			return true;
		case "target":
			npc.setTarget(player);
			return true;
		case "animation":
			npc.playAnimation(Animation.valueOf(args[1].toUpperCase()));
			return true;
		case "equip":
			npc.equip(EquipmentSlot.valueOf(args[1]), player.getItemInHand());
			return true;
		case "health":
			npc.setHealth(Float.parseFloat(args[1]));
			return true;
		case "update":
			if(args.length == 2)
				npc.updateGameProfile(new GameProfileBuilder(args[1]).build());
			if(args.length == 3)
				npc.updateGameProfile(new GameProfileBuilder(args[1], args[2]).build());
			if(args.length > 3)
				player.sendMessage("§cTo many arguments");
			return true;
		case "strip":
			npc.skinFlags(true, false, false, false, false, false, false);
			npc.updateMetadata();
			return true;
		case "tp":
			npc.teleport(new Location(Bukkit.getWorld("world"), -63.5, 65, 262.5, 90, 33));
			return true;
		case "test":
			return true;
		default:
			player.sendMessage("§cNo valit argument");
			return true;
		}
		player.sendMessage("§cNeed at least 1 argument");
		return false;
	}
}
