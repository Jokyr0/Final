import java.sql.*;
import java.util.Scanner;

public class StoreManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/inventoryappdb";
    private static final String user = "root";
    private static final String password = "pass"; //Yourpassword
    private static Connection connection;


    public static void connectDB() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded!");

            connection = DriverManager.getConnection(url, user, password);
            var statement = connection.createStatement();
            System.out.println("Connection established!");

            //Show tables
            System.out.println("Tables in the database: ");
            var resultSet = statement.executeQuery("SHOW TABLES");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public static void addProduct(Scanner scan) {
        int choice;

        do {
            System.out.print("Product ID: ");
            String ID = scan.nextLine();

            System.out.print("Product name: ");
            String name = scan.nextLine();

            System.out.print("Price: ");
            float price = scan.nextFloat();

            while (price < 0) {
                System.out.println("Error...quantity must be > 0");
                System.out.print("Re-enter the quantity: ");
                price = scan.nextFloat();
            }

            System.out.print("Product Quantity: ");
            int quantity = scan.nextInt();

            while (quantity < 0) {
                System.out.println("Error...quantity must be > 0");
                System.out.print("Re-enter the quantity: ");
                quantity = scan.nextInt();
            }

            String sql = "insert into Products (productID, itemName, itemPrice, itemQuantity) VALUES (?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, ID);
                statement.setString(2, name);
                statement.setFloat(3, price);
                statement.setInt(4, quantity);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error", e);
            }

            System.out.println(name + " has been added to Products");
            System.out.println("Would you like to add a new product?\n1. Yes\n2. No");
            System.out.print("Please enter an options: ");
            choice = scan.nextInt();

        } while (choice == 1);
    }

    public static void viewProduct(Scanner scan) {
        String sql;
        PreparedStatement statement;
        ResultSet rs;
        int choice;
    
        try {
            // Display the list of products
            System.out.println("View all products by...\n1. Price\n2. Quantity");
            System.out.print("Please enter an option: ");
            choice = scan.nextInt();
    
            if (choice == 1) {
                sql = "SELECT productID, itemName, itemPrice FROM Products ORDER BY itemPrice ASC";
                statement = connection.prepareStatement(sql);
                rs = statement.executeQuery();
    
                System.out.printf("%-10s\t%-15s\t%-10s\n", "Product ID", "Name", "Price");
                System.out.println("---------------------------------------------");
    
                while (rs.next()) {
                    String productID = rs.getString("productID");
                    String name = rs.getString("itemName");
                    float price = rs.getFloat("itemPrice");
                    System.out.printf("%-10s\t%-15s\t$%-10.2f\n", productID, name, price);
                }
    
            } else if (choice == 2) {
                sql = "SELECT productID, itemName, itemQuantity FROM Products ORDER BY itemQuantity ASC";
                statement = connection.prepareStatement(sql);
                rs = statement.executeQuery();
    
                System.out.printf("%-10s\t%-15s\t%-10s\n", "Product ID", "Name", "Quantity");
                System.out.println("---------------------------------------------");
    
                while (rs.next()) {
                    String productID = rs.getString("productID");
                    String name = rs.getString("itemName");
                    int quantity = rs.getInt("itemQuantity");
                    System.out.printf("%-10s\t%-15s\t%-10d\n", productID, name, quantity);
                }
    
            } else {
                System.out.println("Invalid option.");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void modifyProduct(Scanner scan) {
        String sqlCheck = "SELECT * FROM Products WHERE productID = ?";
        String sqlUpdateName = "UPDATE Products SET itemName = ? WHERE productID = ?";
        String sqlUpdatePrice = "UPDATE Products SET itemPrice = ? WHERE productID = ?";
        String sqlUpdateQuantity = "UPDATE Products SET itemQuantity = ? WHERE productID = ?";
    
        System.out.print("Please enter the ID of the product you want to modify: ");
        String ID = scan.nextLine();
    
        try (PreparedStatement checkStatement = connection.prepareStatement(sqlCheck)) {
            checkStatement.setString(1, ID);
            ResultSet rs = checkStatement.executeQuery();
    
            if (rs.next()) {
                System.out.println("What would you like to modify?\n1. Name\n2. Price\n3. Quantity");
                System.out.print("Please enter an option: ");
                int choice = scan.nextInt();
                scan.nextLine();
    
                switch (choice) {
                    case 1:
                        System.out.print("Please enter the product's new name: ");
                        String name = scan.nextLine();
                        try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdateName)) {
                            updateStatement.setString(1, name);
                            updateStatement.setString(2, ID);
                            updateStatement.executeUpdate();
                            System.out.println("Product name updated successfully.");
                        }
                        break;
    
                    case 2:
                        System.out.print("Please enter the product's new price: ");
                        float price = scan.nextFloat();
                        try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdatePrice)) {
                            updateStatement.setFloat(1, price);
                            updateStatement.setString(2, ID);
                            updateStatement.executeUpdate();
                            System.out.println("Product price updated successfully.");
                        }
                        break;
    
                    case 3:
                        System.out.print("Please enter the product's new quantity: ");
                        int quantity = scan.nextInt();
                        try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdateQuantity)) {
                            updateStatement.setInt(1, quantity);
                            updateStatement.setString(2, ID);
                            updateStatement.executeUpdate();
                            System.out.println("Product quantity updated successfully.");
                        }
                        break;
    
                    default:
                        System.out.println("Invalid option.");
                        break;
                }
            } else {
                System.out.println("Product with ID " + ID + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeProduct(Scanner scan) {
        String sqlCheck = "SELECT * FROM Products WHERE productID = ?";
        String sqlDelete = "DELETE FROM Products WHERE productID = ?";
    
        System.out.print("Please enter the product's ID: ");
        String ID = scan.nextLine();
    
        try (PreparedStatement checkStatement = connection.prepareStatement(sqlCheck)) {
            checkStatement.setString(1, ID);
            ResultSet rs = checkStatement.executeQuery();
    
            if (rs.next()) {
                try (PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete)) {
                    deleteStatement.setString(1, ID);
                    deleteStatement.executeUpdate();
                    System.out.println("Product " + ID + " has been removed.");
                }
            } else {
                System.out.println("Product with ID " + ID + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addPerson(Scanner scan){
        int choice = 0;

        while (true){
            System.out.print("First name: ");
            String fname = scan.nextLine();

            System.out.print("Last Name: ");
            String lname = scan.nextLine();

            System.out.print("Email: ");
            String email = scan.nextLine();

            System.out.print("Number: ");
            String number = scan.nextLine();

            String sql = "insert into Persons (firstName, lastName, email, phone) values (?,?,?,?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, fname);
                statement.setString(2, lname);
                statement.setString(3, email);
                statement.setString(4, number);
                statement.executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.out.println("Customer " + fname + " " + lname + " was added!");
            System.out.print("Would you like to add another customer to the system?\n1. Yes\n2. No\n");
            System.out.print("Please enter an options: ");
            choice = scan.nextInt();

            if (choice != 1){
                System.out.println("Returning to main menu...");
                break;
            }
        }
    }

    public static void makePurchase() {
        try {
            Scanner scan = new Scanner(System.in);

            System.out.print("Enter customer first name: ");
            String firstName = scan.nextLine();
            System.out.print("Enter customer last name: ");
            String lastName = scan.nextLine();
    
            // Query to get the customer ID based on the name
            String getCustomerID = "SELECT personID FROM Persons WHERE firstName = ? AND lastName = ?";
            int customerID = -1;
    
            try (PreparedStatement getCustomerStmt = connection.prepareStatement(getCustomerID)) {
                getCustomerStmt.setString(1, firstName);
                getCustomerStmt.setString(2, lastName);
                ResultSet rs = getCustomerStmt.executeQuery();
    
                if (rs.next()) {
                    customerID = rs.getInt("personID");
                } else {
                    System.out.println("Customer not found. Please ensure the name is correct.");
                    return;
                }
            }
    
            System.out.print("Enter product ID: ");
            String productID = scan.nextLine();

            System.out.print("Enter quantity: ");
            int quantity = scan.nextInt();
            scan.nextLine(); // Consume newline
    
            // Check product availability
            String checkStock = "SELECT itemQuantity FROM Products WHERE productID = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkStock)) {
                checkStatement.setString(1, productID);
                ResultSet rs = checkStatement.executeQuery();
    
                if (rs.next()) {
                    int availableQuantity = rs.getInt("itemQuantity");
                    if (availableQuantity < quantity) {
                        System.out.println("Not enough stock available.");
                        return;
                    }
                } else {
                    System.out.println("Product not found.");
                    return;
                }
            }
    
            // Update stock and record purchase
            String updateStock = "UPDATE Products SET itemQuantity = itemQuantity - ? WHERE productID = ?";
            String recordPurchase = "INSERT INTO Purchase (personID, productID, Date, quantityPurchased) VALUES (?, ?, NOW(), ?)";
            try (PreparedStatement updateStockStmt = connection.prepareStatement(updateStock);
                 PreparedStatement recordPurchaseStmt = connection.prepareStatement(recordPurchase)) {
    
                // Update stock
                updateStockStmt.setInt(1, quantity);
                updateStockStmt.setString(2, productID);
                updateStockStmt.executeUpdate();
    
                // Record purchase
                recordPurchaseStmt.setInt(1, customerID);
                recordPurchaseStmt.setString(2, productID);
                recordPurchaseStmt.setInt(3, quantity);
                recordPurchaseStmt.executeUpdate();
    
                System.out.println("Purchase completed successfully!");
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while making the purchase: " + e.getMessage());
        }
    }


    public static void viewPurchaseHistory() {
    Scanner scan = new Scanner(System.in);

    // Prompt for customer name
    System.out.print("Enter customer first name: ");
    String firstName = scan.nextLine();
    System.out.print("Enter customer last name: ");
    String lastName = scan.nextLine();

    // Query to get the customer ID based on the name
    String getCustomerID = "SELECT personID FROM Persons WHERE firstName = ? AND lastName = ?";
    int customerID = -1;

    try (PreparedStatement getCustomerStmt = connection.prepareStatement(getCustomerID)) {
        getCustomerStmt.setString(1, firstName);
        getCustomerStmt.setString(2, lastName);
        ResultSet rs = getCustomerStmt.executeQuery();

        if (rs.next()) {
            customerID = rs.getInt("personID");
        } else {
            System.out.println("Customer not found. Please ensure the name is correct.");
            return;
        }
    } catch (SQLException e) {
        System.err.println("An error occurred while retrieving customer ID: " + e.getMessage());
        return;
    }

    // Query for purchase history
    String sql = "SELECT pu.productID, pu.quantityPurchased AS quantity, pr.itemName, pr.itemPrice, " +
                 "(pu.quantityPurchased * pr.itemPrice) AS totalCost " +
                 "FROM Purchase pu " +
                 "JOIN Products pr ON pu.productID = pr.productID " +
                 "WHERE pu.personID = ?";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, customerID);
        ResultSet rs = statement.executeQuery();

        System.out.printf("%-15s %-10s %-20s %-10s %-10s\n", "Product ID", "Quantity", "Item Name", "Item Price", "Total Cost");
        System.out.println("--------------------------------------------------------------------------");

        boolean hasPurchases = false;
        while (rs.next()) {
            hasPurchases = true;
            String productID = rs.getString("productID");
            int quantity = rs.getInt("quantity");
            String itemName = rs.getString("itemName");
            float itemPrice = rs.getFloat("itemPrice");
            float totalCost = rs.getFloat("totalCost");

            System.out.printf("%-15s %-10d %-20s $%-10.2f $%-10.2f\n", productID, quantity, itemName, itemPrice, totalCost);
        }

        if (!hasPurchases) {
            System.out.println("No purchase history found for customer: " + firstName + " " + lastName);
        }
    } catch (SQLException e) {
        System.err.println("An error occurred while retrieving purchase history: " + e.getMessage());
    }
}


    public static void queryPerson(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter customer ID: ");
        String customerID = scan.nextLine();

        String sql = "SELECT * FROM Persons WHERE customerID = ?";

        // Query for customer information
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customerID);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String email = rs.getString("email");
                String phone = rs.getString("phone");

                System.out.printf("Customer ID: %s\nFirst Name: %s\nLast Name: %s\nEmail: %s\nPhone: %s\n",
                        customerID, firstName, lastName, email, phone);
            } else {
                System.out.println("Customer not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void searchCustomer() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter customer first name: ");
        String firstName = scan.nextLine();
        System.out.print("Enter customer last name: ");
        String lastName = scan.nextLine();
    
        // Query to search for the customer
        String sql = "SELECT personID, email, phone FROM Persons WHERE firstName = ? AND lastName = ?";
    
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            ResultSet rs = statement.executeQuery();
    
            if (rs.next()) {
                int personID = rs.getInt("personID");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
    
                System.out.printf("Customer ID: %d\nFirst Name: %s\nLast Name: %s\nEmail: %s\nPhone: %s\n",
                        personID, firstName, lastName, email, phone);
            } else {
                System.out.println("Customer not found. Please ensure the name is correct.");
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while searching for the customer: " + e.getMessage());
        }
    }

    public static void viewAllPastPurchases() {
        String sql = "SELECT p.firstName, p.lastName, pu.productID, pr.itemName, pu.quantityPurchased, pr.itemPrice " +
                     "FROM Purchase pu " +
                     "JOIN Persons p ON pu.personID = p.personID " +
                     "JOIN Products pr ON pu.productID = pr.productID " +
                     "ORDER BY p.lastName, p.firstName";
    
        // Display the purchase history for all customers
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
    
            System.out.printf("%-15s %-15s %-15s %-20s %-10s %-10s\n", "First Name", "Last Name", "Product ID", "Item Name", "Quantity", "Price");
            System.out.println("---------------------------------------------------------------------------------------------");
    
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String productID = rs.getString("productID");
                String itemName = rs.getString("itemName");
                int quantity = rs.getInt("quantityPurchased");
                float price = rs.getFloat("itemPrice");
    
                System.out.printf("%-15s %-15s %-15s %-20s %-10d $%-10.2f\n", firstName, lastName, productID, itemName, quantity, price);
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while retrieving past purchases: " + e.getMessage());
        }
    }



}
