package de.selebrator.npc;

import de.selebrator.reflection.Reflection;
import de.selebrator.reflection.ServerPackage;

public enum EnumDataWatcherObject {
	ENTITY_STATUS_BITMASK_00("Entity", "ax"),
	ENTITY_AIR_01("Entity", "ay"),
	ENTITY_NAME_02("Entity", "az"),
	ENTITY_NAME_VISIBLE_03("Entity", "aA"),
	ENTITY_SILENT_04("Entity", "aB"),
	
	LIVING_UNKNOWN_05("EntityLiving", "as"),
	LIVING_HEAlTH_06("EntityLiving", "HEALTH"),
	LIVING_POTION_COLOR_07("EntityLiving", "f"),
	LIVING_POTION_AMBIENT_08("EntityLiving", "g"),
	LIVING_ARROWS_09("EntityLiving", "h"),

	HUMAN_ABSORPTION_10("EntityHuman", "a"),
	HUMAN_SCORE_11("EntityHuman", "b"),
	HUMAN_SKIN_BITBASK_12("EntityHuman", "bp"),
	HUMAN_MAINHAND_13("EntityHuman", "bq");
	
	private Object object;
	
	EnumDataWatcherObject(String owner, String field) {
		Class<?> ownerClazz = Reflection.getClass(ServerPackage.NMS, owner);
		this.object = Reflection.getField(ownerClazz, field).get(null);
	}
	
	public Object getObject() {
		return object;
	}
}
