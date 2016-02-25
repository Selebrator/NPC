package me.selebrator.npc;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;

public interface NPC {
	
//	public void spawn();
	
//	public void despawn();

//	public int getEntityID();
	
	public UUID getUUID();
	
//	public String getName();
	
//	public void teleport(Location location);
	
//	public void teleport(Entity target);
	
//	public void setLocation(Location location);
	
//	public Location getLocation();
	
	public void setInvulnerable(boolean state);
	
	public boolean isInvulnerable();
	
	public void setGravity(boolean state);
	
	public boolean hasGravity();
	
	public void setCollision(boolean state);
	
	public boolean hasCollision();
	
	public void setFrozen(boolean state);
	
	public boolean isFrozen();
	
//	public void setTarget(Entity target);
	
//	public boolean hasTarget();
	
//	public Entity getTarget();
	
	public void setControllable(boolean state);
	
	public boolean isControllable();
	
	public void setPassenger(Entity passenger);
	
	public boolean hasPassenger();
	
	public Entity getPassenger();
	
//	public abstract void setEquipment(EquipmentSlot slot, ItemStack item);
	
//	public abstract boolean hasEquipment(EquipmentSlot slot);
	
//	public abstract ItemStack getEquipment(EquipmentSlot slot);
	
	public abstract void setGameMode(GameMode gamemode);
	
	public abstract GameMode getGameMode();
	
//	public void look(Entity target);
	
//	public void playAnimation(Animation animation);
}
