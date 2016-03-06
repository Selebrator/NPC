package me.selebrator;

import java.util.HashMap;
import java.util.Map;

import me.selebrator.fetcher.GameProfileBuilder;
import me.selebrator.npc.EnumAnimation;
import me.selebrator.npc.EnumEquipmentSlot;
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
import org.bukkit.scheduler.BukkitRunnable;

public class NPCPlugin extends JavaPlugin implements Listener, CommandExecutor {
	
	private Map<Integer, FakePlayer> fakePlayers = new HashMap<Integer, FakePlayer>();
	private FakePlayer npc;
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("npc").setExecutor(this);
		
		npc = new FakePlayer(new GameProfileBuilder("Selebrator").build());
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
					if(npc != null) {
						npc.spawn(event.getClickedBlock().getLocation().add(0.5, 1, 0.5));
						return;
					}
					event.getPlayer().sendMessage("§cSelect a NPC first");
					return;
					
				} else if(event.getAction() == Action.RIGHT_CLICK_AIR) {
					if(npc != null) {
						npc.spawn(event.getPlayer().getLocation());
						return;
					}
					event.getPlayer().sendMessage("§cSelect a NPC first");
					return;
				}
			} 
			//despawn
			else if(event.getItem() != null && event.getItem().getType() == Material.BLAZE_ROD) {
				if(npc != null) {
					npc.despawn();
					return;
				}
				event.getPlayer().sendMessage("§cSelect a NPC first");
				return;
			}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;

		if(args.length >=1) {
			if(args[0].equals("create")) {

				if(args.length == 2) {
					int id = 1;
					while(fakePlayers.containsKey(id)) {
						id++;
					}
					String name = args[1];
					fakePlayers.put(id, new FakePlayer(new GameProfileBuilder(name).build()));
					npc = fakePlayers.get(id);
					player.sendMessage("§8[§a+§8] §eCreated NPC §a" + npc.getName() + " §ewith ID: §a" + id);
					return true;
				}
				player.sendMessage("§c/npc create <name>");
				return true;
				
			} else if(args[0].equals("remove")) {
				
				if(args.length == 2) {
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
					player.sendMessage("§cNPC #" + id + " does not exists  /npc list");
					return true;
				}
				player.sendMessage("§c/npc remove <ID>");
				return true;
				
			} else if(args[0].equals("select")) {
				
				if(args.length == 1) {
					if(npc != null) {
						player.sendMessage("§eSelected §a" + npc.getName());
						return true;
					}
					player.sendMessage("§c/npc select <ID>");
					return true;
				} else if(args.length == 2) {
					int id = Integer.parseInt(args[1]);
					if(fakePlayers.containsKey(id)) {
						npc = fakePlayers.get(id);
						player.sendMessage("§eSelected §a" + npc.getName()  + " §ewith ID: §a" + id);
						return true;
					}
					player.sendMessage("§cNPC #" + id + " does not exists  /npc list");
					return true;
				}
				player.sendMessage("§c/npc select [ID]");
				return true;
				
			} else if(args[0].equals("list")) {
				
				if(args.length == 1) {
					if(!fakePlayers.isEmpty()) {
						fakePlayers.forEach( (id, npc) -> {player.sendMessage("§e" + id + ": " + npc.getName());});
						return true;
					}
					player.sendMessage("§cNo NPC have benn created yet.  /npc create <name>");
					return true;
				}
				player.sendMessage("§c/npc list");
				return true;
				
			} else if(args[0].equals("spawn")) {

				if(npc != null) {
					if(args.length == 1) {
						npc.spawn(player.getLocation());
						return true;
					}
					player.sendMessage("§c/npc spawn");
					return true;
				}
				player.sendMessage("§cSelect a NPC first");
				return true;
				
			} else if(args[0].equals("despawn")) {

				if(npc != null) {
					if(args.length == 1) {
						npc.despawn();
						return true;
					}
					player.sendMessage("§c/npc spawn");
					return true;
				}
				player.sendMessage("§cSelect a NPC first");
				return true;
				
			} else if(args[0].equals("equip")) {

				if(npc != null) {
					if(args.length == 2) {
						npc.equip(EnumEquipmentSlot.valueOf(args[1].toUpperCase()), player.getInventory().getItemInMainHand());
						return true;
					}
					player.sendMessage("§c/npc equip <slot>");
					return true;
				}
				player.sendMessage("§cSelect a NPC first");
				return true;
				
			} else if(args[0].equals("animation")) {

				if(npc != null) {
					if(args.length == 2) {
						npc.playAnimation(EnumAnimation.valueOf(args[1].toUpperCase()));
						return true;
					}
					player.sendMessage("§c/npc animation <animation>");
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
					player.sendMessage("§c/npc " +  args[0] + " <0 - 20>");
					return true;
				}
				player.sendMessage("§cSelect a NPC first");
				return true;
				
			} else if(args[0].equals("target")) {

				if(npc != null) {
					if(args.length == 2) {
						npc.setTarget(Bukkit.getPlayer(args[1]));
						return true;
					}
					player.sendMessage("§c/npc " +  args[0] + " <player>");
					return true;
				}
				player.sendMessage("§cSelect a NPC first");
				return true;
				
			} else if(args[0].equals("update")) {

				if(npc != null) {
					if(args.length == 2) {
						npc.updateGameProfile(new GameProfileBuilder(args[1]).build());
						return true;
					} else if(args.length == 3) {
						npc.updateGameProfile(new GameProfileBuilder(args[1], args[2]).build());
						return true;
					}
					player.sendMessage("§c/npc " + args[0] + " <name> [skinowner]");
					return true;
				}
				player.sendMessage("§cSelect a NPC first");
				return true;
				
			} else if(args[0].equals("test")) {

				if(npc != null) {
					if(args.length == 1) {
						task.runTaskTimer(this, 0, 1);
						return true;
					}
					player.sendMessage("§c/npc " + args[0]);
					return true;
				}
				player.sendMessage("§cSelect a NPC first");
				return true;
				
			} else if(args[0].equals("help")) {
				String[] commands = {"§7----------------§8[§3NPC§8]§7----------------",
						 "§a/npc create <name> §7- create a new NPC",
						 "§a/npc remove <ID> §7- remove a existing NPC",
						 "§a/npc select [ID] §7- select a NPC for editing",
						 "§a/npc list §7- list all NPCs with their ID",
						 "§a/npc spawn §7- spawn the NPC",
						 "§a/npc despawn §7- despawn the NPC",
						 "§a/npc equip <slot> §7- equip the NPC with your held item",
						 "§a/npc animation <animation> §7- make the NPC move",
						 "§a/npc health <0 - 20> §7- set the NPCs health",
						 "§a/npc target <player> §7- make the NPC constantly look at his target",
						 "§a/npc update <name> [skinowner] §7- change the NPCs appearance"};

				for(String line : commands)
					player.sendMessage(line);
				return true;
			}
		}
		player.sendMessage("§a/npc help");
		return true;
	}
	
	private BukkitRunnable task = new BukkitRunnable() {

		@Override
		public void run() {
			if(npc != null && npc.hasTarget()) {
				double differenceX = npc.getTarget().getLocation().getX() - npc.getLocation().getX();
				double differenceY = npc.getTarget().getLocation().getY() - (npc.getLocation().getY());
				double differenceZ = npc.getTarget().getLocation().getZ() - npc.getLocation().getZ();
				
				if(Math.abs(differenceX) > 3 || Math.abs(differenceY) > 3 || Math.abs(differenceZ) > 3) {
					double hypotenuseXZ = Math.sqrt(differenceX * differenceX + differenceZ * differenceZ);
					float yaw = (float) Math.toRadians((Math.atan2(differenceZ, differenceX) * 180D / Math.PI) - 90F);
					float pitch = (float) Math.toRadians(-(Math.atan2(differenceY, hypotenuseXZ) * 180D / Math.PI));
					double x = -Math.sin(yaw);
					double y = -Math.sin(pitch);
					double z = Math.cos(yaw);
					npc.walk(x * (4.3D / 20), y * (4.3D / 20) ,z * (4.3D / 20));
				}
				npc.look(npc.getTarget().getEyeLocation());
			}
		}
	};
}
