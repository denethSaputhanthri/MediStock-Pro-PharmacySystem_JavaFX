package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.CartItem;
import model.Medicine;
import model.OrderDetails;
import model.SaleBill;
import service.ServiceFactory;
import service.custom.MedicineService;
import service.custom.OrderDetailsService;
import service.custom.SaleBillService;
import util.ServiceType;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SaleBillController implements Initializable {
    SaleBillService saleBillService = ServiceFactory.getInstance().getServiceType(ServiceType.SALEBILL);
    OrderDetailsService orderDetailsService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDERDETAILS);
    MedicineService medicineService = ServiceFactory.getInstance().getServiceType(ServiceType.MEDICINE);

    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private ObservableList<SaleBill> salesHistory = FXCollections.observableArrayList();
    private Integer currentBillId = null;
    private static final Double TAX_RATE = 0.0;

    @FXML
    private TableColumn<CartItem, Void> actionColumn;

    @FXML
    private TableView<CartItem> cartTable;

    @FXML
    private TextArea customerAddressField;

    @FXML
    private TableColumn<SaleBill, String> customerColumn;

    @FXML
    private TextField customerContactField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TableColumn<SaleBill, LocalDate> dateColumn;

    @FXML
    private TableColumn<SaleBill, Integer> invoiceIdColumn;

    @FXML
    private ComboBox<String> medicineComboBox;

    @FXML
    private TableColumn<CartItem, String> medicineNameColumn;

    @FXML
    private TableColumn<CartItem, Integer> qtyColumn;

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<SaleBill> salesHistoryTable;

    @FXML
    private TextField searchHistoryField;

    @FXML
    private TableColumn<SaleBill, String> statusColumn;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Label taxLabel;

    @FXML
    private TableColumn<SaleBill, Double> totalAmountColumn;

    @FXML
    private TableColumn<CartItem, Double> totalColumn;

    @FXML
    private Label totalLabel;

    @FXML
    private TableColumn<CartItem, Double> unitPriceColumn;

    @FXML
    private TextField unitPriceField;

    @FXML
    void addToCart(ActionEvent event) {
        try {
            String medicineName = medicineComboBox.getValue();
            if (medicineName == null || medicineName.isEmpty()) {
                showAlert("Validation Error", "Please select a medicine", Alert.AlertType.WARNING);
                return;
            }

            String qtyText = quantityField.getText();
            if (qtyText == null || qtyText.isEmpty()) {
                showAlert("Validation Error", "Please enter quantity", Alert.AlertType.WARNING);
                return;
            }

            String priceText = unitPriceField.getText();
            if (priceText == null || priceText.isEmpty()) {
                showAlert("Validation Error", "Please enter unit price", Alert.AlertType.WARNING);
                return;
            }

            Integer quantity = Integer.parseInt(qtyText);
            Double unitPrice = Double.parseDouble(priceText);

            if (quantity <= 0) {
                showAlert("Validation Error", "Quantity must be greater than 0", Alert.AlertType.WARNING);
                return;
            }

            Medicine medicine = getMedicineByName(medicineName);
            if (medicine == null) {
                showAlert("Error", "Medicine not found", Alert.AlertType.ERROR);
                return;
            }

            if (medicine.getQuantity() < quantity) {
                showAlert("Stock Error", "Insufficient stock! Available: " + medicine.getQuantity(), Alert.AlertType.WARNING);
                return;
            }

            CartItem cartItem = new CartItem(medicine.getId(), medicineName, quantity, unitPrice);
            cartItems.add(cartItem);

            calculateTotals();
            clearMedicineFields();

            showAlert("Success", "Item added to cart", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for quantity and price", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to add item to cart: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void removeFromCart(ActionEvent event) {
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cartItems.remove(selectedItem);
            calculateTotals();
            showAlert("Success", "Item removed from cart", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Selection Error", "Please select an item to remove", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void clearCart(ActionEvent event) {
        if (!cartItems.isEmpty()) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Clear");
            confirmation.setHeaderText("Clear Cart");
            confirmation.setContentText("Are you sure you want to clear the cart?");
            
            if (confirmation.showAndWait().get() == ButtonType.OK) {
                cartItems.clear();
                calculateTotals();
                showAlert("Success", "Cart cleared", Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    void generateInvoice(ActionEvent event) {
        try {
            String customerName = customerNameField.getText();
            if (customerName == null || customerName.trim().isEmpty()) {
                showAlert("Validation Error", "Please enter customer name", Alert.AlertType.WARNING);
                return;
            }

            if (cartItems.isEmpty()) {
                showAlert("Validation Error", "Cart is empty! Add items before checkout", Alert.AlertType.WARNING);
                return;
            }

            Double totalAmount = calculateSubtotal() + calculateTax();

            SaleBill saleBill = new SaleBill(null, LocalDate.now(), totalAmount, customerName);
            boolean billSaved = saleBillService.addSaleBill(saleBill);

            if (!billSaved) {
                showAlert("Error", "Failed to save bill", Alert.AlertType.ERROR);
                return;
            }

            List<SaleBill> allBills = saleBillService.getAll();
            if (allBills.isEmpty()) {
                showAlert("Error", "Failed to retrieve saved bill", Alert.AlertType.ERROR);
                return;
            }

            SaleBill savedBill = allBills.get(allBills.size() - 1);
            currentBillId = savedBill.getBillId();

            List<OrderDetails> orderDetailsList = new ArrayList<>();
            int orderDetailId = 1;

            for (CartItem item : cartItems) {
                OrderDetails orderDetail = new OrderDetails(
                        orderDetailId++,
                        currentBillId,
                        item.getMedicineId(),
                        item.getQuantity(),
                        item.getUnitPrice()
                );
                orderDetailsList.add(orderDetail);

                Medicine medicine = getMedicineById(item.getMedicineId());
                if (medicine != null) {
                    medicine.setQuantity(medicine.getQuantity() - item.getQuantity());
                    medicineService.updateMedicine(medicine);
                }
            }

            boolean detailsSaved = orderDetailsService.addOrderDetailsBatch(orderDetailsList);

            if (detailsSaved) {
                showAlert("Success", "Invoice #" + currentBillId + " generated successfully!\nTotal: $" + 
                         String.format("%.2f", totalAmount), Alert.AlertType.INFORMATION);
                
                clearAllFields();
                refreshSalesHistory(null);
            } else {
                showAlert("Error", "Failed to save order details", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            showAlert("Error", "Failed to generate invoice: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void printInvoice(ActionEvent event) {
        if (currentBillId == null) {
            showAlert("Error", "No invoice to print. Generate an invoice first.", Alert.AlertType.WARNING);
            return;
        }
        printBill(currentBillId);
    }

    @FXML
    void reprintInvoice(ActionEvent event) {
        SaleBill selectedBill = salesHistoryTable.getSelectionModel().getSelectedItem();
        if (selectedBill != null) {
            printBill(selectedBill.getBillId());
        } else {
            showAlert("Selection Error", "Please select an invoice from history to reprint", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void refreshSalesHistory(ActionEvent event) {
        loadSalesHistory();
    }

    @FXML
    void searchSalesHistory(ActionEvent event) {
        String searchText = searchHistoryField.getText();
        if (searchText == null || searchText.trim().isEmpty()) {
            loadSalesHistory();
            return;
        }

        List<SaleBill> allBills = saleBillService.getAll();
        ObservableList<SaleBill> filteredBills = FXCollections.observableArrayList();

        for (SaleBill bill : allBills) {
            if (bill.getCustomerName().toLowerCase().contains(searchText.toLowerCase()) ||
                bill.getBillId().toString().contains(searchText)) {
                filteredBills.add(bill);
            }
        }

        salesHistoryTable.setItems(filteredBills);
    }

    @FXML
    void viewInvoiceDetails(ActionEvent event) {
        SaleBill selectedBill = salesHistoryTable.getSelectionModel().getSelectedItem();
        if (selectedBill != null) {
            showInvoiceDetails(selectedBill);
        } else {
            showAlert("Selection Error", "Please select an invoice to view details", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void checkLowStock(ActionEvent event) {
        List<Medicine> allMedicines = medicineService.getAll();
        StringBuilder lowStockItems = new StringBuilder();

        for (Medicine medicine : allMedicines) {
            if (medicine.getQuantity() < 100) {
                lowStockItems.append(medicine.getName())
                            .append(" - Stock: ")
                            .append(medicine.getQuantity())
                            .append("\n");
            }
        }

        if (lowStockItems.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Low Stock Alert");
            alert.setHeaderText("The following items are low in stock:");
            alert.setContentText(lowStockItems.toString());
            alert.showAndWait();
        } else {
            showAlert("Stock Status", "All items are adequately stocked", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void showDailyReport(ActionEvent event) {
        LocalDate today = LocalDate.now();
        List<SaleBill> allBills = saleBillService.getAll();
        
        double totalSales = 0.0;
        int billCount = 0;

        for (SaleBill bill : allBills) {
            if (bill.getDate().equals(today)) {
                totalSales += bill.getTotalAmount();
                billCount++;
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Daily Sales Report");
        alert.setHeaderText("Sales Report for " + today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        alert.setContentText("Total Bills: " + billCount + "\nTotal Sales: $" + String.format("%.2f", totalSales));
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        cartTable.setItems(cartItems);

        invoiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("billId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        loadMedicineComboBox();
        loadSalesHistory();

        medicineComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Medicine medicine = getMedicineByName(newVal);
                if (medicine != null) {
                    unitPriceField.setText(String.valueOf(medicine.getSelling_price()));
                }
            }
        });
    }

    private void loadMedicineComboBox() {
        List<Medicine> medicines = medicineService.getAll();
        ObservableList<String> medicineNames = FXCollections.observableArrayList();
        
        for (Medicine medicine : medicines) {
            medicineNames.add(medicine.getName());
        }
        
        medicineComboBox.setItems(medicineNames);
    }

    private void loadSalesHistory() {
        List<SaleBill> allBills = saleBillService.getAll();
        salesHistory.clear();
        salesHistory.addAll(allBills);
        salesHistoryTable.setItems(salesHistory);
    }

    private Medicine getMedicineByName(String name) {
        List<Medicine> medicines = medicineService.getAll();
        for (Medicine medicine : medicines) {
            if (medicine.getName().equals(name)) {
                return medicine;
            }
        }
        return null;
    }

    private Medicine getMedicineById(Integer id) {
        List<Medicine> medicines = medicineService.getAll();
        for (Medicine medicine : medicines) {
            if (medicine.getId().equals(id)) {
                return medicine;
            }
        }
        return null;
    }

    private void calculateTotals() {
        double subtotal = calculateSubtotal();
        double tax = calculateTax();
        double total = subtotal + tax;

        subtotalLabel.setText(String.format("$%.2f", subtotal));
        taxLabel.setText(String.format("$%.2f", tax));
        totalLabel.setText(String.format("$%.2f", total));
    }

    private double calculateSubtotal() {
        double subtotal = 0.0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotal();
        }
        return subtotal;
    }

    private double calculateTax() {
        return calculateSubtotal() * TAX_RATE;
    }

    private void clearMedicineFields() {
        medicineComboBox.setValue(null);
        quantityField.clear();
        unitPriceField.clear();
    }

    private void clearAllFields() {
        clearMedicineFields();
        customerNameField.clear();
        customerContactField.clear();
        customerAddressField.clear();
        cartItems.clear();
        calculateTotals();
        currentBillId = null;
    }

    private void showInvoiceDetails(SaleBill bill) {
        List<OrderDetails> orderDetails = orderDetailsService.getOrderDetailsByBillId(bill.getBillId());
        
        StringBuilder details = new StringBuilder();
        details.append("Invoice ID: ").append(bill.getBillId()).append("\n");
        details.append("Date: ").append(bill.getDate()).append("\n");
        details.append("Customer: ").append(bill.getCustomerName()).append("\n");
        details.append("Total Amount: $").append(String.format("%.2f", bill.getTotalAmount())).append("\n\n");
        details.append("Items:\n");
        details.append("--------------------------------------------------\n");

        for (OrderDetails od : orderDetails) {
            Medicine medicine = getMedicineById(od.getMedicineId());
            String medicineName = medicine != null ? medicine.getName() : "Unknown";
            
            details.append(medicineName)
                   .append(" x ").append(od.getQuantity())
                   .append(" @ $").append(String.format("%.2f", od.getUnitPrice()))
                   .append(" = $").append(String.format("%.2f", od.getQuantity() * od.getUnitPrice()))
                   .append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invoice Details");
        alert.setHeaderText("Invoice #" + bill.getBillId());
        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    private void printBill(Integer billId) {
        try {
            SaleBill bill = saleBillService.searchById(billId);
            if (bill == null) {
                showAlert("Error", "Bill not found", Alert.AlertType.ERROR);
                return;
            }

            List<OrderDetails> orderDetails = orderDetailsService.getOrderDetailsByBillId(billId);

            StringBuilder printContent = new StringBuilder();
            printContent.append("========================================\n");
            printContent.append("      PHARMACY INVOICE\n");
            printContent.append("========================================\n\n");
            printContent.append("Invoice #: ").append(bill.getBillId()).append("\n");
            printContent.append("Date: ").append(bill.getDate()).append("\n");
            printContent.append("Customer: ").append(bill.getCustomerName()).append("\n");
            printContent.append("----------------------------------------\n\n");
            printContent.append("Items:\n");

            for (OrderDetails od : orderDetails) {
                Medicine medicine = getMedicineById(od.getMedicineId());
                String medicineName = medicine != null ? medicine.getName() : "Unknown";
                
                printContent.append(medicineName).append("\n");
                printContent.append("  Qty: ").append(od.getQuantity());
                printContent.append(" x $").append(String.format("%.2f", od.getUnitPrice()));
                printContent.append(" = $").append(String.format("%.2f", od.getQuantity() * od.getUnitPrice()));
                printContent.append("\n\n");
            }

            printContent.append("----------------------------------------\n");
            printContent.append("TOTAL: $").append(String.format("%.2f", bill.getTotalAmount())).append("\n");
            printContent.append("========================================\n");
            printContent.append("     Thank you for your business!\n");
            printContent.append("========================================\n");

            Text text = new Text(printContent.toString());
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            
            if (printerJob != null && printerJob.showPrintDialog(cartTable.getScene().getWindow())) {
                boolean success = printerJob.printPage(text);
                if (success) {
                    printerJob.endJob();
                    showAlert("Success", "Invoice printed successfully", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to print invoice", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Printing failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

