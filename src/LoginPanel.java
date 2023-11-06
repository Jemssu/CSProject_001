import javax.swing.JOptionPane;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**This class contains the operations and the UI of the login panel.
 * @author James Nathaniel F. Tan
 * @Note 
*/
public class LoginPanel {
    public static void main(String[] args) {
        boolean loginSuccess = false;

        while (!loginSuccess) {
            String inputUsername = JOptionPane.showInputDialog("Enter your username:");
            if (inputUsername == null) {
                // The user pressed "Cancel," so exit the program
                System.exit(0);
            }

            String inputPassword = JOptionPane.showInputDialog("Enter your password:");
            if (inputPassword == null) {
                // The user pressed "Cancel," so exit the program
                System.exit(0);
            }

            if (inputUsername != null && inputPassword != null) {
                if (inputUsername.equals("manager") && inputPassword.equals("manager")) {
                    System.out.println("Successfully Accessed the Manager Panel.");
                    ManagerPanel.main(args);
                    loginSuccess = true;
                } else if (inputUsername.equals("admin") && inputPassword.equals("admin")) {
                    System.out.println("Attempting to Make a New User.");
                    createNewUser();
                } else {
                    if (isValidUsername(inputUsername) && authenticate(inputUsername, inputPassword)) {
                        JOptionPane.showMessageDialog(null, "Login Successful");
                        updateLoginHistory(inputUsername);
                        System.out.println("Successfully Accessed the Order Panel.");
                        OrderPanel.main(args);
                        loginSuccess = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Login Failed. Please Try Again.");
                        System.out.println("Login Failed. Please Try Again.");
                    }
                }
            }
        }
    }

    // ============================================ //
    //         GETTERS AND OBSERVERS HERE :>        //
    // ============================================ //

    /**This method returns the most recent cashier who logged in
     * the system to input in the order panel
     * @return the most recent username : String
     * @Note
     */
    public String getRecent() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src//passwords.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("recent -- ")) {
                    return line.substring(9);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**This method returns true when the username made has 
     * only alphabetic characters and false otherwise.
     * @param username : String
     * @Note
     */
    private static boolean isValidUsername(String username) {
        return username.matches("[a-zA-Z]+"); // Check if the username contains only letters
    }

    /**This method returns true when the username and password
     * entered matches the ones stored in the file.
     * @param inputUsername : String
     * @param inputPassword : String
     * @Note
     */
    private static boolean authenticate(String inputUsername, String inputPassword) {
        Map<String, String> users = readUsersFromFile("src//passwords.txt");
        String storedPassword = users.get(inputUsername);
        if (storedPassword != null) {
            int shift = 3;
            String decryptedPassword = CaesarCipher.decrypt(storedPassword, shift);
            return inputPassword.equals(decryptedPassword);
        }
        return false;
    }

    /**This method returns true when the username entered
     * is not a duplicate or already exists
     * @param newUsername : String
     * @Note
     */
    private static boolean isUsernameDuplicate(String newUsername) {
        Map<String, String> users = readUsersFromFile("src//passwords.txt");
        return users.containsKey(newUsername);
    }

    // ============================================ //
    //      READER AND WRITERS (ALSO UPDATES)       //
    // ============================================ //

    /**This method reads the users and passwords from the file,
     * and this data will be used by different methods.
     * @param filename : String
     * @Note Added a readLine() to skip the header, just in case it reads it
     * and takes it as a username.
     */
    private static Map<String, String> readUsersFromFile(String filename) {
        Map<String, String> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**This method updates the header which shows who the most
     * recent person.
     * @param username : String
     * @Note Added two '--' instead of '-' one to not make the reader accidentally read recent as a username.
     */
    private static void updateLoginHistory(String username) {
        String newLoginRecord = "recent -- " + username;

        try {
            StringBuilder loginHistory = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader("src//passwords.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("recent -- ")) {
                        loginHistory.append(line).append(System.lineSeparator());
                    }
                }
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter("src//passwords.txt"))) {
                writer.println(newLoginRecord);
                writer.print(loginHistory.toString());
                JOptionPane.showMessageDialog(null, "Login history updated.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ============================================ //
    //           OTHER OPERATIONS NEEDED            //
    // ============================================ //

    /**This method creates a nerw username and writes it into the fileWriter.
     * it also uses the caesar cipher method to encrypt the password before
     * storing it.
     * @param username : String
     * @Note 
     */
    private static void createNewUser() {
        while (true) {
            String newUsername = JOptionPane.showInputDialog("Enter a new username:");
            String newPassword = JOptionPane.showInputDialog("Enter a new password");

            if (newUsername == null || newPassword == null) {
                break;
            }

            int shift = 3;
            String encryptedPassword = CaesarCipher.encrypt(newPassword, shift);

            if (isValidUsername(newUsername) && !isUsernameDuplicate(newUsername)) {
                try (PrintWriter writer = new PrintWriter(new FileWriter("src//passwords.txt", true))) {
                    writer.println(newUsername + " - " + encryptedPassword);
                    JOptionPane.showMessageDialog(null, "User Created successfully!");
                    System.err.println("User Created successfully!");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or username already exists.");
                System.out.println("Invalid username or username already exists.");
            }
        }
    }
} //end of class

/**This class is responsible for encrypting and decrypting the password
 * @author James Nathaniel F. Tan
 * @Note
*/
class CaesarCipher {
    /**This method encrypts the password
     * @param text(password) : String
     * @param shift : int
     * @Note
     */
    public static String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                result.append((char) ((ch - base + shift) % 26 + base));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**This method decrypts the password
     * @param text(password) : String
     * @param shift : int
     * @Note
     */
    public static String decrypt(String text, int shift) {
        return encrypt(text, 26 - shift);
    }
} //end of class
