import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**This Queue class acts as a Queue, with operations such as
 * enqueue and dequeue. It also has some other observers in
 * it. 
 * @author James Nathaniel F. Tan
 * @note add Search Order 
*/
class Queue {
    private Order[] orders;
    private int front, rear, size, capacity;

    /**This is the default contructor of the queue
     * by default it takes a maximum of 10 orders
     * @note
     */
    public Queue() {
        capacity = 10; // Maximum capacity
        orders = new Order[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    /**This observes if the queue is full
     * @note
     */
    public boolean isFull() {
        return size == capacity;
    }

    /**This observes if the queue is empty
     * @note
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**This allows the user to add an order
     * into the queue
     * @note
     */
    public void enqueue(Order order) {
        if (!isFull()) {
            rear = (rear + 1) % capacity; // Circular queue
            orders[rear] = order;
            size++;
        } else {
            System.out.println("Queue is full. Cannot add more orders.");
        }
    }

    /**This allows the user to remove an order
     * from the queue
     * @note
     */
    public Order dequeue() {
        if (!isEmpty()) {
            Order order = orders[front];
            orders[front] = null; // Release the reference
            front = (front + 1) % capacity; // Circular queue
            size--;
            return order;
        } else {
            System.out.println("Queue is empty. Cannot dequeue an order.");
            return null;
        }
    }

    /**This gets the queue size
     * @note
     */
    public int getSize() {
        return size;
    }
}

/**This class is responsible for the operations of adding and removing orders
 * @author James Nathaniel F. Tan
 * @note ADD SEARCH Order Operation
*/
public class Order {
    private String filePath;
    private String orderID, orderDate, customerName, cashierName, itemID;
    private int amount;
    private double totalPrice, itemPrice;
    private RoofTile rTile;
    private LoginPanel login;
    private boolean fileRead = false;
    private static Queue orderQueue = new Queue(); 

    /**This is the default constructor when calling this class.
     * @param filePath : String 
     * @param login : LoginPanel Class
     * @note
     */
    public Order(String filePath, LoginPanel login) {
        this.filePath = filePath;
        this.login = login;
        rTile = new RoofTile("src//tilesdata.txt"); // Initialize rTile
        readFromFile();
    }

    /**This is constructor used when making an order.
     * @param filePath : String 
     * @note
     */
    public Order(String orderID, String orderDate, String customerName, String cashierName, String itemID, int amount, double totalPrice) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.cashierName = cashierName;
        this.itemID = itemID;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }

    // ============================================ //
    //          QUEUE ORDER-TYPE OPERATIONS         //
    // ============================================ //

    /**This queues the order using the Queue ADT 
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public void enqueue(Order order) {
        orderQueue.enqueue(order);
    }

    /**This just dequeues the order
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public Order dequeue() {
        return orderQueue.dequeue(); 
    }

    /**This returns true if the queue is empty
     * @return true / false
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public boolean isEmpty() {
        return orderQueue.isEmpty();
    }

    /**This returns true if the queue is full
     * @return true / false
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public boolean isFull() {
        return orderQueue.isFull();
    }

    // ============================================ //
    //      FILE READER AND WRITER OPERATIONS       //
    // ============================================ //

    /**This allows the other methods in this class to read a textFile
     * and place the contents in a queue.
     * @note
     */
    public void readFromFile() {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Skip the header row
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
    
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" - ");
                if (parts.length == 7) {
                    String orderID = parts[0];
                    String orderDate = parts[1];
                    String customerName = parts[2];
                    String cashierName = parts[3];
                    String itemID = parts[4];
                    int amount = Integer.parseInt(parts[5]);
                    double totalPrice = Double.parseDouble(parts[6]);
                    orderQueue.enqueue(new Order(orderID, orderDate, customerName, cashierName, itemID, amount, totalPrice));
                }
            }
            fileRead = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** This allows the other methods in this class to store the queue or modified queue back into the text file.
     * @note
     */
    public void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("orderID - orderDate - customerName - cashierName - itemID - amount - totalPrice");
            writer.newLine();
            
            Queue tempQueue = new Queue(); // Create a temporary queue to preserve the order of items

            while (!isEmpty()) {
                Order order = orderQueue.dequeue();
                String line = order.orderID + " - " + order.orderDate + " - " + order.customerName + " - " +
                        order.cashierName + " - " + order.itemID + " - " + order.amount + " - " + order.totalPrice;
                writer.write(line);
                writer.newLine();
                
                // Enqueue the order back to the temporary queue
                tempQueue.enqueue(order);
            }
            
