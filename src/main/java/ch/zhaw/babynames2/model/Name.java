package ch.zhaw.babynames2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class Name {
    private String name;
    private String geschlecht;
    private int anzahl;

    public Name(String name, String geschlecht, int anzahl) {
        this.name = name;
        this.geschlecht = geschlecht;
        this.anzahl = anzahl;
    }

    public String getName() {
        return name;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public int getAnzahl() {
        return anzahl;
    }

    @Override
    public String toString() {
        return "Name [name=" + name + ", geschlecht=" + geschlecht + ", anzahl=" + anzahl + "]";
    }
}
