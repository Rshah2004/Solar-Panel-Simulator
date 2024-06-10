package ui;

import model.SolarPanelType;
import model.SolarPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represents the edit ui

public class EditTabUI {
    private JPanel jeditpanel;
    private DefaultListModel<String> myList = new DefaultListModel<>();
    private JList<String> list;
    private String setWeatherType;
    private JPanel backgroundpanel;

    private SolarPanels sp;

    // effects : sets the panel, background and list
    public EditTabUI(JPanel bg, JPanel jed, DefaultListModel<String> myList, JList<String> list) {
        this.jeditpanel = jed;
        this.myList = myList;
        this.list = list;
        this.backgroundpanel = bg;
    }

    // modifies : this,
    // effects : adds the list of solar panels to the panel and sets the visibility to true
    public JPanel editButton(SolarPanels sp) {
        list.setBounds(jeditpanel.getX() + 4, jeditpanel.getY() + 1, 10, 10);
        jeditpanel.add(list);
        jeditpanel.add(addeditButton(sp));
        jeditpanel.setVisible(true);
        return jeditpanel;
    }

    // modifies : this
    // effects  : adds the edit button to the panel
    private JPanel addeditButton(SolarPanels sp) {
        JPanel editing = new JPanel();
        editing.setVisible(true);
        JButton editbutton = new JButton("Edit this solar panel");
        editbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = list.getSelectedIndex();
                editTheoptions(selectedIndex, sp); // Add to the content pane of 'editing'
                editing.revalidate();
                editing.setSize(400, 300); // Set the size of the editing frame
                editing.setLocation(100, 100); // Set the location of the editing frame
                editing.repaint();
            }
        });
        editbutton.setBounds(30, 30, 20, 30);
        editing.add(editbutton);
        return editing;
    }

    // modifies : this,
    // effects : adds a list of buttons for editing each attribute of the solar panel
    private void editTheoptions(int selectedIndex, SolarPanels sp) {
        JInternalFrame jpanel = new JInternalFrame("", true, true, true, true);
        jpanel.setLayout(new GridLayout(6, 2));
        jpanel.add(efficiencypanel(selectedIndex, sp));
        jpanel.add(editangle(selectedIndex, sp));
        jpanel.add(editsolarhours(selectedIndex, sp));
        jpanel.add(editSolarRadiation(selectedIndex, sp));
        jpanel.add(editWeather(selectedIndex, sp));
        jpanel.add(editsetArea(selectedIndex, sp));
        jpanel.add(editShadingArea(selectedIndex, sp));
        this.backgroundpanel.add(jpanel);
        jpanel.setVisible(true);
    }

    // modifies : this,
    // effects : changes efficiency
    private JPanel efficiencypanel(int si, SolarPanels sp) {
        JPanel efficiencypanel = new JPanel();
        JTextField efficiencylabel = new JTextField(String.valueOf(sp.getSolarpanels().get(si).getEfficiency()));
        JButton changeEfficiency = new JButton("Change efficiency");
        changeEfficiency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SolarPanelType sppt = sp.getSolarpanels().get(si);
                sppt.setEfficiency(Double.parseDouble(efficiencylabel.getText()));
                JOptionPane.showMessageDialog(null, "Efficiency changed successfully!");
                sp.editSolarPanel(si, sppt);
            }
        });
        efficiencypanel.add(efficiencylabel);
        efficiencypanel.add(changeEfficiency);
        return efficiencypanel;
    }

    // modifies : this,
    // effects : changes angle
    private JPanel editangle(int selectedIndex, SolarPanels sp) {
        JPanel anglepanel = new JPanel();
        JTextField anglelabel = new JTextField(String.valueOf(sp.getSolarpanels().get(selectedIndex).getAngle()));
        JButton changeEfficiency = new JButton("Change Angle");
        changeEfficiency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SolarPanelType sppt = sp.getSolarpanels().get(selectedIndex);
                sppt.setAngle(Double.parseDouble(anglelabel.getText()));
                JOptionPane.showMessageDialog(null, "Angle changed successfully!");
                sp.editSolarPanel(selectedIndex, sppt);
            }
        });
        anglepanel.add(anglelabel);
        anglepanel.add(changeEfficiency);
        return anglepanel;
    }

    // modifies : this,
    // effects : changes solar hours
    private JPanel editsolarhours(int selectedindex, SolarPanels sp) {
        JPanel solarhours = new JPanel();
        int solarhourslabel = (sp.getSolarpanels().get(selectedindex).getSolarhours());
        JTextField solarlabel = new JTextField(String.valueOf(solarhourslabel));
        JButton solarbutton = new JButton("Change solar hours");
        solarbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SolarPanelType sppt = sp.getSolarpanels().get(selectedindex);
                sppt.setSolarHours(Integer.parseInt(solarlabel.getText()));
                JOptionPane.showMessageDialog(null, "Solar hours changed successfully!");
                sp.editSolarPanel(selectedindex, sppt);
            }
        });
        solarhours.add(solarlabel);
        solarhours.add(solarbutton);
        return solarhours;
    }

    // modifies : this,
    // effects : changes solar radiation
    private JPanel editSolarRadiation(int selectedindex, SolarPanels sp) {
        JPanel solarRadiationPanel = new JPanel();
        double solarRadiationlabel = (sp.getSolarpanels().get(selectedindex).getSolarRadiation());
        JTextField solarRadiationtext = new JTextField(String.valueOf(solarRadiationlabel));
        JButton solarRadiation = new JButton("Change solar radiation");
        solarRadiation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SolarPanelType sppt = sp.getSolarpanels().get(selectedindex);
                sppt.setSolarRadiationAverage(Double.parseDouble(solarRadiationtext.getText()));
                JOptionPane.showMessageDialog(null, "Solar radiation changed successfully!");
                sp.editSolarPanel(selectedindex, sppt);
            }
        });
        solarRadiationPanel.add(solarRadiationtext);
        solarRadiationPanel.add(solarRadiation);
        return solarRadiationPanel;
    }

    // modifies : this,
    // effects : changes weather
    private JPanel editWeather(int selectedIndex, SolarPanels sp) {
        JPanel setWeatherpanel = new JPanel();
        JLabel s = new JLabel(sp.getSolarpanels().get(selectedIndex).getweather());
        String[] languages = {"Summer", "Winter", "Autumn", "Spring"};
        JTextField heading = new JTextField("Weather");
        JComboBox boxOfTypes = new JComboBox();
        JButton weather = new JButton("Change weather");
        for (int i = 0; i < languages.length; i++) {
            boxOfTypes.addItem(languages[i]);
        }
        heading.setEditable(false);
        boxOfTypes.addActionListener(e -> {
            setWeatherType = (String) boxOfTypes.getSelectedItem();
        });
        weather.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                weatherAction(sp, selectedIndex);
            }
        });
        setWeatherpanel.add(s);
        setWeatherpanel.add(boxOfTypes);
        setWeatherpanel.add(weather);
        return setWeatherpanel;
    }

    private void weatherAction(SolarPanels sp, int selectedIndex) {
        SolarPanelType sppt = sp.getSolarpanels().get(selectedIndex);
        sppt.setWeather(setWeatherType);
        sp.editSolarPanel(selectedIndex, sppt);
        JOptionPane.showMessageDialog(null, "Weather changed successfully!");
    }

    // modifies : this,
    // effects  : edits area
    private JPanel editsetArea(int selectedIndex, SolarPanels sp) {
        JPanel setAreaPanel = new JPanel();
        JTextField setAreaLabel = new JTextField(String.valueOf(sp.getSolarpanels().get(selectedIndex).getArea()));
        JButton solarbutton = new JButton("Change area");
        solarbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SolarPanelType sppt = sp.getSolarpanels().get(selectedIndex);
                sppt.setArea(Double.parseDouble(setAreaLabel.getText()));
                JOptionPane.showMessageDialog(null, "Area changed successfully!");
                sp.editSolarPanel(selectedIndex, sppt);
            }
        });
        setAreaPanel.add(setAreaLabel);
        setAreaPanel.add(solarbutton);
        return setAreaPanel;
    }

    // modifies : this,
    // effects  : edits shading area
    private JPanel editShadingArea(int si, SolarPanels sp) {
        JPanel setShadingAreaPanel = new JPanel();
        JTextField setShdaingAreaLabel = new JTextField(String.valueOf(sp.getSolarpanels().get(si).getShadingarea()));
        JButton setShadingarea = new JButton("Change shading area");
        setShadingarea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SolarPanelType sppt = sp.getSolarpanels().get(si);
                sppt.shadingArea(Double.parseDouble(setShdaingAreaLabel.getText()));
                JOptionPane.showMessageDialog(null, "Shading Area changed successfully!");
                sp.editSolarPanel(si, sppt);
            }
        });
        setShadingAreaPanel.add(setShdaingAreaLabel);
        setShadingAreaPanel.add(setShadingarea);
        return setShadingAreaPanel;
    }
}