            // Restore the original queue order from the temporary queue
            while (!tempQueue.isEmpty()) {
                orderQueue.enqueue(tempQueue.dequeue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**This allows the other methods in this class to store the queue
     * or modified queue back into the textFile.
     * @param shippedOrder : Order - the order sent
     * @note
     */
    public void writeTransactionToFile(Order shippedOrder) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src//transactions.txt", true))) {
            String shippedDate = getCurrentDate();

            String line = shippedOrder.orderID + " - " + shippedOrder.orderDate + " - " + shippedDate + " - " + shippedOrder.customerName + " - " +
                    shippedOrder.cashierName + " - " + shippedOrder.itemID + " - " + shippedOrder.amount + " - " + shippedOrder.totalPrice;
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ============================================ //
    //       OBSERVERS AND GETTERS ARE HERE         //
    // ============================================ //

    /**This returns true if the itemID entered exists.
     * @param ItemID : String
     * @return true / false
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public boolean doesExistItemID(String itemID) {
        return rTile.doesExist(itemID);
    }

    /**This returns true if the itemID entered has
     * no Stock Left / equal to 0.
     * @param itemID : String
     * @return true / false
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public boolean doesHaveNoStock(String itemID) {
        return rTile.checkRemaining(itemID) == 0;
    }

    /**This returns an int value of how much Stock
     * does the entered itemID have left.
     * @param itemID : String
     * @return int Stock
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public int howManyStockLeft(String itemID) {
        return rTile.checkRemaining(itemID);
    }

    /**This returns a double value of the
     * price of the itemID.
     * @param itemID : String
     * @return double Price
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public double getItemPrice(String itemID) {
        return rTile.checkPrice(itemID);
    }

    /**This returns a double value of the 
     * price of the itemID * amount.
     * @param itemID : String
     * @param amount : int
     * @return double TotalPrice
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public double getTotalPrice(String itemID, int amount) {
        itemPrice = getItemPrice(itemID);
        totalPrice = itemPrice * amount;
        return totalPrice;
    }

    /**This gets the currentDate used when ordering to know when 
     * an order was done and shipped in a yy-MM-dd format.
     * @return String Date (yy-MM-dd)
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**This randomly generates a 3 char + 3 digit
     * ID that can be used to distinguish orders.
     * @return String orderID
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public String getOrderID() {
        // Generate a random 3-digit number
        int random = (int) (Math.random() * 900) + 100;
        // Generate 3 random letters
        char letter1 = (char) ('A' + (Math.random() * 26));
        char letter2 = (char) ('A' + (Math.random() * 26));
        char letter3 = (char) ('A' + (Math.random() * 26));
        return String.format("%03d%c%c%c", random, letter1, letter2, letter3);
    }

    /**This gets the name of the cashier.
     * @return String cashierName
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public String getCashierName() {
        return login.getRecent();
    }

    // ============================================ //
    //         OTHER OPERATIONS FOR ORDERING        //
    // ============================================ //

    /**This adds an order to the queue.
     * @param orderID : String
     * @param orderDate : String
     * @param customerName : String
     * @param cashierName : String
     * @param itemID : String
     * @param amount : int
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public void addOrder(String orderID, String orderDate, String customerName, String cashierName, String itemID, int amount) {
        
        if (doesExistItemID(itemID) ) {
            if (doesHaveNoStock(itemID) || rTile.checkRemaining(itemID) < amount) {
                System.out.println("No more Stock, can't Order");
                return;
            }
            rTile.removeFromStock(itemID, amount);
            totalPrice = getTotalPrice(itemID, amount);
            
        } else {
            System.out.println("ERROR: NO Stock or ITEM ID not FOUND");
            return;
        }
        
        Order newOrder = new Order(orderID, orderDate, customerName, cashierName, itemID, amount, totalPrice);
        orderQueue.enqueue(newOrder);
        writeToFile();
        fileRead = false;
    }

    /**This adds an order to the queue.
     * @param orderID : String
     * @param orderDate : String
     * @param customerName : String
     * @param cashierName : String
     * @param itemID : String
     * @param amount : int
     * @note now its totally the same as AddOrder.
     */
    public void continueOrder(String orderID, String orderDate, String customerName, String cashierName, String itemID, int amount) {
               
        if (doesExistItemID(itemID) ) {
            if (doesHaveNoStock(itemID) || rTile.checkRemaining(itemID) < amount) {
                System.out.println("No more Stock, can't Order");
                return;
            }
            rTile.removeFromStock(itemID, amount);
            totalPrice = getTotalPrice(itemID, amount);
            
        } else {
            System.out.println("ERROR: NO Stock or ITEM ID not FOUND");
            return;
        }
        
        Order newOrder = new Order(orderID, orderDate, customerName, cashierName, itemID, amount, totalPrice);
        orderQueue.enqueue(newOrder);
        writeToFile();
        fileRead = false;
    }
    
    /**This method returns the cashier name for a given order ID.
     * @param orderID : String
     * @return String cashierName
     */
    public String getRecentCashierName(String orderID) {
        String cashierName = "";
        Queue tempQueue = new Queue(); // Create a temporary queue to preserve the order of items
    
        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.dequeue();
            tempQueue.enqueue(order);
    
            if (order.orderID.equals(orderID)) {
                cashierName = order.cashierName;
            }
        }
    
        // Restore the original queue order from the temporary queue
        while (!tempQueue.isEmpty()) {
            orderQueue.enqueue(tempQueue.dequeue());
        }
    
        return cashierName;
    }

    /**This method returns the customer name for a given order ID.
     * @param orderID : String
     * @return String customerName
     */
    public String getRecentCustomerName(String orderID) {
        String customerName = "";
        Queue tempQueue = new Queue(); // Create a temporary queue to preserve the order of items
    
        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.dequeue();
            tempQueue.enqueue(order);
    
            if (order.orderID.equals(orderID)) {
                customerName = order.customerName;
            }
        }
    
        // Restore the original queue order from the temporary queue
        while (!tempQueue.isEmpty()) {
            orderQueue.enqueue(tempQueue.dequeue());
        }
    
        return customerName;
    }

