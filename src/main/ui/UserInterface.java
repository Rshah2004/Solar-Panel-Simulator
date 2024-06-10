package ui;


import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Scanner;
import java.time.LocalTime;

// represents the output allowing users to perform various functions
public class UserInterface {

    LocalTime currentTime;
    private static final String JSON_STORE = "./data/solarPanels.json";

    private Scanner input;
    private SolarPanelType solarType;
    private SolarPanels solarPanls;
    private Finance finance;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public UserInterface() {
        System.out.println("Hey user in order to choose an option from Menu selct the number it belongs to");
        input = new Scanner(System.in);
        solarPanls = new SolarPanels();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runSolar();
    }

    // effects : runs until q is pressed
    public void runSolar() {
        boolean keepGoing = true;
        String command = null;
        input = new Scanner(System.in);
        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processComand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    private void displayMenu() {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("|                         q -> quit                                                     |");
        System.out.println("|                         0. Are you interested in a simulation of the entire day?      |");
        System.out.println("|                         1.  to add solarPanels                                        |");
        System.out.println("|                         2.  to calculate total energy                                 |");
        System.out.println("|                         3.  to edit a Solar Panel                                     |");
        System.out.println("|                         4.  to get a Solar Panel                                      |");
        System.out.println("|                         5.  to remove a Solar Panel                                   |");
        System.out.println("|                         6.  to view the finances                                      |");
        System.out.println("|                         7.  to save the solar panel                                   |");
        System.out.println("|                         8.  to load the solar panel                                   |");
        System.out.println("-----------------------------------------------------------------------------------------");

    }

    // effects : performs differenct functions based on the input of th euser
    private void processComand(String command) {
        if (command.equals("1")) {
            addSolarPanel();
        } else if (command.equals("2")) {
            calculateTotalEnergy();
        } else if (command.equals("3")) {
            editSolarPanel();
        } else if (command.equals("4")) {
            getaSolarPanel();
        } else if (command.equals("5")) {
            removeaSolarPanel();
        } else if (command.equals("0")) {
            runStimulation();
        } else if (command.equals("6")) {
            viewFinances();
        } else if (command.equals("7")) {
            saveSolarPanel();
        } else if (command.equals("8")) {
            loadSolarPanel();
        } else {
            System.out.println("Invalid input");
        }
    }

    // effects : returns the total finance, that is the cost along with the profit.
    private void viewFinances() {
        System.out.println("Enter the power consumption");
        double power = input.nextDouble();
        finance = new Finance();
        double sum = 0;
        for (int k = 0; k < solarPanls.getSolarpanels().size(); k++) {
            sum += finance.cost(solarPanls.getSolarpanels().get(k));
        }
        System.out.println("Your total cost would be " + sum);
        double profit = finance.profit(power, sum);
        if (profit > 0) {
            System.out.println("You have made a profit of" + " " + profit);
        } else {
            System.out.println("You are at a loss of " + " " + profit);
        }
    }

    // modifies : this,
    // effects : adds the solar panel to a list of solar panels,
    private void addSolarPanel() {
        System.out.println("enter the number of solar panels u wanna add");
        int command = input.nextInt();
        for (int l = 0; l < command; l++) {
            displayMenu2();
            String command2 = input.next();
            if (command2.equals("1")) {
                solarType = new Polycrystalline();
            } else if (command2.equals("2")) {
                solarType = new Monocrystaline();
            } else if (command2.equals("3")) {
                solarType = new Thinfilm();
            } else if (command2.equals("4")) {
                solarType = new Perc();
            } else {
                System.out.println("Invalid input");
                runSolar();
            }
            setSpecifications(solarType);
            solarPanls.addSolarPanels(solarType);
        }
    }

    private void displayMenu2() {
        System.out.println("To add a solar panel specify its type from the list below");
        System.out.println("                                1.   Polycrystalline      ");
        System.out.println("                                2.   Monocrystalline      ");
        System.out.println("                                3.   Thinfilm             ");
        System.out.println("                                4.   Perc                 ");
    }

    private void setSpecifications(SolarPanelType solarType) {
        Double command = null;
        int command2 = 0;
        String command3 = null;
        System.out.println("Set Angle");
        command = input.nextDouble();
        solarType.setAngle(command);
        System.out.println("Set Solar Hours");
        command2 = input.nextInt();
        solarType.setSolarHours(command2);
        System.out.println("Set efficiency");
        double efficiency = input.nextDouble();
        solarType.setEfficiency(efficiency);
        System.out.println("Set Weather from the list");
        System.out.println("                                              Summer          ");
        System.out.println("                                              Winter          ");
        System.out.println("                                              Spring          ");
        System.out.println("                                              Autumn          ");
        command3 = input.next();
        solarType.setWeather(command3);
        inputTheRemaining();
    }

    private void inputTheRemaining() {
        Double command = null;
        System.out.println("Set Solar Radiation Average");
        command = input.nextDouble();
        solarType.setSolarRadiationAverage(command);
        System.out.println("Set Shading Area");
        command = input.nextDouble();
        solarType.shadingArea(command);
        System.out.println("Set Area");
        command = input.nextDouble();
        solarType.setArea(command);
        solarType.totalEnergy();
        System.out.println("The total energy is " +  solarType.gettotalEnergy());
    }

    private void calculateTotalEnergy() {
        System.out.println(solarPanls.calculateTotalEnergy());
    }

    // modifies : this,
    // effects : allows users to edit the solar panel by entering the index
    private void editSolarPanel() {
        System.out.println("Write the index of the solar panel u want to edit");
        int command = input.nextInt();
        System.out.println("Do you wanna edit the entire solar panel");
        String command2 = input.next();
        if (command2.equals("yes")) {
            System.out.println("To edit the solar panel write its new type");
            command2 = input.next();
            if (command2.equals("Polycrystalline")) {
                solarType = new Polycrystalline();
            } else if (command2.equals("Monocrystalline")) {
                solarType = new Monocrystaline();
            } else if (command2.equals("Thinfilm")) {
                solarType = new Thinfilm();
            } else if (command2.equals("Perc")) {
                solarType = new Perc();
            } else {
                System.out.println("Invalid input");
            }
            System.out.println(solarPanls.editSolarPanel(command, solarType));
        } else if (command2.equals("no")) {
            editTheRequired();
        }

    }

    // modifies : this,
    // Effects : edits the required index of the solar panel
    private void editTheRequired() {
        System.out.println("Enter the index of the solar panel in the list u wanna edi");
        int command1 = input.nextInt();
        displayEditOptions();
        String command3 = input.next();
        editTheRequired2(command1, command3);
    }

    // modifies : this,
    // effects : allows users to edit a specific property of a particular solar panel
    private void editTheRequired2(int command1, String command3) {
        if (command3.equals("1")) {
            System.out.println("Write the angle");
            double angle = input.nextDouble();
            solarPanls.getSolarpanels().get(command1).setAngle(angle);
        } else if (command3.equals("2")) {
            System.out.println("Write the solarhours");
            int solarhrs = input.nextInt();
            solarPanls.getSolarpanels().get(command1).setSolarHours(solarhrs);
        } else if (command3.equals("3")) {
            System.out.println("Write the new weather");
            String weather = input.next();
            solarPanls.getSolarpanels().get(command1).setWeather(weather);
        } else if (command3.equals("4")) {
            System.out.println("Write the radiationaverage");
            int radiationavg = input.nextInt();
            solarPanls.getSolarpanels().get(command1).setSolarRadiationAverage(radiationavg);
        } else if (command3.equals("5")) {
            System.out.println("Write the shading area");
            double sharea = input.nextDouble();
            solarPanls.getSolarpanels().get(command1).shadingArea(sharea);
        } else {
            System.out.println("Invalid input");
        }
    }

    private void displayEditOptions() {
        System.out.println("1. to edit the angle of the solarPanel selected");
        System.out.println("2. to edit the solar hours of the solarPanel selected");
        System.out.println("3. to edit the weather of the solarPanel selected");
        System.out.println("4. to edit the solar radiation average of the solarPanel selected");
        System.out.println("5. to edit the solar shading area of the of the solarPanel selected");
    }

    private void getaSolarPanel() {
        for (int z = 0; z < solarPanls.getSolarpanels().size(); z++) {
            System.out.println(solarPanls.getSolarpanels().get(z).getSolarType());
        }
    }

    // modifies : this,
    // effects : removes a solar panel from a list of solar panels
    private void removeaSolarPanel() {
        System.out.println("Enter the index number of the Solar panel u wanna remove");
        int edit = input.nextInt();
        solarPanls.removeSolarPanel(edit);
    }

    // modifies : this,
    // effects  : runs the simulation from 7 am to 3 pm
    private void runStimulation() {
        currentTime = LocalTime.of(7, 0, 0);  // Sets the time to 3:30 PM
        if (solarPanls.getSolarpanels().isEmpty()) {
            addSolarPanel();
        } else {
            System.out.println("You have selected to view the simulation from " + currentTime);
            System.out.println("Input the Solar Panel's direction, left if hour > 12, or else right");
            String command = input.next();
            double x = 0;
            double y = 0;
            double efficiency = 0;
            while (currentTime.getHour() >= 7 && currentTime.getHour() <= 15) {
                simulator(command, y, x, efficiency);
                System.out.println("Please wait for 5 seconds to see the simulation for the next hour");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                currentTime = currentTime.plusHours(1);
            }
        }
    }

    private void simulator(String command, double y, double x, double efficiency) {
        SetSimulation simulation = new SetSimulation(currentTime.getHour());
        simulation.setSolarDirection(command);
        simulation.setSolarDirection("right");
        y = simulation.angleMadeWithSun();
        System.out.println("Please enter the cloud cover at " + currentTime.getHour());
        double cloudcover = input.nextDouble();
        System.out.println("Please enter the dust Density ");
        double dustDensity = input.nextDouble();
        System.out.println("Please enter the precipitation ");
        double precipitation = input.nextDouble();
        for (int q = 0; q < solarPanls.getSolarpanels().size(); q++) {
            efficiency = solarPanls.getSolarpanels().get(q).getEfficiency();
        }
        x = simulation.weather(cloudcover, dustDensity, precipitation, efficiency);
        for (int q = 0; q < solarPanls.getSolarpanels().size(); q++) {
            solarPanls.getSolarpanels().get(q).setEfficiency(x);
            solarPanls.getSolarpanels().get(q).setAngle(y);
            solarPanls.getSolarpanels().get(q).setSolarHours(q + 1);
        }
        displaySimulation(currentTime.getHour());
        System.out.println("total energy:" + " " + solarPanls.calculateTotalEnergy());
    }

    private void displaySimulation(int hour) {
        if (hour >= 7 & hour < 12) {
            System.out.println("                O ");
            System.out.println("                 \\                            ");
            System.out.println("                   \\    ");
            System.out.println("                     \\          ");
            System.out.println("                        /             ");

        } else if (hour == 12) {
            System.out.println("                  O       ");
            System.out.println("                  |                            ");
            System.out.println("                  |    ");
            System.out.println("                  |       ");
            System.out.println("                  /             ");
        } else {
            System.out.println("              O            ");
            System.out.println("            //                            ");
            System.out.println("          //    ");
            System.out.println("        //       ");
            System.out.println("      \\             ");
        }
    }

    // modifies : this,
    // effects : saves the software
    private void saveSolarPanel() {
        try {
            jsonWriter.open();
            jsonWriter.write(solarPanls);
            jsonWriter.close();
            System.out.println("Saved " + solarPanls.getSolarpanels() + " to " + JSON_STORE);

        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // modifies : this
    // effects : loads the software
    private void loadSolarPanel() {
        try {
            solarPanls = jsonReader.read();
            System.out.println("Loaded " + solarPanls.getSolarpanels() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}

