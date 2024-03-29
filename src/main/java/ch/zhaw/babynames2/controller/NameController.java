package ch.zhaw.babynames2.controller;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVReader;

import ch.zhaw.babynames2.model.Name;


@RestController
public class NameController {
    private ArrayList<Name> listOfNames;

    @GetMapping("/names")
    public ArrayList<Name> getNames() {
        return listOfNames;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws Exception {
        listOfNames = new ArrayList<>();
        Path path = Paths.get(ClassLoader.getSystemResource("data/babynames.csv").toURI());
        System.out.println("Read from: " + path);
        try (Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    listOfNames.add(new Name(line[0], line[1], Integer.parseInt(line[2])));
                }
                System.out.println("Read " + listOfNames.size() + " names");
            }
        }
    }

    @GetMapping("/names/count")
    public long getCount(@RequestParam(required = false) String sex) {
        if (sex != null) {
            // Filtern der Liste nach dem Geschlecht und Zählen der Einträge
            return listOfNames.stream()
                    .filter(name -> name.getGeschlecht().equalsIgnoreCase(sex))
                    .count();
        } else {
            // Rückgabe der Gesamtgröße der Liste, wenn kein Geschlecht angegeben ist
            return listOfNames.size();
        }
    } 
    
    @GetMapping("/names/frequency")
    public int getFrequency(@RequestParam String name) {
        return listOfNames.stream()
                .filter(n -> n.getName().equalsIgnoreCase(name))
                .mapToInt(Name::getAnzahl)
                .sum();
    }
    
    @GetMapping("/names/name")
    public ResponseEntity<List<String>> filterNames(
        @RequestParam String sex,
        @RequestParam String start,
        @RequestParam int length) {
    
        // Validierung der Eingabeparameter
        if (sex == null || start == null || length <= 0) {
            return ResponseEntity.badRequest().body(null); // Ungültige Eingabe
        }
    
        String startLowerCase = start.toLowerCase(); // Konvertiere `start` in Kleinbuchstaben
    
        // Filtern der Namen basierend auf den Parametern
        List<String> filteredNames = listOfNames.stream()
            .filter(name -> name.getGeschlecht().equalsIgnoreCase(sex))
            .filter(name -> name.getName().toLowerCase().startsWith(startLowerCase)) // Ignoriere Groß-/Kleinschreibung
            .filter(name -> name.getName().length() <= length)
            .map(Name::getName)
            .collect(Collectors.toList());
    
        // Überprüfung, ob gefilterte Namen vorhanden sind
        if (filteredNames.isEmpty()) {
            return ResponseEntity.notFound().build(); // Keine Namen gefunden
        }
    
        return ResponseEntity.ok(filteredNames); // Rückgabe der gefilterten Namen
        
    }
    
     
}
