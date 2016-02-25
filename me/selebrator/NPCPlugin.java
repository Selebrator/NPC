package me.selebrator;

import java.util.HashMap;
import java.util.Map;

import me.selebrator.fetcher.GameProfileFetcher;
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
import org.bukkit.plugin.java.JavaPlugin;

public class NPCPlugin extends JavaPlugin implements Listener, CommandExecutor {
	
	private Map<Integer, FakePlayer> fakePlayers = new HashMap<Integer, FakePlayer>();
	private FakePlayer npc;
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("npc").setExecutor(this);
		
		npc = new FakePlayer(new GameProfileFetcher("Selebrator").build(), this);
		fakePlayers.put(1, npc);
		
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
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(args.length != 0)
		switch (args[0]) {
		case "select":
			npc = fakePlayers.get(Integer.parseInt(args[1]));
			return true;
		case "list":
			for(int i = 1; i <= fakePlayers.size(); i++) {
				player.sendMessage(i + ": " + fakePlayers.get(i).getName());
			}
			
			return true;
		case "create":
			int id = fakePlayers.size() + 1;
			String name = args[1];
			fakePlayers.put(id, new FakePlayer(new GameProfileFetcher(name).build(), this));
			npc = fakePlayers.get(id);
			player.sendMessage("Created NPC #" + id + ": " + npc.getName());
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
				npc.updateGameProfile(new GameProfileFetcher(args[1]).build());
			if(args.length == 3)
				npc.updateGameProfile(new GameProfileFetcher(args[1], args[2]).build());
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
