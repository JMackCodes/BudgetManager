package budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Budget myBudget = new Budget();
        myBudget.printMenu();
    }
}

class Budget {//maybe just roll budget into this instead of having it be a different class
    private ArrayList<String[]> entries = new ArrayList<>();
    private double income = 0;
    private double total = 0;
    private double subTotalFood = 0;
    private double subTotalClothes = 0;
    private double subTotalEntertainment = 0;
    private double subTotalOther = 0;

    private double balance = income - total;

    public void printMenu() {
        System.out.println("Choose your action:");
        System.out.println("1) Add Income");
        System.out.println("2) Add Purchase");
        System.out.println("3) Show the list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("7) Analyze (Sort)");
        System.out.println("0) Exit");
        selectItem();
    }

    void selectItem() {
        Scanner in = new Scanner(System.in);
        int selection = in.nextInt();
        System.out.println();
        boolean validSelection = false;
        while (!validSelection) {
            if (selection == 1) {
                validSelection = true;
                System.out.println("Enter income:");
                addIncome(in.nextDouble());
            } else if (selection == 2) {
                validSelection = true;
                addPurchase();
            } else if (selection == 3) {
                showPurchases();
            } else if (selection == 4) {
                balance = income - total;
                System.out.printf("Balance: $%.2f\n", this.balance);
                System.out.println();
                printMenu();
            } else if (selection == 5) {
                validSelection = true;
                //save the entries arraylist's contents to a file
                File file = new File("purchases.txt");
                try {
                    PrintWriter printWriter = new PrintWriter(file);
                    printWriter.println(this.total);
                    printWriter.println(this.income);
                    for (String[] e : entries) {
                        printWriter.println(Arrays.deepToString(e));
                    }
                    printWriter.flush();
                    printWriter.close();
                    System.out.println("Purchases were saved!");
                    System.out.println();
                    printMenu();


                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }


            }else if (selection == 6) {
                validSelection = true;
                File file = new File("purchases.txt");
                try {
                    Scanner fileIn = new Scanner(file);
                    this.total = Double.parseDouble(fileIn.nextLine());
                    this.income = Double.parseDouble(fileIn.nextLine());
                    StringBuilder temp;
                    String[] tempEntry = new String[3];
                    int commaLocation;

                    while (fileIn.hasNext()) {
                        temp = new StringBuilder(fileIn.nextLine());
                        temp.deleteCharAt(temp.indexOf("["));
                        temp.deleteCharAt(temp.indexOf("]"));

                        commaLocation = temp.indexOf(",");
                        tempEntry[0] = temp.substring(0, commaLocation).trim();
                        temp.delete(0, commaLocation + 1);

                        commaLocation = temp.indexOf(",");
                        tempEntry[1] = temp.substring(0, commaLocation).trim();
                        temp.delete(0, commaLocation + 1);

                        commaLocation = temp.indexOf(",");
                        tempEntry[2] = temp.substring(0, temp.length()).trim();
                        temp.delete(0, commaLocation + 1);

                        this.entries.add(tempEntry);
                        tempEntry = new String[3];
                    }
                    fileIn.close();
                    System.out.println("Purchases were loaded!");
                    System.out.println();
                    totalPurchases();
                    this.balance = this.income - this.total;
                    printMenu();

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (selection == 7) {
                totalPurchases();
                analyze();


            } else if (selection == 0) {
                System.out.println("Bye!");
                validSelection = true;
                System.exit(0);
            }
        }
    }

    void analyze() {
        System.out.println("How do you want to sort?");
        System.out.println("1) Sort all purchases");
        System.out.println("2) Sort by type");
        System.out.println("3) Sort certain type");
        System.out.println("4) Back");

        Scanner in = new Scanner(System.in);
        int selection = Integer.parseInt(in.nextLine());
        System.out.println();

        //fuck the easiest way of doing this would probably be making each entry a class object
        // and then sorting those class objects by their entry.cost field.
        switch (selection) {
            case 1:
                System.out.println("All:");
                if (entries.isEmpty()) {
                    System.out.println("The purchase list is empty!");
                    System.out.println();
                } else {
                    ArrayList<Entry> tempTest = new ArrayList<>();
                    for (String[] s : entries) {
                        tempTest.add(new Entry(s[0], s[1], s[2]));
                    }
                    tempTest.sort(Comparator.comparing(Entry::getNumPrice).reversed());
                    for (Entry e : tempTest
                    ) {
                        e.print();
                    }
                    System.out.println();
                }
                break;
            case 2:
                System.out.println("Types:");
                ArrayList<Type> tempType = new ArrayList<>();
                tempType.add(new Type("Food", subTotalFood));
                tempType.add(new Type("Entertainment", subTotalEntertainment));
                tempType.add(new Type("Clothes", subTotalClothes));
                tempType.add(new Type("Other", subTotalOther));
                tempType.sort(Comparator.comparing(Type::getNumTotal).reversed());
                for (Type t :
                        tempType) {
                    t.print();
                }
                System.out.printf("Total sum:" +
                        " $%.2f\n", this.total);
                System.out.println();

                break;
            case 3:

                System.out.println("Choose the type of purchase");
                System.out.println("1) Food");
                System.out.println("2) Clothes");
                System.out.println("3) Entertainment");
                System.out.println("4) Other");

                ArrayList<Entry> tempTest = new ArrayList<>();
                for (String[] s : entries) {
                    tempTest.add(new Entry(s[0], s[1], s[2]));
                }
                tempTest.sort(Comparator.comparing(Entry::getNumPrice).reversed());

                String type = "";
                switch (Integer.parseInt(in.nextLine())) {
                    case 1:
                        System.out.println();
                        type = "Food";

                        ArrayList foodList = new ArrayList<>();
                        for (Entry e : tempTest
                        ) {
                            if (e.type.equals(type)) {
                                foodList.add(e);
                            }
                        }

                        if (foodList.isEmpty()) {
                            System.out.println("The purchase list is empty!");
                            System.out.println();
                        } else {
                            System.out.println(type + ":");
                            for (Entry e : tempTest
                            ) {
                                if (e.type.equals("Food")) {
                                    e.print();
                                }
                            }
                            System.out.printf("Total sum:" +
                                    " $%.2f\n", this.subTotalFood);
                        }
                        System.out.println();
                        break;
                    case 2:
                        System.out.println();
                        type = "Clothes";

                        ArrayList clothesList = new ArrayList<>();
                        for (Entry e : tempTest
                        ) {
                            if (e.type.equals(type)) {
                                clothesList.add(e);
                            }
                        }

                        if (clothesList.isEmpty()) {
                            System.out.println("The purchase list is empty!");
                            System.out.println();
                        } else {
                            System.out.println(type + ":");
                            for (Entry e : tempTest
                            ) {
                                if (e.type.equals(type)) {
                                    e.print();
                                }
                            }
                            System.out.printf("Total sum:" +
                                    " $%.2f\n", this.subTotalClothes);
                        }
                        System.out.println();
                        break;
                    case 3:
                        System.out.println();
                        type = "Entertainment";

                        ArrayList entertainmentList = new ArrayList<>();
                        for (Entry e : tempTest
                        ) {
                            if (e.type.equals(type)) {
                                entertainmentList.add(e);
                            }
                        }

                        if (entertainmentList.isEmpty()) {
                            System.out.println("The purchase list is empty!");
                            System.out.println();
                        } else {
                            System.out.println(type + ":");
                            for (Entry e : tempTest
                            ) {
                                if (e.type.equals(type)) {
                                    e.print();
                                }
                            }
                            System.out.printf("Total sum:" +
                                    " $%.2f\n", this.subTotalEntertainment);
                        }
                        System.out.println();
                        break;
                    case 4:
                        System.out.println();
                        type = "Other";

                        ArrayList otherList = new ArrayList<>();
                        for (Entry e : tempTest
                        ) {
                            if (e.type.equals(type)) {
                                otherList.add(e);
                            }
                        }

                        if (otherList.isEmpty()) {
                            System.out.println("The purchase list is empty!");
                            System.out.println();
                        } else {
                            System.out.println(type + ":");
                            for (Entry e : tempTest
                            ) {
                                if (e.type.equals(type)) {
                                    e.print();
                                }
                            }
                            System.out.printf("Total sum:" +
                                    " $%.2f\n", this.subTotalOther);
                        }
                        System.out.println();
                        break;
                }
                break;
            case 4:
                printMenu();
                break;
        }
    }

    void showPurchases() {
        Scanner in = new Scanner(System.in);

        boolean keepgoing = true;
        while (keepgoing) {
            System.out.println("Choose the type of purchase");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) All");
            System.out.println("6) Back");
            int selection = Integer.parseInt(in.nextLine());
            System.out.println();

                switch (selection) {
                    case 1:
                        totalPurchases();
                        System.out.println("Food:");

                        if (this.subTotalFood > 0) {

                            for (int i = 0; i < entries.size(); i++) {
                                if (entries.get(i)[2].equalsIgnoreCase("Food")) {
                                    System.out.printf(entries.get(i)[0] +
                                            " $%.2f\n", Double.parseDouble(entries.get(i)[1]));
                                }
                            }
                            System.out.printf("Total sum: $%.2f\n", this.subTotalFood);
                            System.out.println();
                        } else {
                                System.out.println("The purchase list is empty");
                                System.out.println();
                        }
                        break;

                    case 2:
                        totalPurchases();
                        System.out.println("Clothes:");

                        if (this.subTotalClothes > 0) {

                            for (int i = 0; i < entries.size(); i++) {
                                if (entries.get(i)[2].equalsIgnoreCase("Clothes")) {
                                    System.out.printf(entries.get(i)[0] +
                                            " $%.2f\n", Double.parseDouble(entries.get(i)[1]));
                                }
                            }
                            System.out.printf("Total sum: $%.2f\n", this.subTotalClothes);
                            System.out.println();
                        } else {
                            System.out.println("The purchase list is empty");
                            System.out.println();
                        }
                        break;

                    case 3:
                        totalPurchases();
                        System.out.println("Entertainment:");

                        if (this.subTotalEntertainment > 0) {

                            for (int i = 0; i < entries.size(); i++) {
                                if (entries.get(i)[2].equalsIgnoreCase("Entertainment")) {
                                    System.out.printf(entries.get(i)[0] +
                                            " $%.2f\n", Double.parseDouble(entries.get(i)[1]));
                                }
                            }
                            System.out.printf("Total sum: $%.2f\n", this.subTotalEntertainment);
                            System.out.println();
                        } else {
                            System.out.println("The purchase list is empty");
                            System.out.println();
                        }
                        break;

                    case 4:
                        totalPurchases();
                        System.out.println("Other:");

                        if (this.subTotalOther > 0) {

                            for (int i = 0; i < entries.size(); i++) {
                                if (entries.get(i)[2].equalsIgnoreCase("Other")) {
                                    System.out.printf(entries.get(i)[0] +
                                            " $%.2f\n", Double.parseDouble(entries.get(i)[1]));
                                }
                            }
                            System.out.printf("Total sum: $%.2f\n", this.subTotalOther);
                            System.out.println();
                        } else {
                            System.out.println("The purchase list is empty");
                            System.out.println();
                        }
                        break;

                    case 5:
                        System.out.println("All:");
                        if (entries.size() == 0) {
                            System.out.println("The purchase list is empty");
                            System.out.println();
                        } else {
                            for (int i = 0; i < entries.size(); i++) {
                                System.out.printf(entries.get(i)[0] +
                                        " $%.2f\n", Double.parseDouble(entries.get(i)[1]));
                                totalPurchases();
                            }
                            System.out.printf("Total sum: $%.2f\n", this.total);
                            System.out.println();
                        }
                        break;

                    case 6:
                        keepgoing = false;
                        printMenu();
                        break;

                    default:
                        System.out.println("Error try again");

            }
        }
    }

    void totalPurchases() {
        this.total = 0;
        this.subTotalFood = 0;
        this.subTotalClothes = 0;
        this.subTotalEntertainment = 0;
        this.subTotalOther = 0;


        for (int i = 0; i < entries.size(); i++) {
            this.total += Double.parseDouble(entries.get(i)[1]);

            if (entries.get(i)[2].equalsIgnoreCase("Food")) {
                this.subTotalFood += Double.parseDouble(entries.get(i)[1]);
            } else if (entries.get(i)[2].equalsIgnoreCase("Clothes")) {
                this.subTotalClothes += Double.parseDouble(entries.get(i)[1]);
            } else if (entries.get(i)[2].equalsIgnoreCase("Entertainment")) {
                this.subTotalEntertainment += Double.parseDouble(entries.get(i)[1]);
            } else {
                this.subTotalOther += Double.parseDouble(entries.get(i)[1]);
            }
        }

    }

    void addPurchase() {
        Scanner in = new Scanner(System.in);

        boolean keepgoing = true;
        while (keepgoing) {
            System.out.println("Choose the type of purchase");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) Back");

            String typeSelection = "Other";
            int selection = Integer.parseInt(in.nextLine());

                switch (selection) {
                    case 1:
                        typeSelection = "Food";
                        break;
                    case 2:
                        typeSelection = "Clothes";
                        break;
                    case 3:
                        typeSelection = "Entertainment";
                        break;
                    case 4:
                        typeSelection = "Other";
                        break;
                    case 5:
                        typeSelection = "Back";
                        keepgoing = false;
                        break;
                    default:
                        System.out.println("Error try again");
                        break;

            }

            System.out.println();

            if (!typeSelection.equals("Back")) {
                String[] item = new String[3];
                item[2] = typeSelection;

                System.out.println("Enter purchase name:");
                item[0] = in.nextLine();
                System.out.println("Enter its price:");
                item[1] = in.nextLine();

                entries.add(item);
                System.out.println("Purchase was added!");
                System.out.println();
            } else {
                printMenu();
            }
        }
    }

     void addIncome(double income) {
        this.income += income;
        System.out.println("Income was added!");
        System.out.println();
        printMenu();
    }
}
