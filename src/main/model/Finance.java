package model;

// Represents the total cost, and calculates the profit made as well
public class Finance {
    private double cost;
    private double profit;

    // effects : initializes cost and profit to 0
    public Finance() {
        this.cost = 0;
        this.profit = 0;
    }

    // requires : a solar panel must be added
    // modifies : this,
    // effects :  sets the cost by multiplying the total energy, with energy per Watt
    public double cost(SolarPanelType s) {
        if (s.getSolarType().equals("Monocrystalline")) {
            this.cost = s.gettotalEnergy() * 1.25;
        } else if (s.getSolarType().equals("Polycrystalline")) {
            this.cost = s.gettotalEnergy() * 0.95;
        } else if (s.getSolarType().equals("Thinfilm")) {
            this.cost = s.gettotalEnergy() * 1.25;
        } else {
            this.cost = s.gettotalEnergy() * 0.85;
        }
        return cost;
    }

    // modifies : this,
    // effects  : multiplies the area's total power consumption with Canada's per watt amount and subtracts sum
    public double profit(double totalPowerConsumed, double sum) {
        profit = sum - (totalPowerConsumed * 192);
        return profit;
    }

}
