import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataStorage {
    private Map<String, User> users;

    public DataStorage() {
        this.users = new HashMap<>();
        preloadUsers();
    }

    private void preloadUsers() {
        addUser("aaa", "1234",
                Map.of("salary", 1000.0, "bonus", 200.0),
                Map.of("rent", 500.0, "groceries", 150.0));
        addUser("bbb", "4321",
                Map.of("freelance", 1200.0),
                Map.of("utilities", 300.0, "transport", 100.0));
    }

    public boolean addUser(String username, String password, Map<String, Double> initialIncome, Map<String, Double> initialExpenses) {
        if (users.containsKey(username)) {
            return false; // User already exists
        }
        User newUser = new User(username, password);
        if (initialIncome != null) {
            initialIncome.forEach((category, amount) -> newUser.addIncome(amount, category));
        }
        if (initialExpenses != null) {
            initialExpenses.forEach((category, amount) -> newUser.addExpense(amount, category));
        }
        users.put(username, newUser);
        return true;
    }

    public boolean removeUser(String username, String password) {
        User user = validateUser(username, password);
        if (user != null) {
            users.remove(username);
            return true;
        }
        return false;
    }

    public boolean addIncome(String username, String password, double amount, String category) {
        User user = validateUser(username, password);
        if (user != null) {
            user.addIncome(amount, category);
            return true;
        }
        return false;
    }

    public boolean addExpense(String username, String password, double amount, String category) {
        User user = validateUser(username, password);
        if (user != null) {
            user.addExpense(amount, category);
            return true;
        }
        return false;
    }

    public Map<String, Double> getIncome(String username, String password) {
        User user = validateUser(username, password);
        return user != null ? user.getIncome() : null;
    }

    public Map<String, Double> getExpenses(String username, String password) {
        User user = validateUser(username, password);
        return user != null ? user.getExpenses() : null;
    }

    public double calculateTotalBalance(String username, String password) {
        User user = validateUser(username, password);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        double totalIncome = user.getIncome().values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = user.getExpenses().values().stream().mapToDouble(Double::doubleValue).sum();
        return totalIncome - totalExpenses;
    }

    public boolean saveUserDataToFile(String username, String password, String filePath) {
        User user = validateUser(username, password);
        if (user == null) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("User: " + username + "\n");
            writer.write("Income:\n");
            user.getIncome().forEach((category, amount) -> {
                try {
                    writer.write(category + ": " + amount + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.write("Expenses:\n");
            user.getExpenses().forEach((category, amount) -> {
                try {
                    writer.write(category + ": " + amount + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private User validateUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    private static class User {
        private String username;
        private String password;
        private Map<String, Double> income;
        private Map<String, Double> expenses;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
            this.income = new HashMap<>();
            this.expenses = new HashMap<>();
        }

        public String getPassword() {
            return password;
        }

        public void addIncome(double amount, String category) {
            income.put(category, income.getOrDefault(category, 0.0) + amount);
        }

        public void addExpense(double amount, String category) {
            expenses.put(category, expenses.getOrDefault(category, 0.0) + amount);
        }

        public Map<String, Double> getIncome() {
            return new HashMap<>(income);
        }

        public Map<String, Double> getExpenses() {
            return new HashMap<>(expenses);
        }
    }
}

