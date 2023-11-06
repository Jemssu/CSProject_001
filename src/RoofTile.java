import java.io.*;
import java.util.Scanner;

/**This Node class acts as a "Node" from a LinkedList.
 * @author James Nathaniel F. Tan
 * @note
*/
class Node {
    String itemType;
    String itemVariant;
    String itemID;
    double itemPrice;
    int itemStock;
    Node prev;
    Node next;

    /**Default Constructor of Node
     * @param itemType : String - material
     * @param itemVariant : String - color 
     * @param itemID : String - unqiue digits
     * @param itemPrice : double - price
     * @param itemStock : int - amount of stock  
     * @Note   
    */
    public Node(String itemType, String itemVariant, String itemID, double itemPrice, int itemStock) {
        this.itemType = itemType;
        this.itemVariant = itemVariant;
        this.itemID = itemID;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
        this.prev = null;
        this.next = null;
    }
}

/**This ADT uses the NODE class which acts as a representation of a LinkedList.
 * In here we will use operations such as Insertion, Deletion, Searching and 
 * Traversal.
 * @author James Nathaniel F. Tan
 * @note 
 * Add deleteStock, emptyStock, againStock, 
 * 
*/
public class RoofTile {
    private Node head;
    private Node tail;
    private String filePath;

    /**Default Constructor of the RoofTile ADT
     * @param filePath : String - the file path / source of the text file
     * @note 
    */
    public RoofTile(String filePath) {
        head = null;
        tail = null;
        this.filePath = filePath;
    }

    // ============================================ //
    //          DOUBLY LINKEDLIST OPERATIONS        //
    // ============================================ //

    /**This method allows the user to add a Roof Tile 
     * into the front side of thethe linkedList. 
     * Note: THIS DOESN'T INCLUDE SAVING IT!
     * @param itemType : String - material
     * @param itemVariant : String - color 
     * @param itemID : String - unqiue digits
     * @param itemPrice : double - price
     * @param itemStock : int - amount of stock  
     * @Note    
     */
    public void addAtFront(String itemType, String itemVariant, String itemID, double itemPrice, int itemStock) {
        Node newNode = new Node(itemType, itemVariant, itemID, itemPrice, itemStock);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
    }

    /**This method allows the user to add a Roof Tile 
     * into the end side of thethe linkedList. 
     * Note: THIS DOESN'T INCLUDE SAVING IT!
     * @param itemType : String - material
     * @param itemVariant : String - color 
     * @param itemID : String - unqiue digits
     * @param itemPrice : double - price
     * @param itemStock : int - amount of stock  
     * @Note    
     */
    public void addAtEnd(String itemType, String itemVariant, String itemID, double itemPrice, int itemStock) {
        Node newNode = new Node(itemType, itemVariant, itemID, itemPrice, itemStock);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
    }

    /**This methods prints all of the LinkedList.
     * @Note
     */
    public void traversal() {
        readFromFile();

        Node current = head;
        while (current != null) {
            System.out.println("Type: " + current.itemType + ", Variant: " + current.itemVariant + ", ID: "
                    + current.itemID + ", Price: " + current.itemPrice + ", Stock: " + current.itemStock);
            current = current.next;
        }
    }

    /**This methods Stores traversal in a String 
     * @Note
     */
    public String traversalString() {
        readFromFile();
        
        Node current = head;
        StringBuilder hold = new StringBuilder();

        while (current != null) {
            hold.append("Type: ").append(current.itemType)
                .append(", Variant: ").append(current.itemVariant)
                .append(", ID: ").append(current.itemID)
                .append(", Price: ").append(current.itemPrice)
                .append(", Stock: ").append(current.itemStock)
                .append("\n");
            current = current.next;
        }

        return hold.toString();
    }

    // ============================================ //
    //      FILE READER AND WRITER OPERATIONS       //
    // ============================================ //
    
