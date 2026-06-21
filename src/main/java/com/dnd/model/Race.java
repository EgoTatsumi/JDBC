package com.dnd.model;

public class Race {
    private int idRace;
    private String raceName;

    public Race() {}

    public Race(int idRace, String raceName) {
        this.idRace = idRace;
        this.raceName = raceName;
    }

    public int getIdRace() { return idRace; }
    public void setIdRace(int idRace) { this.idRace = idRace; }

    public String getRaceName() { return raceName; }
    public void setRaceName(String raceName) { this.raceName = raceName; }
}