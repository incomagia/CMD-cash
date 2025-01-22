import java.util.Map;
import java.util.Scanner;

public class CommandProcessor {
    private DataStorage dataStorage;

    public CommandProcessor(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public void processCommand(String commandLine) {
        String[] parts = commandLine.split(" ");
        String command = parts[0].toLowerCase();

        switch (command) {
            case "addnewprof":
                if (parts.length < 3) {
                    System.out.println("Usage: addnewprof <username> <password>");
                } else {
                    String username = parts[1];
                    String password = parts[2];
                    boolean success = dataStorage.addUser(username, password, Map.of(), Map.of());
                    if (success) {
                        System.out.println("User added successfully.");
                    } else {
                        System.out.println("User already exists.");
                    }
                }
                break;

            case "removeprof":
                if (parts.length < 3) {
                    System.out.println("Usage: removeprof <username> <password>");
                } else {
                    String username = parts[1];
                    String password = parts[2];
                    boolean success = dataStorage.removeUser(username, password);
                    if (success) {
                        System.out.println("User removed successfully.");
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                }
                break;

            case "addcash":
                if (parts.length < 5) {
                    System.out.println("Usage: addcash <amount> <category> <username> <password>");
                } else {
                    try {
                        double amount = Double.parseDouble(parts[1]);
                        String category = parts[2];
                        String username = parts[3];
                        String password = parts[4];
                        boolean success = dataStorage.addIncome(username, password, amount, category);
                        if (success) {
                            System.out.println("Income added successfully.");
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Amount must be a number.");
                    }
                }
                break;

            case "addexpense":
                if (parts.length < 5) {
                    System.out.println("Usage: addexpense <amount> <category> <username> <password>");
                } else {
                    try {
                        double amount = Double.parseDouble(parts[1]);
                        String category = parts[2];
                        String username = parts[3];
                        String password = parts[4];
                        if (dataStorage.calculateTotalBalance(username, password) - amount < 0) {
                            System.out.println("Insufficient funds. Expense cannot be added.");
                        } else {
                            boolean success = dataStorage.addExpense(username, password, amount, category);
                            if (success) {
                                System.out.println("Expense added successfully.");
                            } else {
                                System.out.println("Invalid username or password.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Amount must be a number.");
                    }
                }
                break;

            case "marga":
                if (parts.length < 3) {
                    System.out.println("Usage: marga <username> <password>");
                } else {
                    String username = parts[1];
                    String password = parts[2];
                    Double totalBalance = dataStorage.calculateTotalBalance(username, password);
                    if (totalBalance != null) {
                        System.out.println("Total balance for " + username + ": " + totalBalance);
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                }
                break;

            case "info":
                if (parts.length < 3) {
                    System.out.println("Usage: info <username> <password>");
                } else {
                    String username = parts[1];
                    String password = parts[2];
                    Map<String, Double> income = dataStorage.getIncome(username, password);
                    Map<String, Double> expenses = dataStorage.getExpenses(username, password);
                    if (income != null && expenses != null) {
                        System.out.println("Income:");
                        income.forEach((category, amount) -> System.out.println(category + ": " + amount));
                        System.out.println("Expenses:");
                        expenses.forEach((category, amount) -> System.out.println(category + ": " + amount));
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                }
                break;

            case "save":
                if (parts.length < 3) {
                    System.out.println("Usage: save <username> <password>");
                } else {
                    String username = parts[1];
                    String password = parts[2];
                    String filePath = username + "_data.txt"; // Генерация имени файла на основе имени пользователя
                    boolean success = dataStorage.saveUserDataToFile(username, password, filePath);
                    if (success) {
                        System.out.println("Data saved successfully to " + filePath);
                    } else {
                        System.out.println("Invalid username or password, or error saving data.");
                    }
                }
                break;

            case "help":
                System.out.println("Available commands:");
                System.out.println("addnewprof <username> <password> - Add a new user.");
                System.out.println("removeprof <username> <password> - Remove an existing user.");
                System.out.println("addcash <amount> <category> <username> <password> - Add income for a user.");
                System.out.println("addexpense <amount> <category> <username> <password> - Add expense for a user. Cannot exceed available funds.");
                System.out.println("marga <username> <password> - Show total balance for a user.");
                System.out.println("info <username> <password> - Show all income and expenses for a user.");
                System.out.println("save <username> <password> - Save all user data to a file.");
                System.out.println("help - Show this help message.");
                break;

            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
                break;
        }
    }
}

