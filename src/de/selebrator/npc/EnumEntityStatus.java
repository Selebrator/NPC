package de.selebrator.npc;

import de.selebrator.reflection.Reflection;
import org.bukkit.EntityEffect;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

//TODO remove version dependent import

public enum EnumEntityStatus {
	TIPPED_ARROW_PARTICLE(0, "TippedArrow"),
	RABBIT_JUMP(1, "Rabbit"),
	MINECART_TNT_IGNITE(1, "MinecartTNT"),
	HURT(2, "Living"),
	DEATH(3, "Living"),
	SNOWBALLPOOF(3, "Snowball"),
	EGG_IRONCRACK(3, "Egg"),
	IRON_GOLEM_ATTACK(4, "IronGolem"),
	EVOCATION_FANGS_ATTACK(4, "EvokerFangs"),
	TAME_SMOKE(6, "TameableAnimal"),
	HORSE_TAME_SMOKE(6, "HorseAbstract"),
	TAME_HEARTS(7, "TameableAnimal"),
	HORSE_TAME_HEARTS(7, "HorseAbstract"),
	WOLF_SHAKE(8, "Wolf"),
	CONSUME_ITEM(9, "Player"),
	SHEEP_EAT(10, "Sheep"),
	MINECART_SPAWNER_RESET(10, "MinecartMobSpawner"),
	IRON_GOLEM_ROSE(11, "IronGolem"),
	VILLAGER_HEART(12, "Villager"),
	VILLAGER_ANGRY(13, "Villager"),
	VILLAGER_HAPPY(14, "Villager"),
	WITCH_MAGIC(15, "Witch"),
	ZOMBIE_TRANSFORM(16, "Zombie"),
	FIREWORK_EXPLODE(17, "Fireworks"),
	ANIMAL_LOVE(18, "Animal"),
	RESET_SQUID_ROTATION(19, "Squid"),
	BLOCK_PARTICLE(20, "Insentient"),
	GUARDIAN_SOUND(21, "Entity"),
	ENABLE_DEBUG(22, "Player"),
	DISABLE_DEBUG(23, "Player"),
	OP_0(24, "Player"),
	OP_1(25, "Player"),
	OP_2(26, "Player"),
	OP_3(27, "Player"),
	OP_4(28, "Player"),
	SHIELD_BLOCK(29, "Living"),
	SHIELD_BREAK(30, "Living"),
	FISHING_HOOK_PULL(31, "FishingHook"),
	ARMORSTAND_HIT(32, "ArmorStand"),
	THORNS(33, "Living"),
	IRON_GOLEM_ROSE_CANCEL(34, "IronGolem"),
	TOTEM_OF_UNDYING(35, "Entity");

	private byte id;
	private Class<?> executor;

	EnumEntityStatus(int id, String executor) {
		this.id = (byte) id;
		if(!executor.startsWith("Entity")) executor = "Entity" + executor;
		this.executor = Reflection.getMinecraftClass(executor);
	}

	public static EnumEntityStatus fromBukkit(EntityEffect effect) {
		switch(effect) {
			case HURT:
				return HURT;
			case DEATH:
				return DEATH;
			case WOLF_SMOKE:
				return TAME_SMOKE;
			case WOLF_HEARTS:
				return TAME_HEARTS;
			case WOLF_SHAKE:
				return WOLF_SHAKE;
			case SHEEP_EAT:
				return SHEEP_EAT;
			case IRON_GOLEM_ROSE:
				return IRON_GOLEM_ROSE;
			case VILLAGER_HEART:
				return VILLAGER_HEART;
			case VILLAGER_ANGRY:
				return VILLAGER_ANGRY;
			case VILLAGER_HAPPY:
				return VILLAGER_HAPPY;
			case WITCH_MAGIC:
				return WITCH_MAGIC;
			case ZOMBIE_TRANSFORM:
				return ZOMBIE_TRANSFORM;
			case FIREWORK_EXPLODE:
				return FIREWORK_EXPLODE;
			default:
				throw new IllegalArgumentException();
		}
	}

	public byte getId() {
		return this.id;
	}

	public boolean isExecutableBy(Entity entity) {
		return executor.isAssignableFrom(((CraftEntity) entity).getHandle().getClass());
	}
}
