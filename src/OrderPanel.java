import javax.swing.*;

public class OrderPanel {
    public static void main(String[] args) {
        LoginPanel login = new LoginPanel();
        Order order = new Order("src//orders.txt", login);

        String[] options = {
            "ADD ORDER",
            "CONTINUE ORDER",
            "SHIP ORDER",
            "SEARCH ORDER",
            "EXIT"
        };

        // Create a JTextArea to display the stock information
        JTextArea textArea = new JTextArea(10, 40);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setCaretPosition(0);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Display initial data here
        textArea.setText(order.printOrdersString());

        JComboBox<String> comboBox = new JComboBox<>(options);
        JPanel panel = new JPanel();
        panel.add(scrollPane);
        panel.add(comboBox);

        int result;
        String selectedOption = "";

        while (!selectedOption.equals("EXIT")) {
            result = JOptionPane.showConfirmDialog(null, panel, "Select an Option", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(null, "Program Terminated", "Item Details", JOptionPane.INFORMATION_MESSAGE);
                break;
            } else if (result == JOptionPane.OK_OPTION) {
                selectedOption = (String) comboBox.getSelectedItem();

                switch (selectedOption) {
                    case "ADD ORDER":
                    if (order.isFull()) {
                        JOptionPane.showMessageDialog(null, "Queue is full. Cannot add more orders.", "Queue Full", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                    try {
                        String customerName = JOptionPane.showInputDialog("Enter Customer Name:");
                        String itemID = JOptionPane.showInputDialog("Enter Item ID");
                        if (itemID == null) {
                            break;
                        }

                        if (!order.doesExistItemID(itemID)) {
                            JOptionPane.showMessageDialog(null, "Item does not Exist.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        if (order.doesHaveNoStock(itemID)) {
                            JOptionPane.showMessageDialog(null, "Item has no more Stock Left.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        int amount = Integer.parseInt(JOptionPane.showInputDialog("Enter Amount:"));
                        if (order.howManyStockLeft(itemID) < amount) {
                            JOptionPane.showMessageDialog(null, "Item does not have enough Stock. \nStock Remaining: " + order.howManyStockLeft(itemID), "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        if (customerName != null && itemID != null) {
                            String orderID = order.getOrderID();
                            String orderDate = order.getCurrentDate();
                            String cashierName = order.getCashierName();

                            order.continueOrder(orderID, orderDate, customerName, cashierName, itemID, amount);

                            JOptionPane.showMessageDialog(null, "Order Added into the Queue.", "Ordered Successfully", JOptionPane.PLAIN_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid amount entered. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                    case "SHIP ORDER":
                        if (order.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Queue is Empty", "Queue Empty", JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                        order.shipOrder();
                        break;
                    case "CONTINUE ORDER":
                        if (order.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No Previous Order Entered.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        } else {
                            // Prompt for order ID first
                            String orderID = JOptionPane.showInputDialog("Enter Order ID: ");
                            if (orderID == null) {
                                break; // User canceled
                            }
                            if (!order.searchOrder(orderID).equals("Order not found.")) {
                                String itemID = JOptionPane.showInputDialog("Enter Item ID");
                                if (itemID == null) {
                                    break;
                                }

                                if (!order.doesExistItemID(itemID)) {
                                    JOptionPane.showMessageDialog(null, "Item does not Exist.", "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                }

                                if (order.doesHaveNoStock(itemID)) {
                                    JOptionPane.showMessageDialog(null, "Item has no more Stock Left.", "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                }

                                try {
                                    int amount = Integer.parseInt(JOptionPane.showInputDialog("Enter Amount:"));
                                    if (order.howManyStockLeft(itemID) < amount) {
                                        JOptionPane.showMessageDialog(null, "Item does not have enough Stock. \nStock Remaining: " + order.howManyStockLeft(itemID), "Error", JOptionPane.ERROR_MESSAGE);
                                        break;
                                    }

                                    // Get customer name and cashier name
                                    String customerName = order.getRecentCustomerName(orderID);
                                    String cashierName = order.getRecentCashierName(orderID);

                                    order.addOrder(orderID, order.getCurrentDate(), customerName, cashierName, itemID, amount);

                                    JOptionPane.showMessageDialog(null, "Order Added into the Queue.", "Ordered Successfully", JOptionPane.PLAIN_MESSAGE);
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(null, "Invalid amount entered. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Order not found.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;                     
                    case "SEARCH ORDER":
                        if (order.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Queue is Empty", "Queue Empty", JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                        String searchOrderID = JOptionPane.showInputDialog("Enter the Order ID to search for:");
                        if (searchOrderID != null && !searchOrderID.isEmpty()) {
                            String searchResults = order.searchOrder(searchOrderID);
                            if (searchResults != null && !searchResults.isEmpty()) {
                                JOptionPane.showMessageDialog(null, searchResults, "Search Results", JOptionPane.PLAIN_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "EXIT":
                        JOptionPane.showMessageDialog(null, "Program Terminated", "Item Details", JOptionPane.INFORMATION_MESSAGE);
                        break;
                }

                textArea.setText(order.printOrdersString());
            }
        }
    }
}
