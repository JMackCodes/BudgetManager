package budget;

public class Entry {
    String name = "";
    String price = "";
    String type = "";
    double numPrice = 0;

    public Entry(String name, String price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public void print() {
        System.out.printf(this.name +
                " $%.2f\n", Double.parseDouble(this.price));
    }

    public String getName() {
        return name;
    }

    public double getNumPrice() {
        numPrice = Double.parseDouble(this.price);
        return numPrice;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }
}
