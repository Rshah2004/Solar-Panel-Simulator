package persistence;

import java.io.FileNotFoundException;

import model.SolarPanels;
import org.json.JSONObject;

import java.io.*;
// Code influced by the JsonSerizalizationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

// Represents a writer that writes JSON representation of Solar panels  to file
public class JsonWriter {
    private static final int INDENTATION = 2;
    private PrintWriter write;

    private String sourceFile;

    // EFFECTS: constructs writer to write to source file
    public JsonWriter(String file) {
        this.sourceFile = file;
    }

    // MODIFIES: this
    // EFFECTS: creates writer; throws FileNotFoundException if destination file cannot be found
    public void open() throws FileNotFoundException {
        write = new PrintWriter(sourceFile);
    }


    // MODIFIES: this
    // EFFECTS: writes solar panels as json to file
    public void write(SolarPanels sr) {
        JSONObject json = sr.toJson();
        saveToFile(json.toString(INDENTATION));
    }

    // MODIFIES : this
    // EFFECTS : closes the writer
    public void close() {
        write.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        write.print(json);
    }

}
