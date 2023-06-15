import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class passwordmanager {
  
    private static final String FILE_PATH = "C:\\vscode_projects\\passwordmanager\\passwords.txt.txt";
    private static final String SEPARATOR = "|||";
    
    private static Map<String, String> passwords;
    
    public static void main(String[] args) {
        passwords = loadPasswords();
        showMenu();
    }
    
    private static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n--- Password Manager ---");
            System.out.println("1. Add password");
            System.out.println("2. Retrieve password");
            System.out.println("3. Update password");
            System.out.println("4. Delete password");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    addPassword(scanner);
                    break;
                case 2:
                    retrievePassword(scanner);
                    break;
                case 3:
                    updatePassword(scanner);
                    break;
                case 4:
                    deletePassword(scanner);
                    break;
                case 5:
                    savePasswords();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void addPassword(Scanner scanner) {
        System.out.print("Enter website or service name: ");
        String website = scanner.nextLine();
        
        System.out.print("Enter username or email: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        passwords.put(website, encryptPassword(username, password));
        System.out.println("Password added successfully!");
    }
    
    private static void retrievePassword(Scanner scanner) {
        System.out.print("Enter website or service name: ");
        String website = scanner.nextLine();
        
        if (passwords.containsKey(website)) {
            String encryptedPassword = passwords.get(website);
            String[] decryptedPassword = decryptPassword(encryptedPassword);
            
            System.out.println("Username/email: " + decryptedPassword[0]);
            System.out.println("Password: " + decryptedPassword[1]);
        } else {
            System.out.println("No password found for the given website.");
        }
    }
    
    private static void updatePassword(Scanner scanner) {
        System.out.print("Enter website or service name: ");
        String website = scanner.nextLine();
        
        if (passwords.containsKey(website)) {
            System.out.print("Enter new username or email: ");
            String username = scanner.nextLine();
            
            System.out.print("Enter new password: ");
            String password = scanner.nextLine();
            
            passwords.put(website, encryptPassword(username, password));
            System.out.println("Password updated successfully!");
        } else {
            System.out.println("No password found for the given website.");
        }
    }
    
    private static void deletePassword(Scanner scanner) {
        System.out.print("Enter website or service name: ");
        String website = scanner.nextLine();
        
        if (passwords.containsKey(website)) {
            passwords.remove(website);
            System.out.println("Password deleted successfully!");
        } else {
            System.out.println("No password found for the given website.");
        }
    }

    
    
    private static Map<String, String> loadPasswords() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating passwords file: " + e.getMessage());
            }
        }

        Map<String, String> passwordMap = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(SEPARATOR);
                if (parts.length == 2) {
                    String website = parts[0];
                    String encryptedPassword = parts[1];
                    passwordMap.put(website, encryptedPassword);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading passwords: " + e.getMessage());
        }
        
        return passwordMap;
    }
    
    private static void savePasswords() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, String> entry : passwords.entrySet()) {
                String website = entry.getKey();
                String encryptedPassword = entry.getValue();
                writer.write(website + SEPARATOR + encryptedPassword);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving passwords: " + e.getMessage());
        }
    }
    
    // Encrypts the password using a simple encryption scheme
    private static String encryptPassword(String username, String password) {
        StringBuilder encryptedPassword = new StringBuilder();
        
        for (int i = 0; i < username.length(); i++) {
            char encryptedChar = (char) (username.charAt(i) + 1);
            encryptedPassword.append(encryptedChar);
        }
        
        encryptedPassword.append(SEPARATOR);
        
        for (int i = 0; i < password.length(); i++) {
            char encryptedChar = (char) (password.charAt(i) + 1);
            encryptedPassword.append(encryptedChar);
        }
        
        return encryptedPassword.toString();
    }
    
    // Decrypts the encrypted password and returns an array containing the username and password
    private static String[] decryptPassword(String encryptedPassword) {
        String[] decryptedPassword = new String[2];
        
        int separatorIndex = encryptedPassword.indexOf(SEPARATOR);
        String encryptedUsername = encryptedPassword.substring(0, separatorIndex);
        String encryptedPwd = encryptedPassword.substring(separatorIndex + SEPARATOR.length());
        
        StringBuilder decryptedUsername = new StringBuilder();
        for (int i = 0; i < encryptedUsername.length(); i++) {
            char decryptedChar = (char) (encryptedUsername.charAt(i) - 1);
            decryptedUsername.append(decryptedChar);
        }
        
        StringBuilder decryptedPwd = new StringBuilder();
        for (int i = 0; i < encryptedPwd.length(); i++) {
            char decryptedChar = (char) (encryptedPwd.charAt(i) - 1);
            decryptedPwd.append(decryptedChar);
        }
        
        decryptedPassword[0] = decryptedUsername.toString();
        decryptedPassword[1] = decryptedPwd.toString();
        
        return decryptedPassword;
    }
}
