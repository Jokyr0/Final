
public class Main {
    public static void main(String[] args) {
        Menu menus = new Menu();
        StoreManagementSystem.connectDB();
        menus.MainMenu();
    }
}