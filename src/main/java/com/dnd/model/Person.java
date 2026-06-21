package com.dnd.model;

public class Person {
    private int idPerson;
    private int idClass;
    private int idRace;
    private short level;
    private String name;

    public Person() {}

    public Person(int idPerson, int idClass, int idRace, short level, String name) {
        this.idPerson = idPerson;
        this.idClass = idClass;
        this.idRace = idRace;
        this.level = level;
        this.name = name;
    }

    public int getIdPerson() { return idPerson; }
    public void setIdPerson(int idPerson) { this.idPerson = idPerson; }

    public int getIdClass() { return idClass; }
    public void setIdClass(int idClass) { this.idClass = idClass; }

    public int getIdRace() { return idRace; }
    public void setIdRace(int idRace) { this.idRace = idRace; }

    public short getLevel() { return level; }
    public void setLevel(short level) { this.level = level; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}