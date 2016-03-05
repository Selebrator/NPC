package me.selebrator.npc;

import me.selebrator.reflection.Reflection;
import me.selebrator.reflection.ServerPackage;

public enum EnumDataWatcherObject {

	ENTITY_STATUS_BITMASK_00("Entity", "ax"),
	ENTITY_AIR_01("Entity", "ay"),
	ENTITY_NAME_02("Entity", "az"),
	ENTITY_NAME_VISIBLE_03("Entity", "aA"),
	ENTITY_SILENT_04("Entity", "aB"),
	
	LIVING_HAND_05("EntityLiving", "as"),
	LIVING_HELATH_06("EntityLiving", "HEALTH"),
	LIVING_POTION_COLOR_07("EntityLiving", "f"),
	LIVING_POTION_AMBIENT_08("EntityLiving", "g"),
	LIVING_ARROWS_09("EntityLiving", "h"),
	
	HUMAN_ABSORBTION_10("EntityHuman", "a"),
	HUMAN_SCORE_11("EntityHuman", "b"),
	HUMAN_SKIN_BITBASK_12("EntityHuman", "bp"),
	HUMAN_HAND_13("EntityHuman", "bq");
	
	private Object clazz;
	
	private EnumDataWatcherObject(String owner, String field) {
		Class<?> ownerClazz = Reflection.getClass(ServerPackage.NMS, owner);
		this.clazz = Reflection.getField(ownerClazz, field).get(null);
	}
	
	public Object getClazz() {
		return clazz;
	}
}
