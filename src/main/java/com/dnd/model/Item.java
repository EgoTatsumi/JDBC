package com.dnd.model;

public class Item {
    private int idItem;
    private int idPersonFk;
    private short weight;
    private String magicProperties;

    public Item() {}

    public Item(int idItem, int idPersonFk, short weight, String magicProperties) {
        this.idItem = idItem;
        this.idPersonFk = idPersonFk;
        this.weight = weight;
        this.magicProperties = magicProperties;
    }

    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }

    public int getIdPersonFk() { return idPersonFk; }
    public void setIdPersonFk(int idPersonFk) { this.idPersonFk = idPersonFk; }

    public short getWeight() { return weight; }
    public void setWeight(short weight) { this.weight = weight; }

    public String getMagicProperties() { return magicProperties; }
    public void setMagicProperties(String magicProperties) { this.magicProperties = magicProperties; }
}