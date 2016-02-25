package me.selebrator.npc;

public enum Status {
    HURT(2),
    DEAD(3);
    
    private int id;

    private Status(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}