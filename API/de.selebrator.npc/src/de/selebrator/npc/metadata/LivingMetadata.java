package de.selebrator.npc.metadata;

import org.bukkit.Color;

public interface LivingMetadata extends EntityMetadata {

	float getHealth();

	void setHealth(float health);

	Color getPotionColor();

	void setPotionColor(Color potionColor);

	boolean isPotionAmbient();

	void setPotionAmbient(boolean potionAmbient);

	int getArrows();

	void setArrows(int arrows);
}
