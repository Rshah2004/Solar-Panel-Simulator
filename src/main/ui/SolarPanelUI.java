package ui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import model.Event;

// represents the main solar panel ui

public class SolarPanelUI extends JFrame {
    private StartPage start = new StartPage();

    private JButton saveSolar;
    private BufferedImage backgroundImage2;
    private JLabel efficiencyLabel;
    private String selectedType;
    private JPanel jeditpanel;
    private double efficiencyNewValue;
    private BackgroundPanel2 backgroundPanel2;

    private double addAngleValue;
    private JLabel efficiencyLabel2;
    private JTextField areajbutton;
    private JTextField shadingButton;
    private JTextField solarHoursButton;
    private JTextField solarRadiationButton;

    private String setWeatherType;
    private SolarPanels sp;
    private JScrollBar scrollbar;
    private JScrollBar scrollbar2;
    private SolarPanelType spt;
    private DefaultListModel<String> myList = new DefaultListModel<>();
    private JList<String> list;
    private BufferedImage backgroundImage;

    private static final int RECT_X = 115;
    private static final int RECT_Y = RECT_X + 245;
    private static final int RECT_WIDTH = 5;
    private static final int RECT_HEIGHT = 100;

    private BackgroundPanel backgroundPanel;

    private JLabel jlabelsimulator;

    private Timer timer;

    private int xsun = 1400;
    private int ysun = 350;
    private int speed = -1;

    private LocalTime currentTime;

    private int selectedIndexForSimulation;

    private double angle;

    private int c1 = 0;
    private JPanel totalenergysimulationpanel;

    private FinanceGUI finance;


    // modifies: this,
    // effects : sets the background panel
    public SolarPanelUI(SolarPanels solarPanels) throws IOException {
        sp = solarPanels;
        finance = new FinanceGUI(sp, this);
        saveSolar = new JButton("SAVE");
        produceimage();
        list = new JList<>(myList);
        for (SolarPanelType spt : sp.getSolarpanels()) {
            myList.addElement(spt.getSolarType());
        }
        setTitle("Solar Panel 210 simulator");
        JTabbedPane tabbedPane = new JTabbedPane();
        backgroundPanel = new BackgroundPanel();
        simulationbuttonandAction(backgroundPanel);
        addTotabpane(tabbedPane);
        backgroundPanel.add(tabbedPane); // Add internal frame to the desktop
        backgroundPanel.setSize(500,500);
        setContentPane(backgroundPanel);
        last();
        setVisible(true);
    }


    // effects : produces image
    private void produceimage() {
        try {
            backgroundImage = ImageIO.read(new File("src/main/images/simulatormain.jpeg"));
        } catch (IOException e) {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
        }
    }

    // effects : sets the action when window closes
    private void last() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing the application");

            // Print all the events that have been logged since the application started
            for (Event e : EventLog.getInstance()) {
                System.out.println(e.toString());
            }

