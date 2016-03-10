package de.selebrator.npc;

public enum EnumEntityStatus {
    HURT(2),
    DEAD(3);
    
    private int id;

    EnumEntityStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
