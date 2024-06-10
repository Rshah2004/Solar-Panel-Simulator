package model;

import static java.lang.Math.cos;

// creates a Perc type solar panel
public class Perc extends SolarPanelType {

    // effects : sets the solar type to Perc
    public Perc() {
        super("Perc");
    }

    // modifies : this
    // effects : sets the weather condition to 0, if winter, 1 if summer, 0.5 if Spring/ Autumn
    @Override
    public void setWeather(String weather) {
        if (weather.equals("Winter")) {
            this.weatherCondition = 0;
        } else if (weather.equals("Summer")) {
            this.weatherCondition = 1;
        } else {
            this.weatherCondition = 0.5;
        }
    }
}