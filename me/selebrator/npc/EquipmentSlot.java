package me.selebrator.npc;

public enum EquipmentSlot {
    HAND(0),
    BOOTS(1),
    LEGGINGS(2),
    CHESTPLATE(3),
    HELMET(4);
    
    private int id;

    private EquipmentSlot(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}