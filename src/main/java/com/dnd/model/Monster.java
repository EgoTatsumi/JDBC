package com.dnd.model;

public class Monster {
    private String name;
    private short strength;
    private short level;

    public Monster() {}

    public Monster(String name, short strength, short level) {
        this.name = name;
        this.strength = strength;
        this.level = level;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public short getStrength() { return strength; }
    public void setStrength(short strength) { this.strength = strength; }

    public short getLevel() { return level; }
    public void setLevel(short level) { this.level = level; }
}