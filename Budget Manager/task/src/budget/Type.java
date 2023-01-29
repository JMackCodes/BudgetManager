package budget;

public class Type {
    String name = "";
    String total = "";
    double numTotal = 0;

    public String getName() {
        return name;
    }

    public Type(String name, Double total) {
        this.name = name;
        this.total = total.toString();
    }

    public String getTotal() {
        return total;
    }

    public double getNumTotal() {
        numTotal = Double.parseDouble(total);
        return numTotal;
    }

    public void print() {
        System.out.printf(this.name +
                " - $%.2f\n", Double.parseDouble(this.total));
    }
}
