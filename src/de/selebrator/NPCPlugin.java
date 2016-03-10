package de.selebrator;

import de.selebrator.fetcher.GameProfileBuilder;
import de.selebrator.npc.EnumAnimation;
import de.selebrator.npc.EnumEquipmentSlot;
import de.selebrator.npc.FakePlayer;
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
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class NPCPlugin extends JavaPlugin implements Listener, CommandExecutor {
	
	private Map<Integer, FakePlayer> fakePlayers = new HashMap<>();
	private FakePlayer npc;
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("npc").setExecutor(this);
		
		npc = new FakePlayer(new GameProfileBuilder("Selebrator").build());
		fakePlayers.put(1, npc);

		try {
			task.runTaskTimer(this, 0, 1);
		} catch (Exception e) {
			System.out.println("Scheduler already running");
		}

		/*
		 * Optifine capes will override Mojangs.
		 */
	}

	@Override
	public void onDisable() {
		fakePlayers.forEach( (id, npc) -> npc.despawn());
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

				} else if(event.getAction() == Action.RIGHT_CLICK_AIR) {
					if(npc != null) {
						npc.spawn(event.getPlayer().getLocation());
						return;
					}
					event.getPlayer().sendMessage("§cSelect a NPC first");
				}
			}
			//despawn
			else if(event.getItem() != null && event.getItem().getType() == Material.BLAZE_ROD) {
				if(npc != null) {
					npc.despawn();
					return;
				}
				event.getPlayer().sendMessage("§cSelect a NPC first");
			}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;

		if(args.length >= 1) {

			switch (args[0]) {
				case "create":

					if (args.length == 2) {
						int id = 1;
						while (fakePlayers.containsKey(id)) {
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

				case "remove":

					if (args.length == 2) {
						int id = Integer.parseInt(args[1]);
						if (fakePlayers.containsKey(id)) {
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

				case "select":

					if (args.length == 1) {
						if (npc != null) {
							player.sendMessage("§eSelected §a" + npc.getName());
							return true;
						}
						player.sendMessage("§c/npc select <ID>");
						return true;
					} else if (args.length == 2) {
						int id = Integer.parseInt(args[1]);
						if (fakePlayers.containsKey(id)) {
							npc = fakePlayers.get(id);
							player.sendMessage("§eSelected §a" + npc.getName() + " §ewith ID: §a" + id);
							return true;
						}
						player.sendMessage("§cNPC #" + id + " does not exists  /npc list");
						return true;
					}
					player.sendMessage("§c/npc select [ID]");
					return true;

				case "list":

					if (args.length == 1) {
						if (!fakePlayers.isEmpty()) {
							fakePlayers.forEach((id, npc) -> player.sendMessage("§e" + id + ": " + npc.getName()));
							return true;
						}
						player.sendMessage("§cNo NPC have benn created yet.  /npc create <name>");
						return true;
					}
					player.sendMessage("§c/npc list");
					return true;

				case "spawn":

					if (npc != null) {
						if (args.length == 1) {
							npc.spawn(player.getLocation());
							return true;
						}
						player.sendMessage("§c/npc spawn");
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;

				case "despawn":

					if (npc != null) {
						if (args.length == 1) {
							npc.despawn();
							return true;
						}
						player.sendMessage("§c/npc spawn");
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;

				case "equip":

					if (npc != null) {
						if (args.length == 2) {
							npc.equip(EnumEquipmentSlot.valueOf(args[1].toUpperCase()), player.getInventory().getItemInMainHand());
							return true;
						}
						player.sendMessage("§c/npc equip <slot>");
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;

				case "animation":

					if (npc != null) {
						if (args.length == 2) {
							npc.playAnimation(EnumAnimation.valueOf(args[1].toUpperCase()));
							return true;
						}
						player.sendMessage("§c/npc animation <animation>");
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;

				case "health":

					if (npc != null) {
						if (args.length == 2) {
							npc.setHealth(Float.parseFloat(args[1]));
							return true;
						}
						player.sendMessage("§c/npc " + args[0] + " <0 - 20>");
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;

				case "target":

					if (npc != null) {
						if (args.length == 2) {
							npc.setTarget(Bukkit.getPlayer(args[1]));
							return true;
						}
						player.sendMessage("§c/npc " + args[0] + " <player>");
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;

				case "speed":

					if (npc != null) {
						if (args.length == 2) {
							npc.setMoveSpeed(Double.parseDouble(args[1]));
							return true;
						}
						player.sendMessage("§c/npc " + args[0]);
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;

				case "update":

					if (npc != null) {
						if (args.length == 2) {
							npc.updateGameProfile(new GameProfileBuilder(args[1]).build());
							return true;
						} else if (args.length == 3) {
							npc.updateGameProfile(new GameProfileBuilder(args[1], args[2]).build());
							return true;
						}
						player.sendMessage("§c/npc " + args[0] + " <name> [skinowner]");
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;

				case "test":

					if (npc != null) {
						if (args.length == 1) {

							return true;
						}
						player.sendMessage("§c/npc " + args[0]);
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;

				case "help":

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

					for (String line : commands)
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
				Vector vNPC = new Vector(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ());
				Vector vTarget = new Vector(npc.getTarget().getLocation().getX(), npc.getTarget().getLocation().getY(), npc.getTarget().getLocation().getZ());
				if(vNPC.distance(vTarget) > 3)
					npc.step(npc.getTarget().getLocation());
				npc.look(npc.getTarget().getEyeLocation());
			}
		}
	};
}