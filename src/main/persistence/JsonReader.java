package persistence;

import model.Monocrystaline;
import model.Polycrystalline;
import model.Thinfilm;
import model.Perc;
import model.SolarPanelType;
import model.SolarPanels;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.stream.Stream;

import org.json.*;
// Code influced by the JsonSerizalizationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Represents a reader that reads SolarPanels from json stored in file

public class JsonReader {
    private String destination;
    private SolarPanelType sr1;

    // constructs a reader to read from the file
    public JsonReader(String source) {
        this.destination = source;
        sr1 = null;
    }

    // EFFECTS: reads solarPanels from file and returns it;
    // throws IOException if an error occurs reading data from file
    public SolarPanels read() throws IOException {
        String jsonData = readFile(destination);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSolarPanels(jsonObject);
    }

    // EFFECTS : reads it as a string and returns
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS : parses Solar panel and returns it
    private SolarPanels parseSolarPanels(JSONObject jsonObject) {
        SolarPanels sr = new SolarPanels();
        addSolarPanels(sr, jsonObject);
        return sr;
    }

    // EFFECTS : parses solar panel type from a list of solar panels and returns it
    private void addSolarPanels(SolarPanels sr, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("SolarPanels");
        for (Object json : jsonArray) {
            JSONObject nextSolarPanel = (JSONObject) json;
            addSolarPanel(sr, nextSolarPanel);
        }
    }

    // EFFECTS : parses solar panel from json Object and adds it to a list of solar panels
    private void addSolarPanel(SolarPanels sr, JSONObject jsonObject) {
        String type = jsonObject.getString("Type");
        double efficiency = jsonObject.getDouble("efficiency");
        String weather = jsonObject.getString("Weather");
        double angle = jsonObject.getDouble("angle");
        double shadingarea = jsonObject.getDouble("shadingArea");
        double area = jsonObject.getDouble("Total Area");
        int hours = jsonObject.getInt("solar hours");
        double solarRadiatoinAverage = jsonObject.getDouble("solar radiation average");
        sr1 = check(sr1, type);
        sr1.setArea(area);
        sr1.setEfficiency(efficiency);
        sr1.setAngle(angle);
        sr1.setSolarHours(hours);
        sr1.setWeather(weather);
        sr1.shadingArea(shadingarea);
        sr1.setSolarRadiationAverage(solarRadiatoinAverage);
        sr.addSolarPanels(sr1);
    }

    // EFFECTS: creates a new class based on the type
    private SolarPanelType check(SolarPanelType sr1, String type) {
        if (type.equals("Monocrystalline")) {
            sr1 = new Monocrystaline();
        } else if (type.equals("Polycrystalline")) {
            sr1 = new Polycrystalline();
        } else if (type.equals("Perc")) {
            sr1 = new Perc();
        } else {
            sr1 = new Thinfilm();
        }
        return sr1;
    }
}
