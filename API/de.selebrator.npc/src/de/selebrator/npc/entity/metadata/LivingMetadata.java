package de.selebrator.npc.entity.metadata;

import org.bukkit.Color;

import static de.selebrator.npc.entity.metadata.MetadataObject.LivingFlag.ACTIVE_HAND;
import static de.selebrator.npc.entity.metadata.MetadataObject.LivingFlag.HAND_ACTIVE;

public interface LivingMetadata extends EntityMetadata {

	byte getHandInfo();

	default boolean getHandInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getHandInfo(), target);
	}

	void setHandInfo(byte value);

	default void setHandInfo(int target, boolean state) {
		this.setHandInfo(MetadataObject.setBitmaskValue(this.getHandInfo(), target, state));
	}

	default boolean isAnyHandActive() {
		return this.getHandInfo(HAND_ACTIVE);
	}

	default boolean isHandActive(boolean mainHand) {
		return this.isAnyHandActive() && (this.getHandInfo(ACTIVE_HAND) == mainHand);
	}

	default void setHandActive(boolean active) {
		this.setHandInfo(HAND_ACTIVE, active);
	}

	float getHealth();

	void setHealth(float health);

	Color getPotionColor();

	void setPotionColor(Color potionColor);

	boolean isPotionAmbient();

	void setPotionAmbient(boolean potionAmbient);

	int getArrows();

	void setArrows(int arrows);
}
