package ui;

import model.SolarPanels;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StartPage {
    private static final String JSON_STORE = "./data/solarPanels.json";

    private JFrame jframe;
    private JButton newSolarPanel;
    private JButton loadSolarPanel;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private SolarPanels solarPanels;

    public StartPage() {
        solarPanels = new SolarPanels();
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        jframe = new JFrame();
        newSolarPanel = new JButton("Add new");
        loadSolarPanel = new JButton("Load");
        loadSolarPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSolarPanelAction();
            }
        });
        newSolarPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newSolarAction();
            }
        });
        jframe.setLayout(new GridLayout());
        jframe.add(newSolarPanel);
        jframe.add(loadSolarPanel);
        jframe.setVisible(true);
    }

    public void save(SolarPanels solarPanls) {
        try {
            jsonWriter.open();
            jsonWriter.write(solarPanls);
            jsonWriter.close();
            System.out.println("Saved " + solarPanls.getSolarpanels() + " to " + JSON_STORE);

        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    private void loadSolarPanelAction() {
        try {
            solarPanels = jsonReader.read();
        } catch (IOException e12) {
            System.out.println("Unable to read from file: ");
        }
        try {
            new SolarPanelUI(solarPanels);
            jframe.setVisible(false);

        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }

    private void newSolarAction() {
        try {
            new SolarPanelUI(solarPanels);
            jframe.setVisible(false);

        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }
}
