package de.selebrator;

import de.selebrator.event.npc.NPCDamageEvent;
import de.selebrator.event.npc.NPCSpawnEvent;
import de.selebrator.fetcher.GameProfileBuilder;
import de.selebrator.npc.EnumAnimation;
import de.selebrator.npc.EnumEquipmentSlot;
import de.selebrator.npc.EnumNature;
import de.selebrator.npc.FakePlayer;
import de.selebrator.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class NPCPlugin extends JavaPlugin implements Listener, CommandExecutor {

	private Map<Integer, NPC> fakePlayers = new HashMap<>();
	private NPC npc;

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("npc").setExecutor(this);

		npc = new FakePlayer(new GameProfileBuilder("Selebrator").build());
		fakePlayers.put(1, npc);

		task.runTaskTimer(this, 0, 1);
	}

	@Override
	public void onDisable() {
		fakePlayers.forEach( (id, npc) -> npc.despawn());
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		//spawn
		ItemStack item = event.getItem();
		if(item != null && item.getType() == Material.STICK) {
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
		if(item != null && item.getType() == Material.EMERALD) {
			if(event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(npc != null) {
					npc.respawn(event.getClickedBlock().getLocation().add(0.5, 1, 0.5));
					return;
				}
				event.getPlayer().sendMessage("§cSelect a NPC first");

			} else if(event.getAction() == Action.RIGHT_CLICK_AIR) {
				if(npc != null) {
					npc.respawn(null);
					return;
				}
				event.getPlayer().sendMessage("§cSelect a NPC first");
			}
		}
	}

	@EventHandler
	public void onEntityUse(PlayerInteractEntityEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if(item != null && item.getType() == Material.BLAZE_ROD) {
			npc.setTarget((LivingEntity) event.getRightClicked());
		}
	}

	@EventHandler
	public void onDamage(NPCDamageEvent event) {
		if(event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
			System.out.println("§c" + event.getNpc().getFireTicks());
		}
	}

	@EventHandler
	public void onNPCEvent(NPCSpawnEvent event) {}

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
						player.sendMessage("§8[§a+§8] §eCreated NPC §r" + npc.getDisplayName() + " §ewith ID: §a" + id);
						return true;
					}
					player.sendMessage("§c/npc create <name>");
					return true;

				case "remove":

					if (args.length == 2) {
						int id = Integer.parseInt(args[1]);
						if (fakePlayers.containsKey(id)) {
							NPC npc = fakePlayers.get(id);
							String name = fakePlayers.get(id).getDisplayName();
							fakePlayers.remove(id);
							npc.despawn();
							this.npc = null;
							player.sendMessage("§8[§c-§8] §eRemoved NPC §r" + name + " §ewith ID: §c" + id);
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
							player.sendMessage("§eSelected §r" + npc.getDisplayName());
							return true;
						}
						player.sendMessage("§c/npc select <ID>");
						return true;
					} else if (args.length == 2) {
						int id = Integer.parseInt(args[1]);
						if (fakePlayers.containsKey(id)) {
							npc = fakePlayers.get(id);
							player.sendMessage("§eSelected §r" + npc.getDisplayName() + " §ewith ID: §a" + id);
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
							fakePlayers.forEach((id, npc) -> player.sendMessage("§e" + id + ": §r" + npc.getDisplayName()));
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
						if (args.length == 1) {
							player.sendMessage(npc.getHealth() + "");
							return true;
						} else if (args.length == 2) {
							((FakePlayer)npc).setHealth(Float.parseFloat(args[1]));
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

				case "tp":

					if (npc != null) {
						if (args.length == 4) {
							Location location = npc.getLocation().clone().add(Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]));
							npc.teleport(location);
							return true;
						}
						player.sendMessage("§c/npc " + args[0]);
						return true;
					}
					player.sendMessage("§cSelect a NPC first");
					return true;

				case "freeze":

					if (npc != null) {
						if (args.length == 1) {
							npc.freeze(!npc.isFrozen());
							return true;
						}
						player.sendMessage("§c/npc " + args[0]);
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
			fakePlayers.forEach( (id, npc) -> {
				if(npc != null && npc.isAlive() && !npc.isFrozen()) {
					if(npc.hasTarget() && !npc.getTarget().isDead()) {
						Vector vNPC = new Vector(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ());
						Vector vTarget = new Vector(npc.getTarget().getLocation().getX(), npc.getTarget().getLocation().getY(), npc.getTarget().getLocation().getZ());
						double distance = vNPC.distance(vTarget);
						if(distance > 3) {
							npc.step(npc.getTarget().getLocation());
						}
						if(distance <= 2.5 && npc.getNature() == EnumNature.HOSTILE){
							npc.attack(npc.getTarget());
						}
						npc.look(npc.getTarget().getEyeLocation());
					}
				}
				npc.tick();
			});
		}
	};
}
