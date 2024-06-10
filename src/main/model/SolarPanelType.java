package model;

import org.json.JSONObject;

// Represents all the properties of the solarpanel
public abstract class SolarPanelType {

    private double angle;
    private int hours;
    private double area;
    private double shadingarea;
    private double efficiency;
    private double solarRadiationAverage;
    private double te;
    private String solarType;

    protected double weatherCondition;
    protected String weather;

    public SolarPanelType(String stype) {
        this.solarType = stype;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }


    public void setAngle(double a) {
        this.angle = a;
    }


    public void setSolarHours(int c) {
        this.hours = c;
    }


    public abstract void setWeather(String weather);

    public void setArea(double a) {
        this.area = a - this.shadingarea;
    }


    public void setSolarRadiationAverage(double s) {
        this.solarRadiationAverage = s;
    }


    public void shadingArea(double a) {
        this.shadingarea = a;
    }

    public int getSolarhours() {
        return hours;
    }

    public double getSolarRadiation() {
        return solarRadiationAverage;
    }

    public String getSolarType() {
        return solarType;
    }

    public double getAngle() {
        return angle;
    }

    // requires : the solar panel's specifications must be added before trying to find the total energy
    // modifies : this,
    // effects  : returns total energy
    public void totalEnergy() {
        double x = Math.cos(Math.toRadians(this.angle));
        this.te = Math.abs(solarRadiationAverage * x * area * efficiency * weatherCondition * hours);
        this.te = Math.round(this.te);
    }

    public double gettotalEnergy() {
        totalEnergy();
        return this.te;
    }

    public double getEfficiency() {
        return this.efficiency;
    }

    public double getWeatherCondition() {
        return this.weatherCondition;
    }

    public double getArea() {
        return this.area;
    }

    public String getweather() {
        if (weatherCondition == 1) {
            weather = "Summer";
        } else if (weatherCondition == 0.5) {
            weather = "Spring|Autumn";
        } else {
            weather = "Winter";
        }
        return weather;
    }

    public double getShadingarea() {
        return shadingarea;
    }

    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Type", getSolarType());
        json.put("efficiency", efficiency);
        json.put("Weather",getweather());
        json.put("angle", angle);
        json.put("shadingArea", shadingarea);
        json.put("Total Area", area);
        json.put("solar hours", hours);
        json.put("solar radiation average", solarRadiationAverage);
        return json;
    }
}