            // Add your cleanup or other necessary code here
        }));
    }

    // modifies : this
    // effects  : adds the tabs
    private void addTotabpane(JTabbedPane tabbedPane) {
        tabbedPane.add("Add Solar Panels", addButton());
        tabbedPane.add("Finance", finance.addFinance());
        tabbedPane.add("Edit Solar Panels", editButton());
    }

    // modifies : this
    // effects  : performes action when the simulation button is pressed
    private void simulationbuttonandAction(JPanel backgroundPanel) {
        JButton jbutton = new JButton("Click to show the simulation");
        backgroundPanel.add(jbutton);
        jbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulation();
            }
        });
    }

    // represents a private class to set the background
    private class BackgroundPanel extends JPanel {
        private double angle = 0.0; // Initial angle
        private boolean rotateSecondRectangle = false; // Flag to control rotation

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            ArrayList<Rectangle> rectangles = new ArrayList<>();
            producepain(g, rectangles, rotateSecondRectangle);
            if (rotateSecondRectangle) {
                int newRectX2 = (int) (72 * ((double) getWidth() / 500));
                int newRectY2 = (int) (220 * ((double) getWidth() / 500));
                int newRectHeight2 = (int) (5 * ((double) getWidth() / 500));
                int newRectWidth2 = (int) (100 * ((double) getWidth() / 500));
                Rectangle r2 = new Rectangle(newRectX2, newRectY2, newRectWidth2, newRectHeight2);
                rectangles.add(r2);
                Graphics2D g2d = (Graphics2D) g;
                g2d.rotate(Math.toRadians(angle), r2.x1 + r2.w1 / 2.0, r2.y1 + r2.z1 / 2.0);
                g.fillRect(r2.x1, r2.y1, r2.w1, r2.z1);
                g2d.rotate(-Math.toRadians(angle), r2.x1 + r2.w1 / 2.0, r2.y1 + r2.z1 / 2.0); // Reset rotation
            }
        }

        // modifies: this
        //  effects: Method to update the angle when the angle bar changes
        public void updateAngle(double newAngle) {
            angle = newAngle;
            rotateSecondRectangle = true; // Set the flag to enable rotation
            repaint(); // Trigger a repaint to update the displayed rectangle
        }
    }

    // modifies : this
    // effects: changes the angle of the solarpanel
    private void producepain(Graphics g, ArrayList<Rectangle> rectangles, boolean rotateSecondRectangle) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            int newRectX = (int) (RECT_X * ((double) getWidth() / 500));
            int newRectY = (int) (RECT_Y * ((double) getHeight() / 500));
            int newRectWidth = (int) (RECT_WIDTH * ((double) getWidth() / 500));
            int newRectHeight = (int) (RECT_HEIGHT * ((double) getHeight() / 500));
            g.setColor(Color.BLACK);
            Rectangle r1 = new Rectangle(newRectX,newRectY,newRectWidth,newRectHeight);
            rectangles.add(r1);
            g.fillRect(r1.x1, r1.y1, r1.w1, r1.z1);
        }
    }

    // A class to represent rectangles
    private class Rectangle {
        private int x1;
        private int y1;
        private int w1;
        private int z1;

        public Rectangle(int x, int y, int w, int h) {
            this.x1 = x;
            this.y1 = y;
            this.w1 = w;
            this.z1 = h;
        }
    }


    // modifies : this
    // effects  : creates a panel required to add the attributes for adding a solar panel
    private JPanel addButton() {
        addsaveSolar();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6,1));
        String[] languages = {"Pollycrystalline", "Monocrystalline", "Perc", "Thinfilm"};
        JComboBox boxOfTypes = new JComboBox();
        JLabel heading = new JLabel("Solar Type:  " + boxOfTypes.getSelectedItem());
        for (int i = 0; i < languages.length; i++) {
            boxOfTypes.addItem(languages[i]);
        }

        boxOfTypes.addActionListener(e -> {
            selectedType = (String) boxOfTypes.getSelectedItem();
            heading.setText("Solar Type:  " + selectedType);

            // Implement your logic based on the selectedType
        });
        return addpanels(panel, boxOfTypes, heading);
    }

    // effects : saves the solar panel
    private void addsaveSolar() {
        saveSolar.setBounds(70, 200, 300,300);
        saveSolar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start.save(sp);
                JOptionPane.showMessageDialog(null, "Data saved successfully!");
            }
        });
    }

    // modifies: this
    // effects : adds the attributes of a solar panel to the jpanl
    private JPanel addpanels(JPanel panel, JComboBox boxOfTypes, JLabel heading) {
        panel.add(boxOfTypes);
        panel.add(heading);
        panel.add(addEfficiency());
        panel.add(addAngle());
        panel.add(addArea());
        panel.add(shadingArea());
        panel.add(setSolarHours());
        panel.add(setSolarRadiation());
        panel.add(setWeather());
        panel.add(addTheMainButton());
        panel.add(saveSolar);
        return panel;
    }

    // modifies : this
    // effects : adds efficiency label nd button
    public JPanel addEfficiency() {
        JPanel panel = new JPanel();
        scrollbar = new JScrollBar(JScrollBar.HORIZONTAL);
        efficiencyLabel = new JLabel("Efficiency: " + scrollbar.getValue() + "%");

        // Add components to the frame
        panel.add(scrollbar);
        panel.add(efficiencyLabel);

        // Add a ChangeListener to the scrollbar
        scrollbar.addAdjustmentListener(e -> {
            // Update the label text when the slider value changes
            efficiencyNewValue = scrollbar.getValue();
            efficiencyLabel.setText("Efficiency: " + efficiencyNewValue + "%");
        });

        scrollbar.setBounds(50, 50, 100, 30);


        return panel;
    }


    // modifies : this
    // effects : adds angle label and button
    public JPanel addAngle() {
        JPanel panel = new JPanel();
        scrollbar2 = new JScrollBar(JScrollBar.HORIZONTAL);
        efficiencyLabel2 = new JLabel("Angle: " + scrollbar2.getValue());

        // Add components to the frame
        panel.add(scrollbar2);
        panel.add(efficiencyLabel2);

        // Add a ChangeListener to the scrollbar
        scrollbar2.addAdjustmentListener(e -> {
            // Update the label text when the slider value changes
            addAngleValue = scrollbar2.getValue();
            efficiencyLabel2.setText("Angle: " + addAngleValue + "Degrees");
            backgroundPanel.updateAngle(scrollbar2.getValue());
        });
        scrollbar2.setBounds(100, 50, 100, 30);

        return panel;
    }


    // modifies : this
    // effects : adds area label and button
    public JPanel addArea() {
        JPanel panel = new JPanel();
        areajbutton = new JTextField(7);
        JLabel areabutton = new JLabel("Set area");
        areajbutton.setBounds(10,50,200,20);
        areabutton.setBounds(20, 50, 200, 22);
        panel.add(areajbutton);
        panel.add(areabutton);
        return panel;
    }

    // modifies : this
    // effects  : adds shading area label and button
    public JPanel shadingArea() {
        JPanel panel = new JPanel();
        shadingButton = new JTextField(7);
        shadingButton.setBounds(100,100,150,20);
        JLabel shade = new JLabel("Set Shading area");
        shade.setBounds(50,100,100,20);
        panel.add(shadingButton);
        panel.add(shade);
        return panel;
    }

    // modifies : this
    // effects  : adds solar hours label and button
    public JPanel setSolarHours() {
        JPanel panel = new JPanel();
        solarHoursButton = new JTextField(7);
        solarHoursButton.setBounds(100,50,150,20);
        JLabel solarhours = new JLabel("Set Solar Hours");
        solarhours.setBounds(50,100,132,22);
        panel.add(solarHoursButton);
        panel.add(solarhours);
        return panel;
    }

    // modifies : this
    // effects  : adds weather label and button
    public JPanel setWeather() {
        JPanel panel = new JPanel();
        String[] languages = {"Summer", "Winter", "Autumn", "Spring"};
        JTextField heading = new JTextField("Weather");
        JComboBox boxOfTypes = new JComboBox();
        for (int i = 0; i < languages.length; i++) {
            boxOfTypes.addItem(languages[i]);
        }
        heading.setEditable(false);

        boxOfTypes.addActionListener(e -> {
            setWeatherType = (String) boxOfTypes.getSelectedItem();
            // Implement your logic based on the selectedType
        });
        panel.add(heading);
        panel.add(boxOfTypes);
        return panel;
    }

    // modifies : this
    // effects : adds solar radiation label nd button
    public JPanel setSolarRadiation() {
        JPanel panel = new JPanel();
        solarRadiationButton = new JTextField(7);
        JLabel solarradiation = new JLabel("Set Solar Radiation");
        solarRadiationButton.setBounds(100,182,132,22);
        panel.add(solarRadiationButton);
        panel.add(solarradiation);
        return panel;
    }

    // modifies : this
    // effects  : adds the "Add solar panel" button to the jpanel
    private JButton addTheMainButton() {
        JButton button = new JButton("Add the solar Panel");
        button.setBounds(50,200,150,150);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newSolarPanel();
                sp.addSolarPanels(spt);
                myList.addElement(selectedType);
                JOptionPane.showMessageDialog(null, "Data added successfully!");
            }
        });
        return button;
    }

    // requires : the button to be pressed
    // effects  : creates a new solar panel
    private void newSolarPanel() {
        if (selectedType == "Monocrystalline") {
            spt = new Monocrystaline();
        } else if (selectedType == "Pollycrystalline") {
            spt = new Polycrystalline();
        } else if (selectedType == "Thinfilm") {
            spt = new Thinfilm();
        } else {
            spt = new Perc();
        }
        setEfficiency(spt);
        setweatherSpt(spt);
        addAngleValuetoSpt(spt);
        addshadingArea(spt);
        addAreaToSpt(spt);
        addSolarRadiation(spt);
        setSolarHoursTospt(spt);
    }

    // modifies : this
    // effects  : sets efficiency
    private void setEfficiency(SolarPanelType spt) {
        spt.setEfficiency(efficiencyNewValue);
    }

    // modifies : this
    // effects  : sets weather
    private void setweatherSpt(SolarPanelType spt) {
        spt.setWeather(setWeatherType);
    }

    // modifies : this
    // effects  : sets angle
    private void addAngleValuetoSpt(SolarPanelType spt) {
        spt.setAngle(addAngleValue);
    }

    // modifies : this
    // effects  : sets shading area
    private void addshadingArea(SolarPanelType spt) {
        spt.shadingArea(Double.parseDouble(shadingButton.getText()));
    }

    // modifies : this
    // effects  : sets area
    private void addAreaToSpt(SolarPanelType spt) {
        spt.setArea(Double.parseDouble(areajbutton.getText()));
    }

    // modifies : this
    // effects  : sets solar radiation
    private void addSolarRadiation(SolarPanelType spt) {
        spt.setSolarRadiationAverage(Double.parseDouble(solarRadiationButton.getText()));
    }

    // modifies : this
    // effects  : sets solar hours
    private void setSolarHoursTospt(SolarPanelType spt) {
        spt.setSolarHours(Integer.parseInt(solarHoursButton.getText()));
    }

    // modifies : this
    // effects  : creates the tab panel for the edit
    private JPanel editButton() {
        jeditpanel = new JPanel();
        EditTabUI edit = new EditTabUI(backgroundPanel, jeditpanel, myList, list);
        return edit.editButton(sp);
    }

    // modifies : this
    // effects :  creates a new panel and an internal frame for simulation
    private void simulation() {
        totalenergysimulationpanel = new JPanel();
        totalenergysimulationpanel.setBounds(550, 750, 500,600);
        JInternalFrame editing = new JInternalFrame("check",true,true,true,true);
        JButton editbutton = new JButton("Select solar panel");
        JPanel jpanel = new JPanel();
        editing.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                editing.setVisible(false);
                jpanel.setVisible(false);
                list.setBounds(jeditpanel.getX() + 4, jeditpanel.getY() + 1, 10, 10);
                jeditpanel.add(list);
                setContentPane(backgroundPanel);
            }
        });
        editbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionUponClickingEdit(editing);
            }
        });
        pathtoBackgroundimage2();
        addContainersToediting(jpanel,editing,editbutton);
    }

    private void pathtoBackgroundimage2() {
        try {
            backgroundImage2 = ImageIO.read(new File("src/main/images/simulatorpic.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // modifies : this
    // effects : if the click to show simulation is pressed then the simulation starts
    private void actionUponClickingEdit(JInternalFrame editing) {
        selectedIndexForSimulation = list.getSelectedIndex();
        angle = sp.getSolarpanels().get(selectedIndexForSimulation).getAngle();
        editing.setVisible(false);
        mainSimulationRunner(selectedIndexForSimulation);
        backgroundPanel2 = new BackgroundPanel2(selectedIndexForSimulation);
        backgroundPanel2.setLayout(null); // You can adjust the layout manager based on your requirements
        backgroundPanel2.setVisible(true);
        backgroundPanel2.setBounds(0, 0, getWidth(), getHeight());
        setContentPane(backgroundPanel2);
        JButton jbutton = new JButton("BACK");
        backgroundPanel2.add(jbutton);
        jbutton.setBounds(20, 750,120,120);
        jbutton.setBackground(Color.black);
        jbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonAction();
            }
        });
    }

    // modifies : this
    // effects : creates the back buttton with its function
    private void backButtonAction() {
        backgroundPanel.setVisible(true);
        list.setBounds(jeditpanel.getX() + 4, jeditpanel.getY() + 1, 10, 10);
        jeditpanel.add(list);
        setContentPane(backgroundPanel);
        backgroundPanel2.setVisible(false);
        xsun = 1400;
        ysun = 350;
        speed = -1;
        backgroundPanel2.repaint();
        sp.getSolarpanels().get(selectedIndexForSimulation).setAngle(angle);
        timer.stop();
    }

    // modifies :this
    // effects : adds the containers to the editing tab
    private void addContainersToediting(JPanel jpanel, JInternalFrame editing, JButton editbutton) {
        list.setBounds(200,200,500,500);
        jpanel.add(list);
        editing.add(editbutton);
        editbutton.setBounds(100, 100, 120, 40);
        editing.add(jpanel);
        editing.setSize(500, 400); // Set the size of the editing frame
        editing.setLocation(200, 200); // Set the location of the editing frame
        editing.setVisible(true);
        setContentPane(editing);
    }

    // effects : starts the timer that runs from 7 am in the morning to 4 pm in the afternoon
    private JPanel mainSimulationRunner(int i) {
        currentTime = LocalTime.of(7, 0, 0);  // Sets the time to 7:00 AM
        jlabelsimulator = new JLabel();
        timer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerAction(i);
            }
        });
        sp.getSolarpanels().get(i).setAngle(angle);
        // Start the Timer
        timer.setInitialDelay(0);
        timer.start();

        return backgroundPanel2;
    }

    // effects : stops the timer if the time is 4pm
    private void timerAction(int i) {
        jlabelsimulator.setText("You are seeing the simulation for " + currentTime);

        // Stop the Timer when the time is greater than 3 PM
        if (currentTime.getHour() > 15) {
            timer.stop();
        }
        changePositionOfTheSun();
        backgroundPanel2.add(jlabelsimulator);
        if (currentTime.getMinute() == 56) {
            currentTime =  LocalTime.of(currentTime.getHour() + 1, 0, 0);
            findTotalEnergyforsimulation(i);
        } else if (currentTime.getSecond() == 52) {
            currentTime =  LocalTime.of(currentTime.getHour(), currentTime.getMinute() + 4, 0);
        } else {
            currentTime = LocalTime.of(currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond() + 13);
        }
    }

    // modifies : this
    // effects  : shows total energy produced at each hour
    private void findTotalEnergyforsimulation(int i) {
        totalenergysimulationpanel.setLayout(new FlowLayout());  // Use an appropriate layout manager
        totalenergysimulationpanel.setBounds(0, 0, 400, 220);
        SolarPanelType s = sp.getSolarpanels().get(i);
        s.setAngle(checkAngleWithSun(angle));
        String s1 = "THE TOTAL ENERGY AT " +  currentTime.getHour();
        JLabel jlabel = new JLabel(s1 + " for " + s.getSolarType() + " is " + s.gettotalEnergy());
        jlabel.setBounds(0,  c1, 50, 50);
        c1 += 20;
        totalenergysimulationpanel.add(jlabel);
        backgroundPanel2.add(totalenergysimulationpanel);
    }

    // effects : returns the angle made with the Sun
    private double checkAngleWithSun(double angle) {
        return 90 - Math.abs((15 * (currentTime.getHour() - 12))) - angle;
    }

    // effects : changes the position of the Sun with respect to the time
    private void changePositionOfTheSun() {
        if (currentTime.getHour() >= 7 && currentTime.getHour() <= 11) {
            xsun -= 2;
            ysun = ysun + speed;
        } else if (currentTime.getHour() > 11 && currentTime.getHour() <= 12) {
            xsun -= 2;
            ysun = 0;
        } else {
            speed = 2;
            xsun -= 3;
            ysun = ysun + speed;
        }

        System.out.println("xsun: " + xsun + ", ysun: " + ysun);
        backgroundPanel2.repaint();
    }

    // represents a class that sets the background for the simualtion
    private class BackgroundPanel2 extends JPanel {
        private int index;

        private BackgroundPanel2(int i) {
            this.index = i;
        }

        // modifies : this
        // effects  : changes the background
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage2 != null) {
                g.drawImage(backgroundImage2, 0, 0, getWidth(), getHeight(), this);
                g.setColor(Color.ORANGE);
                g.fillOval(xsun, ysun, 175, 175);
                g.setColor(Color.black);
                drawAnotherRectangle(g);
            }
        }
    }

    // modifies : this,
    // effects : sets the angle for the simulation according to the angle set by the user earlier
    private void drawAnotherRectangle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        double angleInRadians = Math.toRadians(angle);
        AffineTransform transform = new AffineTransform();
        int newRectX2 = (int) (200 * ((double) getWidth() / 500));
        int newRectY2 = (int) (150 * ((double) getWidth() / 500));
        int newRectHeight2 = (int) (5 * ((double) getWidth() / 500));
        int newRectWidth2 = (int) (100 * ((double) getWidth() / 500));
        Rectangle rectangle = new Rectangle(newRectX2, newRectY2, newRectWidth2, newRectHeight2);
        g2d.setTransform(originalTransform);
        Double ang = angleInRadians;
        transform.rotate(ang,rectangle.x1 + rectangle.w1 / 2.0, rectangle.y1 + rectangle.z1 / 2.0);
        g2d.transform(transform);
        g2d.fillRect(rectangle.x1, rectangle.y1, rectangle.w1, rectangle.z1);
        g2d.setTransform(originalTransform);
    }
}