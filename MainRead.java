import java.util.Scanner;

public class MainRead {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataStorage dataStorage = new DataStorage();
        CommandProcessor commandProcessor = new CommandProcessor(dataStorage);

        System.out.println("Welcome to the Personal Finance Management System!");
        System.out.println("Type 'help' for a list of commands.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the program. Goodbye!");
                break;
            }

            commandProcessor.processCommand(input);
        }

        scanner.close();
    }
}

