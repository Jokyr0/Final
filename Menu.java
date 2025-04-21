import java.util.Scanner;

public class Menu {
    Scanner scan = new Scanner(System.in);

    void MainMenu(){
        int userInt = 0;
        while(userInt != 4) {
            System.out.print("1. Manage Products\n2. Complete Transactions\n3. View Customer History\n4. Exit the Application\n");
            System.out.print("Please enter an option: ");
            userInt = scan.nextInt();
            switch (userInt) {
                case 1:
                    ManageProducts();
                    break;
                case 2:
                    CompleteTransactions();
                    break;
                case 3:
                    CustomerHistory();
                    break;
                default:
                    break;
            }
        }
    }


    void ManageProducts(){
        int userInt = 0;
        while (userInt != 5){
            System.out.println("Hello user! Welcome to the manage products section, would you like to...");
            System.out.print("1. Add Product\n2. View all Products\n3. Modify a Product\n4. Remove a Product\n5. Return to Main Menu\n");
            System.out.print("Please enter an option: ");
            userInt = scan.nextInt();
            scan.nextLine();

            switch (userInt){
                case 1:
                    StoreManagementSystem.addProduct(scan);
                    break;
                case 2:
                    StoreManagementSystem.viewProduct(scan);
                    break;
                case 3:
                    StoreManagementSystem.modifyProduct(scan);
                    break;
                case 4:
                    StoreManagementSystem.removeProduct(scan);
                    break;
                default:
                    System.out.println("Returning to main menu...");
                    break;
            }
        }
    }


    void CompleteTransactions() {
        int userInt = 0;
    
        while (userInt != 4) {
            System.out.println("1. Add a Client (with validation for email and phone format)");
            System.out.println("2. Make a Purchase (link a client to a product, specify the quantity bought, and apply any discounts if applicable)");
            System.out.println("3. View Purchase History (retrieve all purchases for a given client, showing product details, dates, and quantities)");
            System.out.println("4. Return to Main Menu");
            System.out.print("Please enter an option: ");
            userInt = scan.nextInt();
            scan.nextLine();
    
            switch (userInt) {
                case 1:
                    StoreManagementSystem.addPerson(scan);
                    break;
                case 2:
                    StoreManagementSystem.makePurchase();
                    System.out.println("Returning to transaction menu...");
                    break;
                case 3:
                    StoreManagementSystem.viewPurchaseHistory();
                    break;
                case 4:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    void CustomerHistory() {
        Scanner scan = new Scanner(System.in);
        int userInt = 0;
    
        while (userInt != 3) {
            System.out.println("1. Search for customer");
            System.out.println("2. View all past purchases");
            System.out.println("3. Return to Main Menu");
            System.out.print("Please enter an option: ");
            userInt = scan.nextInt();
            scan.nextLine();
    
            switch (userInt) {
                case 1:
                    StoreManagementSystem.searchCustomer();
                    break;
                case 2:
                    StoreManagementSystem.viewAllPastPurchases();
                    break;
                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
