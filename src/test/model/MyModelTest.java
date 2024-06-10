package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolarPanelTest {
    private SolarPanelType solar;
    private SolarPanels solarpanels;

    private Finance finances;

    @BeforeEach
    void runBefore() {
        this.solarpanels = new SolarPanels();
    }

    @Test
    void testConstructor() {
        assertEquals(0, solarpanels.getSolarpanels().size());
        assertEquals(0, solarpanels.calculateTotalEnergy());
    }

    @Test
    void testaddSolarPanels() {
        solar = new Monocrystaline();
        solarpanels.addSolarPanels(solar);
        assertEquals(1, solarpanels.getSolarpanels().size());
    }

    @Test
    void testaddMultipleSolarPanels() {
        solar = new Monocrystaline();
        solarpanels.addSolarPanels(solar);
        solar = new Polycrystalline();
        solarpanels.addSolarPanels(solar);
        solar = new Polycrystalline();
        solarpanels.addSolarPanels(solar);

        assertEquals(3, solarpanels.getSolarpanels().size());
    }

    @Test
    void testCalculateTotalEnergy() {
        solar = new Monocrystaline();
        solarpanels.addSolarPanels(solar);
        solar.setAngle(30.0);
        solar.setSolarHours(4);
        solar.setWeather("Summer");
        solar.setArea(100); // in m^2
        solar.setSolarRadiationAverage(340); // average solar radiation received by Earth
        solar.shadingArea(0);
        solar.setEfficiency(0.23);
        solar.totalEnergy();
        double x = solarpanels.calculateTotalEnergy();
        assertEquals(27089.0, x);

    }

    @Test
    void testMultipleCalculateTotalEnergy() {
        solar = new Monocrystaline();
        solarpanels.addSolarPanels(solar);
        solar.setAngle(30.0);
        solar.setSolarHours(4);
        solar.setWeather("Summer");
        solar.setArea(100); // in m^2
        solar.setSolarRadiationAverage(340); // average solar radiation received by Earth
        solar.shadingArea(0);
        solar.setEfficiency(0.23);
        assertEquals(1, solar.getWeatherCondition());
        solar = new Polycrystalline();
        solarpanels.addSolarPanels(solar);
        solar.setAngle(60.0);
        solar.setSolarHours(5);
        solar.setWeather("Autumn");
        solar.setSolarRadiationAverage(340); // average solar radiation received by Earth
        solar.shadingArea(50);
        solar.setEfficiency(0.23);
        solar.setArea(150); // in m^2
        double x = solarpanels.calculateTotalEnergy();
        assertEquals(36864.0, x);
        assertEquals(0.5, solar.getWeatherCondition());
        finances = new Finance();
        double s = finances.cost(solarpanels.getSolarpanels().get(1));
        assertEquals(9286.25, s);
    }

    @Test
    void testEditSolarPanelType() {
        solar = new Monocrystaline();
        solarpanels.addSolarPanels(solar);
        solar = new Polycrystalline();
        solarpanels.editSolarPanel(0, solar);
        assertEquals("Polycrystalline",solarpanels.getSolarpanels().get(0).getSolarType());
    }

    @Test
    void testRemoveSolarPanel() {
        solar = new Perc();
        solarpanels.addSolarPanels(solar);
        solar = new Thinfilm();
        solarpanels.addSolarPanels(solar);
        solarpanels.removeSolarPanel(0);
        assertEquals("Thinfilm", solarpanels.getSolarpanels().get(0).getSolarType());
    }

    @Test
    void testFinanceCost() {
        finances = new Finance();
        solar = new Monocrystaline();
        solarpanels.addSolarPanels(solar);
        solar.setAngle(30.0);
        solar.setSolarHours(4);
        solar.setWeather("Summer");
        solar.setArea(100); // in m^2
        solar.setSolarRadiationAverage(340); // average solar radiation received by Earth
        solar.shadingArea(0);
        solar.setEfficiency(0.23);
        solar.totalEnergy();
        double x = solarpanels.calculateTotalEnergy();
        assertEquals(1, solar.getWeatherCondition());
        assertEquals(27089.0, x);
        double y = finances.cost(solar);
        assertEquals(27089.0*1.25, y);
    }

    @Test
    void testFinanceprofit() {
        finances = new Finance();
        solar = new Monocrystaline();
        solarpanels.addSolarPanels(solar);
        solar.setAngle(30.0);
        solar.setSolarHours(4);
        solar.setWeather("Summer");
        solar.setArea(100); // in m^2
        solar.setSolarRadiationAverage(340); // average solar radiation received by Earth
        solar.shadingArea(0);
        solar.setEfficiency(0.23);
        solar.totalEnergy();
        double x = solarpanels.calculateTotalEnergy();
        assertEquals(27089.0, x);
        double y = finances.cost(solar);
        assertEquals(27089.0*1.25, y);
        double z = finances.profit(1000, y);
        assertEquals(-158138.75,z);
    }

    @Test
    void testSimulation() {
        SetSimulation simulation = new SetSimulation(12);
        simulation.setSolarDirection("right");
        assertEquals(0,0);
        assertEquals("left", simulation.setAngleAccordingToSun("left"));
        double x = simulation.weather(0.12, 0.12, 1, 0.234);
        assertEquals(0.41600000000000004, x);
        assertEquals(90 - 0 - 15, simulation.angleMadeWithSun());
        assertEquals("left", simulation.setSolarDirection("left"));
    }

    @Test
    void testWeather() {
        solar = new Monocrystaline();
        solar.setWeather("Summer");
        assertEquals(1.0, solar.getWeatherCondition());
        assertEquals("Summer", solar.getweather());
    }

    @Test
    void testWeatherForThinfilm() {
        solar = new Thinfilm();
        solar.setWeather("Autumn");
        assertEquals(0.5, solar.getWeatherCondition());
        assertEquals("Spring|Autumn", solar.getweather());
        solar.setWeather("Summer");
        assertEquals(1, solar.getWeatherCondition());
        assertEquals(0, solar.getSolarhours());
        assertEquals(0, solar.getSolarRadiation());
    }

    @Test
    void testArea() {
        solar = new Monocrystaline();
        solar.shadingArea(10);
        solar.setArea(21);
        assertEquals(11, solar.getArea());
    }

    @Test
    void testCostOfdifferentSolar() {
        finances = new Finance();
        solar = new Thinfilm();
        solarpanels.addSolarPanels(solar);
        solar.setAngle(30.0);
        solar.setSolarHours(4);
        solar.setWeather("Summer");
        solar.setArea(100); // in m^2
        solar.setSolarRadiationAverage(340); // average solar radiation received by Earth
        solar.shadingArea(0);
        solar.setEfficiency(0.23);
        solar.totalEnergy();
        double x = solarpanels.calculateTotalEnergy();
        assertEquals(27089.0, x);
        double y = finances.cost(solar);
        assertEquals(27089.0*1.25, y);
    }

    @Test
    void testPerc() {
        solar = new Perc();
        solarpanels.addSolarPanels(solar);
        solar.setAngle(30.0);
        solar.setSolarHours(4);
        solar.setWeather("Summer");
        solar.setArea(100); // in m^2
        solar.setSolarRadiationAverage(340); // average solar radiation received by Earth
        solar.shadingArea(0);
        solar.setEfficiency(0.23);
        assertEquals(1, solar.getWeatherCondition());
        solar = new Thinfilm();
        solarpanels.addSolarPanels(solar);
        solar.setAngle(60.0);
        solar.setSolarHours(5);
        solar.setWeather("Autumn");
        solar.setSolarRadiationAverage(340); // average solar radiation received by Earth
        solar.shadingArea(50);
        solar.setEfficiency(0.23);
        solar.setArea(150); // in m^2
        double x = solarpanels.calculateTotalEnergy();
        assertEquals(36864.0, x);
        assertEquals(0.5, solar.getWeatherCondition());
        finances = new Finance();
        double z = finances.cost(solarpanels.getSolarpanels().get(0));
        assertEquals(23025.649999999998, z);
        solar.setWeather("Winter");
        assertEquals(0, solar.getWeatherCondition());
        assertEquals("Winter", solar.getweather());
    }

    @Test
    void testWinterandSpringForPerc() {
        solar = new Perc();
        solar.setWeather("Winter");
        assertEquals(0, solar.getWeatherCondition());
        solar.setWeather("Spring");
        assertEquals(0.5, solar.getWeatherCondition());
    }

    @Test
    void testWinterandSpringForMonocrystalline() {
        solar = new Monocrystaline();
        solar.setWeather("Winter");
        assertEquals(0.2, solar.getWeatherCondition());
        assertEquals("Winter", solar.getweather());
        solar.setWeather("Spring");
        assertEquals(0.5, solar.getWeatherCondition());
    }

    @Test
    void testWinterandSummerForPollycrystalline() {
        solar = new Polycrystalline();
        solar.setWeather("Winter");
        assertEquals(0.2, solar.getWeatherCondition());
        solar.setWeather("Summer");
        assertEquals(1, solar.getWeatherCondition());
    }

    @Test
    void testSimulationWhenHours13() {
        SetSimulation simulation = new SetSimulation(13);
        String x = simulation.setSolarDirection("left");
        assertEquals("left", x);
    }

    @Test
    void simulationSunPosition() {
        SetSimulation simulation = new SetSimulation(11);
        simulation.setSolarDirection("left");
        assertEquals("right", simulation.setAngleAccordingToSun("right"));
    }

    @Test
    void testArea2() {
        solar = new Polycrystalline();
        solar.shadingArea(100);
        solar.setArea(120);
        assertEquals(20, solar.getArea());
        solar = new Monocrystaline();
        solar.shadingArea(100);
        solar.setArea(120);
        assertEquals(20, solar.getArea());
        solar = new Perc();
        solar.shadingArea(100);
        solar.setArea(120);
        assertEquals(20, solar.getArea());
        solar = new Thinfilm();
        solar.shadingArea(100);
        solar.setArea(120);
        assertEquals(20, solar.getArea());
    }

    @Test
    void testEfficiency() {
        solar = new Polycrystalline();
        solar.setEfficiency(100);
        assertEquals(100, solar.getEfficiency());
        solar = new Monocrystaline();
        solar.setEfficiency(100);
        assertEquals(100, solar.getEfficiency());
        solar = new Perc();
        solar.setEfficiency(100);
        assertEquals(100, solar.getEfficiency());
        solar = new Thinfilm();
        solar.setEfficiency(100);
        assertEquals(100, solar.getEfficiency());
    }

    @Test
    void testforThinfilm() {
        solar = new Thinfilm();
        solar.setWeather("Summer");
        assertEquals(1, solar.getWeatherCondition());
        solar.setWeather("Winter");
        assertEquals(0, solar.getWeatherCondition());
        solar.setWeather("Spring");
        assertEquals(0.5, solar.getWeatherCondition());
    }

    @Test
    void testForCostForPerc() {
        finances = new Finance();
        solar = new Perc();
        solarpanels.addSolarPanels(solar);
        solar.setAngle(30.0);
        solar.setSolarHours(4);
        solar.setWeather("Summer");
        solar.setArea(100); // in m^2
        solar.setSolarRadiationAverage(340); // average solar radiation received by Earth
        solar.shadingArea(0);
        solar.setEfficiency(0.23);
        solar.totalEnergy();
        double x = solarpanels.calculateTotalEnergy();
        assertEquals(27089.0, x);
        double y = finances.cost(solar);
        assertEquals(23025.649999999998, y);
    }

    @Test
    void testForPollycrystalline() {
        finances = new Finance();
        solar = new Polycrystalline();
        solarpanels.addSolarPanels(solar);
        solar.setAngle(30.0);
        solar.setSolarHours(4);
        solar.setWeather("Summer");
        solar.setArea(100); // in m^2
        solar.setSolarRadiationAverage(340); // average solar radiation received by Earth
        solar.shadingArea(0);
        solar.setEfficiency(0.23);
        solar.totalEnergy();
        double x = solarpanels.calculateTotalEnergy();
        assertEquals(27089.0, x);
        double y = finances.cost(solar);
        assertEquals(25734.55, y);
    }

    @Test
    void testsunPosition() {
        SetSimulation simulation = new SetSimulation(11);
        simulation.setSolarDirection("left");
        assertEquals("left", simulation.setAngleAccordingToSun("left"));
        assertEquals("right", simulation.setAngleAccordingToSun("right"));

        simulation.setSolarDirection("right");
        assertEquals("right", simulation.setAngleAccordingToSun("right"));
        assertEquals("left", simulation.setAngleAccordingToSun("left"));

        simulation.setSolarDirection("middle");
        assertEquals("middle", simulation.setAngleAccordingToSun("right"));
        assertEquals("middle", simulation.setAngleAccordingToSun("middle"));

        simulation.setSolarDirection("right");
        assertEquals("right", simulation.setAngleAccordingToSun("middle"));

        simulation.setSolarDirection("middle");
        assertEquals("middle", simulation.setAngleAccordingToSun("left"));

        simulation.setSolarDirection("left");
        assertEquals("left", simulation.setAngleAccordingToSun("middle"));
    }

    @Test
    void testForWeather() {
        solar = new Polycrystalline();
        solar.setWeather("Summer");
        assertEquals("Summer", solar.getweather());
        solar.setWeather("Spring");
        assertEquals("Spring|Autumn", solar.getweather());
        solar.setWeather("Winter");
        assertEquals("Winter", solar.getweather());

        solar = new Thinfilm();
        solar.setWeather("Summer");
        assertEquals("Summer", solar.getweather());
        solar.setWeather("Spring");
        assertEquals("Spring|Autumn", solar.getweather());
        solar.setWeather("Winter");
        assertEquals("Winter", solar.getweather());

        solar = new Monocrystaline();
        solar.setWeather("Summer");
        assertEquals("Summer", solar.getweather());
        solar.setWeather("Spring");
        assertEquals("Spring|Autumn", solar.getweather());
        solar.setWeather("Winter");
        assertEquals("Winter", solar.getweather());

        solar = new Perc();
        solar.setWeather("Summer");
        assertEquals("Summer", solar.getweather());
        solar.setWeather("Spring");
        assertEquals("Spring|Autumn", solar.getweather());
        solar.setWeather("Winter");
        assertEquals("Winter", solar.getweather());
        solar.shadingArea(23);
        assertEquals(23, solar.getShadingarea());
        solar.setAngle(12);
        assertEquals(12, solar.getAngle());
    }
}
