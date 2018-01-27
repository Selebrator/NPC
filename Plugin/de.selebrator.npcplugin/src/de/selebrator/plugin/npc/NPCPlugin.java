package de.selebrator.plugin.npc;

import de.selebrator.npc.Animation;
import de.selebrator.npc.entity.*;
import de.selebrator.npc.fake.entity.*;
import de.selebrator.plugin.npc.fetcher.GameProfileBuilder;
import de.selebrator.plugin.npc.gui.*;
import de.selebrator.reflection.ServerPackage;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class NPCPlugin extends JavaPlugin implements Listener, CommandExecutor {

	public static final String PLUGIN_VERSION = "v1_12_R1";
	public static final String SERVER_VERSION = ServerPackage.getVersion();
	public static final boolean STABLE = PLUGIN_VERSION.equals(SERVER_VERSION);
	public static final NumberFormat COORDINATE_FORMAT = DecimalFormat.getNumberInstance(Locale.ENGLISH);
	public static Logger logger;

	public static final String CREATE_PARAMETERS = " <type> [name]";
	public static final String REMOVE_PARAMETERS = "";
	public static final String SELECT_PARAMETERS = " [EID]";
	public static final String LIST_PARAMETERS = "";
	public static final String SPAWN_PARAMETERS = " [<x> <y> <z>] [yaw] [pitch]";
	public static final String DESPAWN_PARAMETERS = "";
	public static final String EQUIP_PARAMETERS = " [slot]";
	public static final String ANIMATION_PARAMETERS = " [animation]";
	public static final String STATUS_PARAMETERS = " <entity effect>";
	public static final String HEALTH_PARAMETERS = " [0 - 20]";
	public static final String TARGET_PARAMETERS = " <player>";
	public static final String UPDATE_PARAMETERS = " <name> [skinowner]";
	public static final String TP_PARAMETERS = " [<x> <y> <z>] [yaw] [pitch]";
	public static final String FREEZE_PARAMETERS = "";
	public static final String EFFECT_PARAMETERS = "";
	public static final String ABSORPTION_PARAMETERS = " <amount>";
	public static final String DAMAGE_PARAMETERS = " <amount>";

	static {
		COORDINATE_FORMAT.setMinimumFractionDigits(1);
		COORDINATE_FORMAT.setMaximumFractionDigits(3);
	}

	private Set<EntityNPC> npcs = new HashSet<>();

	private EntityNPC selected;
	private BukkitRunnable task = new BukkitRunnable() {

		@Override
		public void run() {
			Supplier<Stream<EntityNPC>> operableNPCs = () -> npcs.stream()
					.filter(Objects::nonNull)
					.filter(EntityNPC::isAlive)
					.filter(npc -> !npc.isFrozen());

			operableNPCs.get()
					.filter(npc -> npc instanceof PlayerNPC)
					.map(npc -> (PlayerNPC) npc)
					.filter(PlayerNPC::hasTarget)
					.filter(npc -> !npc.getTarget().isDead())
					.forEach(npc -> {
						Vector vNPC = new Vector(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ());
						Vector vTarget = new Vector(npc.getTarget().getLocation().getX(), npc.getTarget().getLocation().getY(), npc.getTarget().getLocation().getZ());
						double distance = vNPC.distance(vTarget);
						if(distance < npc.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).getValue()) {
							if(distance > 3) {
								npc.step(npc.getTarget().getLocation());
							}
							npc.look(npc.getTarget().getEyeLocation());
						}
					});

			operableNPCs.get().forEach(EntityNPC::tick);
		}
	};

	private static TextComponent npcText(EntityNPC npc) {
		String name = npc.getCustomName().isEmpty() ? npc.getClass().getSimpleName().replace("Fake", "") : npc.getCustomName();
		TextComponent message = new TextComponent(name);
		TextComponent hoverType = new TextComponent("Type: " + npc.getType().name().toLowerCase() + "\n");
		TextComponent hoverEID = new TextComponent("EntityId: " + npc.getEntityId());
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { hoverType, hoverEID }));
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npc select " + npc.getEntityId()));
		return message;
	}

	private static TextComponent locationText(Location location) {
		TextComponent delimiter = new TextComponent(", ");
		TextComponent message = new TextComponent();
		TextComponent x = new TextComponent(COORDINATE_FORMAT.format(location.getX()));
		TextComponent y = new TextComponent(COORDINATE_FORMAT.format(location.getY()));
		TextComponent z = new TextComponent(COORDINATE_FORMAT.format(location.getZ()));
		message.addExtra(x);
		message.addExtra(delimiter);
		message.addExtra(y);
		message.addExtra(delimiter);
		message.addExtra(z);
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch()));
		return message;
	}

	private static Location parseLocation(Location relative, String[] args) {
		return new Location(
				relative.getWorld(),
				args[0].startsWith("~") ? relative.getX() + (args[0].length() == 1 ? 0.0d : Double.parseDouble(args[0].replace("~", ""))) : Double.parseDouble(args[0]),
				args[1].startsWith("~") ? relative.getY() + (args[1].length() == 1 ? 0.0d : Double.parseDouble(args[1].replace("~", ""))) : Double.parseDouble(args[1]),
				args[2].startsWith("~") ? relative.getZ() + (args[2].length() == 1 ? 0.0d : Double.parseDouble(args[2].replace("~", ""))) : Double.parseDouble(args[2]),
				args.length == 4 ? Float.parseFloat(args[3]) : 0.0f,
				args.length == 5 ? Float.parseFloat(args[4]) : 0.0f
		);
	}

	private static EntityNPC createNPCForType(EntityType type) {
		switch(type) {
			case ELDER_GUARDIAN:
				return new FakeElderGuardian();
			case WITHER_SKELETON:
				return new FakeWitherSkeleton();
			case STRAY:
				return new FakeStray();
			case HUSK:
				return new FakeHusk();
			case ZOMBIE_VILLAGER:
				return new FakeZombieVillager();
			case SKELETON_HORSE:
				return new FakeSkeletonHorse();
			case ZOMBIE_HORSE:
				return new FakeZombieHorse();
			case ARMOR_STAND:
				throw new UnsupportedOperationException();
			case DONKEY:
				return new FakeDonkey();
			case MULE:
				return new FakeMule();
			case EVOKER_FANGS:
				throw new UnsupportedOperationException();
			case EVOKER:
				throw new UnsupportedOperationException();
			case VEX:
				throw new UnsupportedOperationException();
			case VINDICATOR:
				throw new UnsupportedOperationException();
			case ILLUSIONER:
				throw new UnsupportedOperationException();
			case CREEPER:
				throw new UnsupportedOperationException();
			case SKELETON:
				return new FakeSkeleton();
			case SPIDER:
				throw new UnsupportedOperationException();
			case GIANT:
				throw new UnsupportedOperationException();
			case ZOMBIE:
				return new FakeZombie();
			case SLIME:
				throw new UnsupportedOperationException();
			case GHAST:
				throw new UnsupportedOperationException();
			case PIG_ZOMBIE:
				return new FakeZombiePigman();
			case ENDERMAN:
				return new FakeEnderman();
			case CAVE_SPIDER:
				throw new UnsupportedOperationException();
			case SILVERFISH:
				throw new UnsupportedOperationException();
			case BLAZE:
				throw new UnsupportedOperationException();
			case MAGMA_CUBE:
				throw new UnsupportedOperationException();
			case ENDER_DRAGON:
				throw new UnsupportedOperationException();
			case WITHER:
				throw new UnsupportedOperationException();
			case BAT:
				throw new UnsupportedOperationException();
			case WITCH:
				throw new UnsupportedOperationException();
			case ENDERMITE:
				throw new UnsupportedOperationException();
			case GUARDIAN:
				return new FakeGuardian();
			case SHULKER:
				return new FakeShulker();
			case PIG:
				return new FakePig();
			case SHEEP:
				return new FakeSheep();
			case COW:
				return new FakeCow();
			case CHICKEN:
				return new FakeChicken();
			case SQUID:
				throw new UnsupportedOperationException();
			case WOLF:
				return new FakeWolf();
			case MUSHROOM_COW:
				return new FakeMushroomCow();
			case SNOWMAN:
				return new FakeSnowman();
			case OCELOT:
				return new FakeOcelot();
			case IRON_GOLEM:
				return new FakeIronGolem();
			case HORSE:
				return new FakeHorse();
			case RABBIT:
				return new FakeRabbit();
			case POLAR_BEAR:
				return new FakePolarBear();
			case LLAMA:
				return new FakeLlama();
			case PARROT:
				return new FakeParrot();
			case VILLAGER:
				return new FakeVillager();
			default:
				throw new UnsupportedOperationException();
		}
	}

	@Override
	public void onEnable() {
		logger = this.getLogger();
		if(!STABLE) {
			this.getLogger().warning("Server version: " + SERVER_VERSION + ", Recommended version: " + PLUGIN_VERSION);
		}

		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("npc").setExecutor(this);

		this.selected = new FakePlayer(new GameProfileBuilder("Selebrator").build());
		this.npcs.add(selected);

		this.task.runTaskTimer(this, 0, 1);
	}

	@Override
	public void onDisable() {
		npcs.forEach(EntityNPC::despawn);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if(item != null && item.getType() == Material.STICK) { //spawn
			if(event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(selected != null) {
					selected.spawn(event.getClickedBlock().getLocation().add(0.5, 1, 0.5));
					return;
				}
				event.getPlayer().sendMessage(ChatColor.RED + "Select a NPC first");

			} else if(event.getAction() == Action.RIGHT_CLICK_AIR) {
				if(selected != null) {
					selected.spawn(event.getPlayer().getLocation());
					return;
				}
				event.getPlayer().sendMessage(ChatColor.RED + "Select a NPC first");
			}
		}
	}

	@EventHandler
	public void onEntityUse(PlayerInteractEntityEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if(item != null && item.getType() == Material.BLAZE_ROD && this.selected instanceof PlayerNPC) {
			((PlayerNPC) this.selected).setTarget((LivingEntity) event.getRightClicked());
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length >= 1) {
			String command = ChatColor.RED + "/" + label + " " + args[0];

			switch(args[0]) {
				case "create":

					if(args.length >= 2) {
						try {
							EntityType type = EntityType.valueOf(args[1].toUpperCase());
							if(type == EntityType.PLAYER) {
								this.selected = new FakePlayer(new GameProfileBuilder(args[2]).build());
							} else {
								this.selected = createNPCForType(type);
								if(args.length == 3) {
									this.selected.setCustomName(args[2]);
									this.selected.setCustomNameVisible(true);
								}
							}
							this.npcs.add(this.selected);

							TextComponent message = new TextComponent(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW + "Created NPC " + ChatColor.RESET);
							message.addExtra(npcText(this.selected));
							sender.spigot().sendMessage(message);
							return true;
						} catch(IllegalArgumentException e) {
							sender.sendMessage("Unknown entity type");
							return true;
						} catch(UnsupportedOperationException e) {
							sender.sendMessage("Unsupported entity type");
							return true;
						}
					}
					sender.sendMessage(command + CREATE_PARAMETERS);
					return true;

				case "remove":

					if(args.length == 1) {
						this.selected.despawn();
						TextComponent message = new TextComponent(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW + "Removed NPC " + ChatColor.RESET);
						message.addExtra(npcText(this.selected));
						sender.spigot().sendMessage(message);
						this.npcs.remove(this.selected);
						this.selected = null;
						return true;
					}

					sender.sendMessage(command + REMOVE_PARAMETERS);
					return true;

				case "select":

					if(args.length == 2) {
						try {
							int id = Integer.parseInt(args[1]);
							Optional<EntityNPC> newSelected = this.npcs.stream()
									.filter(npc -> npc.getEntityId() == id)
									.findFirst();
							this.selected = newSelected.orElse(null);

							TextComponent message = new TextComponent("Selected ");
							message.addExtra(newSelected.isPresent() ? npcText(this.selected) : new TextComponent("nothing"));
							sender.spigot().sendMessage(message);
							return true;
						} catch(NumberFormatException e) {
							sender.sendMessage("\"" + ChatColor.RED + args[1] + "\" is not a valid EID");
							return true;
						}
					}
					sender.sendMessage(command + SELECT_PARAMETERS);
					return true;

				case "list":

					if(args.length == 1) {
						TextComponent message = new TextComponent();
						TextComponent delimiter = new TextComponent(", ");
						for(Iterator<EntityNPC> iterator = this.npcs.iterator(); iterator.hasNext(); ) {
							EntityNPC npc = iterator.next();
							message.addExtra(npcText(npc));
							if(iterator.hasNext())
								message.addExtra(delimiter);
						}
						sender.spigot().sendMessage(message);
						return true;
					}
					sender.sendMessage(command + LIST_PARAMETERS);
					return true;

				case "spawn":

					if(this.selected != null) {

						if(args.length == 1 || args.length == 4 || args.length == 5 || args.length == 6) {
							Location location;
							if(args.length == 1) {
								location = ((Player) sender).getLocation();
							} else {
								location = parseLocation(((Player) sender).getLocation(), Arrays.copyOfRange(args, 1, args.length));
							}
							this.selected.spawn(location);

							TextComponent message = new TextComponent(ChatColor.YELLOW + "Spawned NPC " + ChatColor.RESET);
							message.addExtra(npcText(this.selected));
							message.addExtra(ChatColor.YELLOW + " at " + ChatColor.RESET);
							message.addExtra(locationText(location));
							sender.spigot().sendMessage(message);
							return true;
						}

						sender.sendMessage(command + SPAWN_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "despawn":

					if(this.selected != null) {
						if(args.length == 1) {
							this.selected.despawn();
							return true;
						}
						sender.sendMessage(command + DESPAWN_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "equip":

					if(this.selected != null) {
						if(!(this.selected instanceof PlayerNPC)) {
							sender.sendMessage(ChatColor.RED + "Only PlayerNPCs have equipment");
							return true;
						}

						if(!(sender instanceof Player)) {
							sender.sendMessage(ChatColor.RED + "Only players can change NPC equipment");
							return true;
						}

						if(args.length == 1) {
							new EquipGUI((PlayerNPC) this.selected, this).open((Player) sender);
							return true;
						} else if(args.length == 2) {
							((PlayerNPC) this.selected).getEquipment().set(EquipmentSlot.valueOf(args[1].toUpperCase()), ((Player) sender).getInventory().getItemInMainHand());
							return true;
						}
						sender.sendMessage(command + EQUIP_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "animation":

					if(this.selected != null) {
						if(args.length == 1) {
							if(!(sender instanceof Player)) {
								sender.sendMessage(ChatColor.RED + "Only players can open the Animation GUI");
								return true;
							}
							new AnimationGUI(this.selected, this).open((Player) sender);
							return true;
						} else if(args.length == 2) {
							this.selected.playAnimation(Animation.valueOf(args[1].toUpperCase()));
							return true;
						}
						sender.sendMessage(command + ANIMATION_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "status":

					if(this.selected != null) {
						if(args.length == 1) {
							if(!(sender instanceof Player)) {
								sender.sendMessage(ChatColor.RED + "Only players can open the Status GUI");
								return true;
							}
							new EntityEffectGUI(this.selected, this).open((Player) sender);
							return true;
						} else if(args.length == 2) {
							this.selected.playAnimation(Animation.valueOf(args[1].toUpperCase()));
							return true;
						}
						sender.sendMessage(command + STATUS_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "health":

					if(this.selected != null) {
						if(!(this.selected instanceof LivingNPC)) {
							sender.sendMessage(ChatColor.RED + "Only LivingNPCs have health");
							return true;
						}

						if(args.length == 1) {
							sender.sendMessage(String.valueOf(((LivingNPC) this.selected).getHealth()));
							return true;
						} else if(args.length == 2) {
							((LivingNPC) this.selected).setHealth(Float.parseFloat(args[1]));
							return true;
						}
						sender.sendMessage(command + HEALTH_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "target":

					if(this.selected != null) {
						if(!(this.selected instanceof PlayerNPC)) {
							sender.sendMessage(ChatColor.RED + "Only PlayerNPCs can have targets");
							return true;
						}

						if(args.length == 2) {
							((PlayerNPC) this.selected).setTarget(Bukkit.getPlayer(args[1]));
							return true;
						}
						sender.sendMessage(command + TARGET_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "update":

					if(this.selected != null) {
						if(!(this.selected instanceof PlayerNPC)) {
							sender.sendMessage(ChatColor.RED + "Only PlayerNPCs have a GameProfile");
							return true;
						}

						if(args.length == 2) {
							((PlayerNPC) this.selected).setGameProfile(new GameProfileBuilder(args[1]).build());
							return true;
						} else if(args.length == 3) {
							((PlayerNPC) this.selected).setGameProfile(new GameProfileBuilder(args[1], args[2]).build());
							return true;
						}
						sender.sendMessage(command + UPDATE_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "tp":

					if(this.selected != null) {
						if(args.length == 4 || args.length == 5 || args.length == 6) {
							Location location = parseLocation(this.selected.getLocation(), Arrays.copyOfRange(args, 1, args.length));
							this.selected.teleport(location);

							TextComponent message = new TextComponent(ChatColor.YELLOW + "Teleported NPC " + ChatColor.RESET);
							message.addExtra(npcText(this.selected));
							message.addExtra(ChatColor.YELLOW + " to " + ChatColor.RESET);
							message.addExtra(locationText(location));
							sender.spigot().sendMessage(message);
							return true;
						}
						sender.sendMessage(command + TP_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "freeze":

					if(this.selected != null) {
						if(args.length == 1) {
							this.selected.setFrozen(!this.selected.isFrozen());
							TextComponent message = new TextComponent(ChatColor.YELLOW + (this.selected.isFrozen() ? "Froze" : "Unfroze") + " NPC " + ChatColor.RESET);
							message.addExtra(npcText(this.selected));
							sender.spigot().sendMessage(message);
							return true;
						}
						sender.sendMessage(command + FREEZE_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "effect":

					if(this.selected != null) {
						if(!(this.selected instanceof LivingNPC)) {
							sender.sendMessage(ChatColor.RED + "Only LivingNPCs can have potion effects");
							return true;
						}

						if(!(sender instanceof Player)) {
							sender.sendMessage(ChatColor.RED + "Only players can change NPC potion effects");
							return true;
						}

						if(args.length == 1) {
							((LivingNPC) this.selected).addPotionEffects(((Player) sender).getActivePotionEffects());
							return true;
						}
						sender.sendMessage(command + EFFECT_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "absorption":

					if(this.selected != null) {
						if(!(this.selected instanceof PlayerNPC)) {
							sender.sendMessage(ChatColor.RED + "Only PlayerNPCs can have absorption hearts");
							return true;
						}

						if(args.length == 2) {
							((PlayerNPC) this.selected).setAbsorption(Float.parseFloat(args[1]));
							return true;
						}
						sender.sendMessage(command + ABSORPTION_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "damage":

					if(this.selected != null) {
						if(!(this.selected instanceof LivingNPC)) {
							sender.sendMessage(ChatColor.RED + "Only LivingNPCs can be damaged");
							return true;
						}

						if(args.length == 2) {
							((LivingNPC) this.selected).damage(Float.parseFloat(args[1]), EntityDamageEvent.DamageCause.ENTITY_ATTACK);
							return true;
						}
						sender.sendMessage(command + DAMAGE_PARAMETERS);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "test":

					if(selected != null) {
						if(args.length == 1) {

							return true;
						}
						sender.sendMessage(command);
						return true;
					}
					sender.sendMessage(ChatColor.RED + "Select a NPC first");
					return true;

				case "help":

					String[] commands = {
							ChatColor.GRAY + "----------------" + ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "NPC" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + "----------------",
							ChatColor.GREEN + "/npc create" 	+ CREATE_PARAMETERS 	+ ChatColor.GRAY + " - create a new NPC",
							ChatColor.GREEN + "/npc remove" 	+ REMOVE_PARAMETERS 	+ ChatColor.GRAY + " - remove a existing NPC",
							ChatColor.GREEN + "/npc select" 	+ SELECT_PARAMETERS 	+ ChatColor.GRAY + " - select a NPC for editing",
							ChatColor.GREEN + "/npc list" 		+ LIST_PARAMETERS 		+ ChatColor.GRAY + " - list all NPCs",
							ChatColor.GREEN + "/npc spawn" 		+ SPAWN_PARAMETERS 		+ ChatColor.GRAY + " - spawn the NPC",
							ChatColor.GREEN + "/npc despawn" 	+ DESPAWN_PARAMETERS 	+ ChatColor.GRAY + " - despawn the NPC",
							ChatColor.GREEN + "/npc equip" 		+ EQUIP_PARAMETERS 		+ ChatColor.GRAY + " - equip the NPC with your held item",
							ChatColor.GREEN + "/npc animation" 	+ ANIMATION_PARAMETERS 	+ ChatColor.GRAY + " - make the NPC move",
							ChatColor.GREEN + "/npc status" 	+ STATUS_PARAMETERS 	+ ChatColor.GRAY + " - play a entity effect",
							ChatColor.GREEN + "/npc health" 	+ HEALTH_PARAMETERS 	+ ChatColor.GRAY + " - query / set the NPCs health",
							ChatColor.GREEN + "/npc target" 	+ TARGET_PARAMETERS 	+ ChatColor.GRAY + " - make the NPC constantly look at his target",
							ChatColor.GREEN + "/npc update" 	+ UPDATE_PARAMETERS 	+ ChatColor.GRAY + " - change the NPCs appearance",
							ChatColor.GREEN + "/npc tp"			+ TP_PARAMETERS 		+ ChatColor.GRAY + " - teleport",
							ChatColor.GREEN + "/npc freeze" 	+ FREEZE_PARAMETERS 	+ ChatColor.GRAY + " - toggle freeze",
							ChatColor.GREEN + "/npc effect"		+ EFFECT_PARAMETERS 	+ ChatColor.GRAY + " - copy your effects to the NPC",
							ChatColor.GREEN + "/npc absorption"	+ ABSORPTION_PARAMETERS	+ ChatColor.GRAY + " - set the NPCs absorption hearts",
							ChatColor.GREEN + "/npc damage"		+ DAMAGE_PARAMETERS 	+ ChatColor.GRAY + " - damage the NPC"
					};

					sender.sendMessage(commands);
					return true;
			}
		}
		sender.sendMessage(ChatColor.GREEN + "/npc help");
		return true;
	}
}
