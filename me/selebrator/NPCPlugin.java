package me.selebrator;

import java.util.HashMap;
import java.util.Map;

import me.selebrator.fetcher.GameProfileFetcher;
import me.selebrator.npc.Animation;
import me.selebrator.npc.EquipmentSlot;
import me.selebrator.npc.FakePlayer;

import org.bukkit.Bukkit;
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
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		//spawn
		if(event.getItem() != null && event.getItem().getType() == Material.STICK) {
			if(event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				npc.spawn(event.getClickedBlock().getLocation().add(0.5, 1, 0.5));
			} else if(event.getAction() == Action.RIGHT_CLICK_AIR) {
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
		if(args.length != 0) {
			if(args[0] != null) {
				if(args[0].equals("create")) {

					int id = 1;
					while(fakePlayers.containsKey(id)) {
						id++;
					}
					String name = args[1];
					fakePlayers.put(id, new FakePlayer(new GameProfileFetcher(name).build(), this));
					npc = fakePlayers.get(id);
					player.sendMessage("§8[§a+§8] §eCreated NPC §a" + npc.getName() + " §ewith ID: §a" + id);
					return true;
					
				} else if(args[0].equals("remove")) {
					
					int id = Integer.parseInt(args[1]);
					if(fakePlayers.containsKey(id)) {
						FakePlayer npc = fakePlayers.get(id);
						String name = fakePlayers.get(id).getName();
						fakePlayers.remove(id);
						npc.despawn();
						this.npc = null;
						player.sendMessage("§8[§c-§8] §eRemoved NPC §c" + name + " §ewith ID: §c" + id);
						return true;
					}
					player.sendMessage("§cNPC #" + id + " does not exists   /npc list");
					return true;
					
				} else if(args[0].equals("select")) {
					
					if(args.length == 1) {
						player.sendMessage("§eSelected §a" + npc.getName());
						return true;
					} else if(args.length == 2) {
						int id = Integer.parseInt(args[1]);
						npc = fakePlayers.get(id);
						player.sendMessage("§eSelected §a" + npc.getName()  + " §ewith ID: §a" + id);
						return true;
					}
					
				} else if(args[0].equals("list")) {
					
					fakePlayers.forEach( (id, npc) -> {player.sendMessage("§e" + id + ": " + npc.getName());});
					return true;
					
				} else if(args[0].equals("spawn")) {
					
					if(npc != null) {
						npc.spawn(player.getLocation());
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;
					
				} else if(args[0].equals("despawn")) {
					
					if(npc != null) {
						npc.despawn();
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;
					
				} else if(args[0].equals("equip")) {
					
					if(npc != null) {
						npc.equip(EquipmentSlot.valueOf(args[1].toUpperCase()), player.getItemInHand());
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;
					
				} else if(args[0].equals("animation")) {
					
					if(npc != null) {
						npc.playAnimation(Animation.valueOf(args[1].toUpperCase()));
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;
					
				} else if(args[0].equals("health")) {

					if(npc != null) {
						if(args.length == 2) {
							npc.setHealth(Float.parseFloat(args[1]));
							return true;
						}
						player.sendMessage("§c/npc " +  args[0] + " <health>");
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;
					
				} else if(args[0].equals("target")) {

					if(npc != null) {
						if(args.length == 2) {
							npc.setTarget(player);
							return true;
						}
						player.sendMessage("§c/npc " +  args[0] + " <targetEntity>");
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;
					
				} else if(args[0].equals("update")) {

					if(npc != null) {
						if(args.length == 2) {
							npc.updateGameProfile(new GameProfileFetcher(args[1]).build());
							return true;
						} else if(args.length == 3) {
							npc.updateGameProfile(new GameProfileFetcher(args[1], args[2]).build());
							return true;
						}
						player.sendMessage("§c/npc " + args[0] + " <name> [skinowner]");
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;
					
				}
			}
			player.sendMessage("§cInvalid argument");
			return true;
		}
		player.sendMessage("§cNeed at least 1 argument");
		return false;
	}
}
