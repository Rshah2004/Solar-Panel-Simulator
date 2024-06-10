package ui;

import model.Finance;
import model.SolarPanelType;
import model.SolarPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// represents finance ui

public class FinanceGUI {
    private ArrayList<Double> costs;
    private ArrayList<SolarPanelType> types;
    private SolarPanels sp;

    private Finance finance;
    double sum = 0;

    public FinanceGUI(SolarPanels sp, SolarPanelUI si) {
        this.sp = sp;
        finance = new Finance();
        costs = new ArrayList<>();
        types = new ArrayList<>();

    }

    // modifes : this,
    // effects : adds the main finance panel
    public JPanel addFinance() {
        JPanel jpanel = new JPanel();
        jpanel.setBounds(50, 50, 200, 100); // Set bounds (x, y, width, height)
        JLabel jlabel2 = new JLabel();

        JTextField jtext = new JTextField(15);
        jtext.setBounds(20, 20, 10, 10);
        JLabel jlabel0 = new JLabel("What is the total power consumed at your house");
        JButton jbutton = new JButton("Enter total power");
        JLabel jlabel = new JLabel();
        jbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jbuttonAction(jlabel, jtext, jlabel2);
            }
        });
        jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
        jpanel.add(jlabel0);
        jpanel.add(Box.createVerticalStrut(2)); // Add some vertical spacing
        jpanel.add(jtext);
        jpanel.add(jbutton);
        jpanel.add(Box.createVerticalStrut(2)); // Add some vertical spacing
        jpanel.add(jlabel);
        jpanel.add(jlabel2);
        return jpanel;
    }

    // modifies : this,
    // effects  : adds action to a button
    private void jbuttonAction(JLabel jlabel, JTextField jtext, JLabel jlabel2) {
        String s1 = "Total cost to install ";
        jlabel.setText(s1 + sp.getSolarpanels().size() + " is going to be " + String.valueOf(produceSum()));
        double profit = finance.profit(Double.parseDouble(jtext.getText()), sum);
        if (profit > 0) {
            jlabel2.setText("You will be making a profit of " + profit + " dollars by installing this solar panel");
        } else {
            jlabel2.setText("You will be making a loss of " + profit + " dollar by installing this solar panel");
        }
    }

    // effects : returns sum
    private double produceSum() {
        sum = 0;
        for (int k = 0; k < sp.getSolarpanels().size(); k++) {
            sum += finance.cost(sp.getSolarpanels().get(k));
        }
        return sum;
    }
}