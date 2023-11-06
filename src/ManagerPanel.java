import javax.swing.*;

public class ManagerPanel {
    public static void main(String[] args) {
        RoofTile rTile = new RoofTile("src/tilesdata.txt");

        String[] options = {
            "SEARCH STOCK",
            "ADD TO STOCK", 
            "ADD NEW STOCK", 
            "REMOVE FROM STOCK", 
            "EMPTY STOCK",
            "UPDATE STOCK",
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
        textArea.setText(rTile.traversalString());
        
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
                    case "ADD TO STOCK":
                        String itemToAdd = JOptionPane.showInputDialog("Enter the item ID to add stock to:");
                        if (rTile.doesExist(itemToAdd)) {
                            String stockAmount = JOptionPane.showInputDialog("Enter the amount to add:");
                            try {
                                int amount = Integer.parseInt(stockAmount);
                                rTile.addToStock(itemToAdd, amount);
                                JOptionPane.showMessageDialog(null, "Item/s has been added to stock.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "Invalid input for stock amount. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case "ADD NEW STOCK":
                        String newItemID = JOptionPane.showInputDialog("Enter the new item ID:");

                        if (rTile.doesExist(newItemID)) {
                            JOptionPane.showMessageDialog(null, "Item with the same ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            String newType = JOptionPane.showInputDialog("Enter the new item type:");
                            String newVariant = JOptionPane.showInputDialog("Enter the new item variant:");
                            String newPrice = JOptionPane.showInputDialog("Enter the new item price:");
                            String newStock = JOptionPane.showInputDialog("Enter the new item stock");

                            try {
                                double price = Double.parseDouble(newPrice);
                                int stock = Integer.parseInt(newStock);
                                rTile.addNewStock(newType, newVariant, newItemID, price, stock);
                                JOptionPane.showMessageDialog(null, "A new item has been added to stock.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "Invalid input for price or stock. Please enter valid numerical values.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;

                    case "REMOVE FROM STOCK":
                        String itemToRemove = JOptionPane.showInputDialog("Enter the item ID to remove stock from:");
                        if (rTile.doesExist(itemToRemove)) {
                            if (!rTile.isEmpty(itemToRemove)) {
                                String removeAmount = JOptionPane.showInputDialog("Enter the amount to remove:");
                                try {
                                    int amount = Integer.parseInt(removeAmount);
                                    if (amount > 0) {
                                        rTile.removeFromStock(itemToRemove, amount);
                                        JOptionPane.showMessageDialog(null, "Item/s have been removed from the stock.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Please enter a positive amount to remove.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(null, "Invalid input for the amount to remove. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "The item is empty. Nothing to remove.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;     

                    case "EMPTY STOCK":
                        String itemToEmpty = JOptionPane.showInputDialog("Enter the item ID to empty stock from:");
                        if (rTile.doesExist(itemToEmpty)) {
                            if (!rTile.isEmpty(itemToEmpty)) {
                                rTile.emptyStock(itemToEmpty);
                                JOptionPane.showMessageDialog(null, "Item is now empty.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Item is already empty.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    

                        case "UPDATE STOCK":
                        String itemToUpdate = JOptionPane.showInputDialog("Enter the item ID to update:");
                        if (rTile.doesExist(itemToUpdate)) {
                            String[] updateOptions = {
                                "Modify Type",
                                "Modify Variant",
                                "Modify Price",
                                "Modify Stock"
                            };
                            JComboBox<String> updateComboBox = new JComboBox<>(updateOptions);
                    
                            int updateResult = JOptionPane.showConfirmDialog(null, updateComboBox, "Select an Update Option", JOptionPane.OK_CANCEL_OPTION);
                    
                            if (updateResult == JOptionPane.OK_OPTION) {
                                String selectedUpdateOption = (String) updateComboBox.getSelectedItem();
                    
                                switch (selectedUpdateOption) {
                                    case "Modify Type":
                                        String newModType = JOptionPane.showInputDialog("Enter the new type:");
                                        rTile.modifyType(itemToUpdate, newModType);
                                        break;
                                    case "Modify Variant":
                                        String newModVariant = JOptionPane.showInputDialog("Enter the new variant:");
                                        rTile.modifyVariant(itemToUpdate, newModVariant);
                                        break;
                                    case "Modify Price":
                                        try {
                                            double newModPrice = Double.parseDouble(JOptionPane.showInputDialog("Enter the new price:"));
                                            rTile.modifyPrice(itemToUpdate, newModPrice);
                                        } catch (NumberFormatException e) {
                                            JOptionPane.showMessageDialog(null, "Invalid price format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                        break;
                                    case "Modify Stock":
                                        try {
                                            int newModStock = Integer.parseInt(JOptionPane.showInputDialog("Enter the new stock:"));
                                            rTile.modifyStock(itemToUpdate, newModStock);
                                        } catch (NumberFormatException e) {
                                            JOptionPane.showMessageDialog(null, "Invalid stock format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                    
                                JOptionPane.showMessageDialog(null, "Item has been updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case "SEARCH STOCK":
                        String itemToSearch = JOptionPane.showInputDialog("Enter the item ID to search:");
                        String itemDetails = rTile.searchAndShowDetails(itemToSearch);
                        JOptionPane.showMessageDialog(null, itemDetails, "Item Details", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    
                    case "EXIT":
                        JOptionPane.showMessageDialog(null, "Program Terminated", "Item Details", JOptionPane.INFORMATION_MESSAGE);
                        break;
                }
            }
            // Update the displayed data after each operation
            rTile.sortStock();
            textArea.setText(rTile.traversalString());
            
        }
    }
}
