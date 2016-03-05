package me.selebrator.npc;

import net.minecraft.server.v1_9_R1.EnumItemSlot;

public enum EquipmentSlot {
    MAIN_HAND(0, EnumItemSlot.MAINHAND),
    OFF_HAND(1, EnumItemSlot.OFFHAND),
    BOOTS(2, EnumItemSlot.FEET),
    LEGGINGS(3, EnumItemSlot.LEGS),
    CHESTPLATE(4, EnumItemSlot.CHEST),
    HELMET(5, EnumItemSlot.HEAD);
    
    private int id;
    private EnumItemSlot NMSSlot;

    private EquipmentSlot(int id, EnumItemSlot NMSSlot) {
        this.id = id;
        this.NMSSlot = NMSSlot;
    }

    public int getID() {
        return this.id;
    }
    
    public EnumItemSlot getSlot() {
    	return this.NMSSlot;
    }
}