package ch.zhaw.babynames2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Name {
    private String name;
    private String geschlecht;
    private int anzahl;
}