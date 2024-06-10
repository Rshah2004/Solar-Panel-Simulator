package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Contains a list of solar panels and contains different functions
public class SolarPanels {
    private List<SolarPanelType> solarpanels;
    private double totalenergy;

    // effects : creates a new ArrayList and initializes the total energy to 0
    public SolarPanels() {
        this.solarpanels = new ArrayList<>();
        this.totalenergy = 0;
    }

    // modifies : this,
    // effects : adds the solar panel to a list of solar panels
    public void addSolarPanels(SolarPanelType solar) {
        solarpanels.add(solar);
        EventLog.getInstance().logEvent(new Event("Added solarpanel: " + solar.getSolarType()));
    }

    // modifies : this,
    // effects  : return the total energy
    public double calculateTotalEnergy() {
        for (int k = 0; k < solarpanels.size(); k++) {
            SolarPanelType c = solarpanels.get(k);
            c.totalEnergy();
            totalenergy = totalenergy + c.gettotalEnergy();
        }
        return totalenergy;
    }

    // modifies : this,
    // effects  : replaces the solar panel at the current index with a new solar panel at that index
    public SolarPanelType editSolarPanel(int index, SolarPanelType newSolar) {
        EventLog.getInstance().logEvent(new Event("editted panel: " + newSolar.getSolarType()));
        solarpanels.set(index, newSolar);
        return solarpanels.get(index);
    }

    // modifies : this
    // effects : removes a particular solar panel from the list by using the index
    public void removeSolarPanel(int index) {
        solarpanels.remove(index);
    }

    public List<SolarPanelType> getSolarpanels() {
        return solarpanels;
    }

    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("SolarPanels", solarPanelsToJson());
        return json;
    }

    // EFFECTS : returns solar panel from solarpanels as a json array
    public JSONArray solarPanelsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (SolarPanelType solarpanel : solarpanels) {
            jsonArray.put(solarpanel.toJson());
        }
        return jsonArray;
    }



}
