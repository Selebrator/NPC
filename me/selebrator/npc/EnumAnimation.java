package me.selebrator.npc;

public enum EnumAnimation {
    SWING_ARM(0),
    TAKE_DAMAGE(1),
    LEAVE_BED(2),
    EAT_FOOD(3),
    CRITICAL_EFFECT(4),
    MAGIC_CRITICAL_EFFECT(5),
    CROUCH(104),
    UNCROUCH(105);
    
    private int id;

    private EnumAnimation(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