    /**This searches for an order
     * @param orderID : String
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public String searchOrder(String orderID) {
        StringBuilder searchResults = new StringBuilder();
        boolean found = false;
    
        searchResults.append("Search Results:");
        Queue tempQueue = new Queue(); // Create a temporary queue to preserve the order of items
    
        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.dequeue();
            tempQueue.enqueue(order); // Enqueue the order into the temporary queue
    
            if (order.orderID.equals(orderID)) {
                found = true;
                searchResults.append("Order ID: ").append(order.orderID).append("\n");
                searchResults.append("Order Date: ").append(order.orderDate).append("\n");
                searchResults.append("Customer Name: ").append(order.customerName).append("\n");
                searchResults.append("Cashier Name: ").append(order.cashierName).append("\n");
                searchResults.append("Item ID: ").append(order.itemID).append("\n");
                searchResults.append("Amount: ").append(order.amount).append("\n");
                searchResults.append("Total Price: ").append(order.totalPrice).append("\n\n");
            }
        }
    
        // Restore the original queue order from the temporary queue
        while (!tempQueue.isEmpty()) {
            orderQueue.enqueue(tempQueue.dequeue());
        }
    
        if (!found) {
            return "Order not found.";
        }
    
        return searchResults.toString();
    }
    
    
    
    /**This just dequeues the order. Removes it from the queue and
     * adds the details in a transaction history file.
     */
    public void shipOrder() {
    
        if (!isEmpty()) {
            Order shippedOrder = orderQueue.dequeue();
            writeTransactionToFile(shippedOrder); // Write the shipped order to transactions.txt
            
            JOptionPane.showMessageDialog(null, "Order Shipped Successfully.", "Shipped Successfully", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "There are no Orders to be Shipped.", "Error", JOptionPane.ERROR_MESSAGE);
            
        }
    
        // Write the updated queue to the file once, after all orders have been processed
        writeToFile();
    }
    
    
    // ============================================ //
    //               DISPLAY FOR VISUALS            //
    // ============================================ //

    /**This prints the orders in the console.
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public void printOrders() {
        if (isEmpty()) {
            System.out.println("Queue is empty. No orders to print.");
            return;
        }
    
        System.out.println("Orders in the Queue:");
        Queue tempQueue = new Queue(); // Create a temporary queue to preserve the order of items
    
        while (!isEmpty()) {
            Order order = orderQueue.dequeue();
            String orderString = "Order ID: " + order.orderID + "\n" +
                                "Order Date: " + order.orderDate + "\n" +
                                "Customer Name: " + order.customerName + "\n" +
                                "Cashier Name: " + order.cashierName + "\n" +
                                "Item ID: " + order.itemID + "\n" +
                                "Amount: " + order.amount + "\n" +
                                "Total Price: " + order.totalPrice + "\n";
    
            // Print the order
            System.out.println(orderString);
    
            // Enqueue the order back to the temporary queue
            tempQueue.enqueue(order);
        }
    
        // Restore the original queue order from the temporary queue
        while (!tempQueue.isEmpty()) {
            orderQueue.enqueue(tempQueue.dequeue());
        }
    }

    /**This is a String Version of  the printOrders() method
     * @note DO NOT MODIFY ME I'M ALL GOOD
     */
    public String printOrdersString() {
        StringBuilder orderString = new StringBuilder("Orders in the Queue:\n\n");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;
            
            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    // Skip the header row
                    headerSkipped = true;
                    continue;
                }
                
                String[] parts = line.split(" - ");
                if (parts.length == 7) {
                    String orderID = parts[0];
                    String orderDate = parts[1];
                    String customerName = parts[2];
                    String cashierName = parts[3];
                    String itemID = parts[4];
                    int amount = Integer.parseInt(parts[5]);
                    double totalPrice = Double.parseDouble(parts[6]);
                    
                    String orderDetails = "Order ID: " + orderID + "\n" +
                                        "Order Date: " + orderDate + "\n" +
                                        "Customer Name: " + customerName + "\n" +
                                        "Cashier Name: " + cashierName + "\n" +
                                        "Item ID: " + itemID + "\n" +
                                        "Amount: " + amount + "\n" +
                                        "Total Price: " + totalPrice + "\n";
                    orderString.append(orderDetails).append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (orderString.length() == 0) {
            return "Queue is empty. No orders to display.";
        }
        
        return orderString.toString();
    }
}//end of Class
