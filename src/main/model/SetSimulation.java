package model;

import java.time.LocalTime;

// Presents a simulatino of the solar panel from 7 am to 3pm
public class SetSimulation {

    private double positionOfSun;
    private double angle;
    private double angleWithSun;
    private int timenow;

    private double efficiency;

    private String solarDirection;
    final double angleWithGround = 15;

    // effects : sets the position of the sun using the formula, and sets the time
    public SetSimulation(int timenow) {
        positionOfSun = Math.abs((15 * (timenow - 12)));
        this.timenow = timenow;

    }

    // modifies : this.
    // effects : changes the solar direction of the solar panel if time currently is 13 or more, else keeps it as it is
    public String setSolarDirection(String direction) {
        this.solarDirection = direction;
        if (timenow >= 13) {
            solarDirection = setAngleAccordingToSun("left");
        }
        return solarDirection;
    }

    // modifies : this,
    // effects : sets the angle made by the solar panel with sun ( 90 - position of sun - angle with ground)
    public double angleMadeWithSun() {
        this.angleWithSun = 90 - positionOfSun - angleWithGround;
        return angleWithSun;
    }

    // modifies : efficiency
    // effects  : adjusts the efficiency based on the obstacles, if it's raining, increases efficiency, as it cleans SP
    public double weather(double cloudCover, double dustDensity, double precipitation, double efficiencyAdjustment) {
        efficiencyAdjustment -= cloudCover * 0.1;
        efficiencyAdjustment -= dustDensity * 0.05;
        efficiencyAdjustment += precipitation * 0.2;
        efficiency += efficiencyAdjustment;
        efficiency = Math.max(0, Math.min(100, efficiency));
        return efficiency;
    }

    // modifies : this,
    // effects : changes the solar[anel's position according to the sun's posiiton
    public String setAngleAccordingToSun(String sunPosition) {
        if (sunPosition.equals("left")) {
            if (solarDirection.equals("right")) {
                solarDirection = "left";
            }
        } else if (sunPosition.equals("right") && solarDirection.equals("left")) {
            solarDirection = "right";
        } else {
            return solarDirection;
        }
        return solarDirection;
    }

}
