package me.selebrator.npc;

public enum EnumStatus {
    HURT(2),
    DEAD(3);
    
    private int id;

    private EnumStatus(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}