    /**This method allows the user to read a textfile and
     * place the data in a Node (LinkedList). It uses the
     * addAtEnd() method to add the data into the node.
     * @note
     */
    public void readFromFile() {
        // Clear the existing data from the linked list
        head = null;
        tail = null;
    
        try {
            Scanner scanner = new Scanner(new File(filePath));
            scanner.nextLine(); // skips header
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] data = line.split(" - ");
                if (data.length == 5) { 
                    addAtEnd(data[0], data[1], data[2], Double.parseDouble(data[3]), Integer.parseInt(data[4]));
                } else {
                    System.out.println("Invalid input format on line: " + line);
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**This method allows the user to write in a textfile
     * it places the updated nodes back into the textfile
     * @Note
     */
    public void writeToFile() {
        try {
            PrintWriter writer = new PrintWriter(filePath);
            Node current = head;
            writer.println("itemType - itemVariant - itemID - itemPrice - itemStock");
            while (current != null) {
                writer.println(current.itemType + " - " + current.itemVariant + " - " + current.itemID + " - "
                        + current.itemPrice + " - " + current.itemStock);
                current = current.next;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    // ============================================ //
    //        OTHER OPERATIONS FOR MANAGEMENT       //
    // ============================================ //

    /**This method checks if the 'Stock' exists and if 
     * does exist then it adds the amount to the item
     * @param itemID : String - item ID
     * @param amount : int - amount to add
     * @Note
     */
    public void addToStock(String itemID, int amount) {
        if (doesExist(itemID)) {
            Node current = head;
            while (current != null) {
                if (current.itemID.equals(itemID)) {
                    current.itemStock += amount;
                    System.out.println("ADDED -- Item with ID " + itemID + " has been updated to " + current.itemStock);
                    writeToFile(); // Save changes to the file
                    break;
                }
                current = current.next;
            }
        } else {
            System.out.println("Item with ID " + itemID + " not found. No stock updated.");
        }
    }    

    /**This method adds a non-existing Stock to the
     * catalog, if the itemID does exist it cancels
     * the operation
     * @param itemType : String - material
     * @param itemVariant : String - color 
     * @param itemID : String - unqiue digits
     * @param itemPrice : double - price
     * @param itemStock : int - amount of stock  
     * @Note
     */
    public void addNewStock(String itemType, String itemVariant, String itemID, double itemPrice, int itemStock) {
        if (!doesExist(itemID)) {
            addAtEnd(itemType, itemVariant, itemID, itemPrice, itemStock);
            System.out.println("ADDED -- " + itemVariant + " " + itemType + " with ID " + itemID + " price of " + itemPrice + " base stock of " + itemStock);
            writeToFile(); // Save changes to the file
        } else {
            System.out.println("Item with ID " + itemID + " already exists. No new stock added.");
        }
    }
    
    /**This method removes an amount from an existing 
     * Stock from the catalog.
     * @param itemID : String - item ID
     * @param amount : int - amount to remove
     * @Note
     */
    public void removeFromStock(String itemID, int amount) {
        if (doesExist(itemID)) {
            Node current = head;
            while (current != null) {
                if (current.itemID.equals(itemID)) {
                    if (current.itemStock == 0) {
                        System.out.println("Item with ID " + itemID + " has no stock. Contact the Manager.");
                    } else {
                        current.itemStock -= amount;
                        System.out.println("REMOVED -- Item with ID " + itemID + " has been updated to " + current.itemStock);
                        writeToFile(); // Save changes to the file
                    }
                    break;
                }
                current = current.next;
            }
        } else {
            System.out.println("Item with ID " + itemID + " not found. No stock updated.");
        }
    }

    /**This method removes all stock from an existing 
     * Stock from the catalog.
     * @param itemID : String - item ID
     * @param amount : int - amount to remove
     * @Note
     */
    public void emptyStock(String itemID) {
        if (doesExist(itemID)) {
            Node current = head;
            while (current != null) {
                if (current.itemID.equals(itemID)) {
                    current.itemStock = 0;
                    System.out.println("Emptied stock for item with ID " + itemID);
                    writeToFile(); // Save changes to the file
                    break;
                }
                current = current.next;
            }
        } else {
            System.out.println("Item with ID " + itemID + " not found. No stock emptied.");
        }
    }

    /**This method allows the user to modify or update any detail
     * except for the ID of a Stock. 
     * @param itemID : String - item ID
     * @note this is mainly used for CONSOLE, use modifyType, etc.
     * for JOptionePane
     */
    public void modifyMainStock(String itemID) {
        Node current = head;
        boolean itemFound = false;

        while (current != null) {
            if (current.itemID.equals(itemID)) {
                itemFound = true;
                System.out.println("Item found. What would you like to modify?");
                System.out.println("1 - Type");
                System.out.println("2 - Variant");
                System.out.println("3 - Price");
                System.out.println("4 - Stock");
                System.out.println("0 - Exit");

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Old Type: " + current.itemType);
                        System.out.print("Enter the new Type: ");
                        String newType = scanner.next();
                        current.itemType = newType;
                        System.out.println("Changed Successfully!");
                        break;
                    case 2:
                        System.out.println("Old Variant: " + current.itemVariant);
                        System.out.print("Enter the new Variant: ");
                        String newVariant = scanner.next();
                        current.itemVariant = newVariant;
                        System.out.println("Changed Successfully!");
                        break;
                    case 3:
                        System.out.println("Old Price: " + current.itemPrice);
                        double newPrice;
                        do {
                            System.out.print("Enter the new Price: ");
                            if (scanner.hasNextDouble()) {
                                newPrice = scanner.nextDouble();
                                current.itemPrice = newPrice;
                                System.out.println("Changed Successfully!");
                                break;
                            } else {      
                                System.out.println("Invalid input. Please enter a valid number.");
                                scanner.nextLine(); // Clear the invalid input
                            }
                        } while (true);
                        break;
                    case 4:
                        System.out.println("Old Stock: " + current.itemStock);
                        int newStock;
                        do {
                            System.out.print("Enter the new Stock: ");
                            if (scanner.hasNextInt()) {
                                newStock = scanner.nextInt();
                                current.itemStock = newStock;
                                System.out.println("Changed Successfully!");
                                break;
                            } else {
                                System.out.println("Invalid input. Please enter a valid integer.");
                                scanner.nextLine(); // Clear the invalid input
                            }
                        } while (true);
                        break;
                    case 0:
                        break;                        
                    default:
                        System.out.println("Invalid choice. No modifications were made.");
                }
                writeToFile(); // Save changes to the file
                scanner.close();
                break;
            }
            current = current.next;
        }

        if (!itemFound) {
            System.out.println("Item with ID " + itemID + " not found. No modifications were made.");
        }
    }

    public void modifyType(String itemID, String newType) {
        readFromFile();
    
        Node current = head;
        boolean itemFound = false;
    
        while (current != null) {
            if (current.itemID.equals(itemID)) {
                itemFound = true;
                current.itemType = newType;
                System.out.println("Modified Type for item with ID " + itemID);
                writeToFile(); // Save changes to the file
                break;
            }
            current = current.next;
        }
    
        if (!itemFound) {
            System.out.println("Item with ID " + itemID + " not found. No modifications were made.");
        }
    }
    
    public void modifyVariant(String itemID, String newVariant) {
        readFromFile();
    
        Node current = head;
        boolean itemFound = false;
    
        while (current != null) {
            if (current.itemID.equals(itemID)) {
                itemFound = true;
                current.itemVariant = newVariant;
                System.out.println("Modified Variant for item with ID " + itemID);
                writeToFile(); // Save changes to the file
                break;
            }
            current = current.next;
        }
    
        if (!itemFound) {
            System.out.println("Item with ID " + itemID + " not found. No modifications were made.");
        }
    }
    
    public void modifyPrice(String itemID, double newPrice) {
        readFromFile();
    
        Node current = head;
        boolean itemFound = false;
    
        while (current != null) {
            if (current.itemID.equals(itemID)) {
                itemFound = true;
                current.itemPrice = newPrice;
                System.out.println("Modified Price for item with ID " + itemID);
                writeToFile(); // Save changes to the file
                break;
            }
            current = current.next;
        }
    
        if (!itemFound) {
            System.out.println("Item with ID " + itemID + " not found. No modifications were made.");
        }
    }
    
    public void modifyStock(String itemID, int newStock) {
        readFromFile();
    
        Node current = head;
        boolean itemFound = false;
    
        while (current != null) {
            if (current.itemID.equals(itemID)) {
                itemFound = true;
                current.itemStock = newStock;
                System.out.println("Modified Stock for item with ID " + itemID);
                writeToFile(); // Save changes to the file
                break;
            }
            current = current.next;
        }
    
        if (!itemFound) {
            System.out.println("Item with ID " + itemID + " not found. No modifications were made.");
        }
    }
    
    /**This methods allows the user to sort the Stocks
     * based on their ID
     * @Note
     */
    public void sortStock() {
        readFromFile(); // Read the stock from the file
    
        Node current = head;
        Node temp;
        boolean swapped;
    
        if (head == null) {
            System.out.println("Stock is empty. Nothing to sort.");
            return;
        }
    
        do {
            swapped = false;
            current = head;
    
            while (current.next != null) {
                // Compare item IDs and swap if needed
                if (current.itemID.compareTo(current.next.itemID) > 0) {
                    // Swap nodes
                    temp = current;
                    current = current.next;
                    temp.next = current.next;
                    current.prev = temp.prev;
    
                    if (temp.prev == null) {
                        head = current;
                    } else {
                        temp.prev.next = current;
                    }
    
                    if (current.next == null) {
                        tail = temp;
                    } else {
                        current.next.prev = temp;
                    }
    
                    temp.prev = current;
                    current.next = temp;
                    swapped = true;
                } else {
                    current = current.next;
                }
            }
        } while (swapped);
    
        System.out.println("Stock sorted by item ID.");
        writeToFile(); // Save the sorted stock to the file
    }
    
    // ============================================ //
    //       OBSERVERS AND GETTERS ARE HERE         //
    // ============================================ //

    /**This method returns a true or false statement if
     * the item is empty or not
     * @param itemID : String - item ID
     * @Note
     */
    public boolean isEmpty(String itemID) {
        return checkRemaining(itemID) == 0;
    }

    /**This method returns a true or false statement if
     * the item does exist or not in the catalog
     * @param itemID : String - item ID
     * @Note
     */
    public boolean doesExist(String itemID) {
        readFromFile();
        Node current = head;
        while (current != null) {
            if (current.itemID.equals(itemID)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**This method prints the price of the selected item,
     * prints not found otherwise. It also returns a value
     * of -1 if not found.
     * @param itemID : String - item ID
     * @Note removed the printing. it cluttered the whole console
     */
    public double checkPrice(String itemID) {
        readFromFile();
        Node current = head;
        while (current != null) {
            if (current.itemID.equals(itemID)) {
                return current.itemPrice;
            }
            current = current.next;
        }
        System.out.println("Item with ID " + itemID + " not found.");
        return -1.00;
    }
    
    /**This method prints the stocke of the selected item,
     * prints not found otherwise. It also returns a value
     * of -1 if not found.
     * @param itemID : String - item ID
     * @Note removed the printing. it cluttered the whole console
     */
    public int checkRemaining(String itemID) {
        readFromFile();
        Node current = head;
        while (current != null) {
            if (current.itemID.equals(itemID)) {
                return current.itemStock;
            }
            current = current.next;
        }
        System.out.println("Item with ID " + itemID + " not found.");
        return -1;
    }

    // ============================================ //
    //               DISPLAY FOR VISUALS            //
    // ============================================ //
    
    /**This method allows the user to read a textfile and
     * place the data in a Node (LinkedList). It uses the
     * addAtEnd() method to add the data into the node.
     * @note
     */
    public void viewStock() {
        readFromFile();
        Node current = head;
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        while (current != null && option != 0) {
            System.out.println("Type: " + current.itemType + ", Variant: " + current.itemVariant + ", ID: "
                    + current.itemID + ", Price: " + current.itemPrice + ", Stock: " + current.itemStock);
            System.out.println("1 - Next, 2 - Previous, 0 - Exit");
            option = scanner.nextInt();

            if (option == 1 && current.next != null) {
                current = current.next; // Move to the next item
            } else if (option == 2 && current.prev != null) {
                current = current.prev; // Move to the previous item
            }
        }
        scanner.close();
    }
    
    /**This method allows the user to convert all the 
     * Stock into a String.
     * @param itemID : String - item number
     * @return String data
     * @note
     */
    public String searchAndShowDetails(String itemID) { 
        readFromFile();
        Node current = head;
    
        while (current != null) {
            if (current.itemID.equals(itemID)) {
                return "Type: " + current.itemType + ", Variant: " + current.itemVariant +
                       ", ID: " + current.itemID + ", Price: " + current.itemPrice +
                       ", Stock: " + current.itemStock;
            }
            current = current.next;
        }
        
        return "Item with ID " + itemID + " not found.";
    }
}

