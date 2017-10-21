package de.selebrator.npc.entity;

import com.mojang.authlib.GameProfile;
import de.selebrator.npc.MathHelper;
import de.selebrator.npc.entity.metadata.HumanMetadata;
import de.selebrator.npc.inventory.Equipment;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public interface PlayerNPC extends LivingNPC, HumanMetadata {

	@Override
	default EntityType getType() {
		return EntityType.PLAYER;
	}

	/**
	 * Look at the given location
	 *
	 * @param location absolute target location
	 */
	default void look(Location location) {
		Vector distance = MathHelper.calcDistanceVector(this.getEyeLocation(), location);

		this.look(distance.getX(), distance.getY(), distance.getZ());
	}

	GameProfile getGameProfile();

	void setGameProfile(GameProfile gameProfile);

	String getDisplayName();

	default double getEyeHeight() {
		return this.getEyeHeight(false);
	}

	double getEyeHeight(boolean ignoreSneaking);

	default Location getEyeLocation() {
		return this.hasLocation() ? this.getLocation().clone().add(0, this.getEyeHeight(false), 0) : null;
	}

	Equipment getEquipment();

	boolean hasTarget();

	LivingEntity getTarget();

	void setTarget(LivingEntity target);
}
