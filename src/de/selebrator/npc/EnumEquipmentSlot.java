package de.selebrator.npc;

import net.minecraft.server.v1_9_R1.EnumItemSlot;

public enum EnumEquipmentSlot {
    MAIN_HAND(EnumItemSlot.MAINHAND),
    OFF_HAND(EnumItemSlot.OFFHAND),
    BOOTS(EnumItemSlot.FEET),
    LEGGINGS(EnumItemSlot.LEGS),
    CHESTPLATE(EnumItemSlot.CHEST),
    HELMET(EnumItemSlot.HEAD);

    private EnumItemSlot NMSSlot;

    EnumEquipmentSlot(EnumItemSlot NMSSlot) {
        this.NMSSlot = NMSSlot;
    }
    
    public EnumItemSlot getNMS() {
    	return this.NMSSlot;
    }
}
