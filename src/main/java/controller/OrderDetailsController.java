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
import model.Medicine;
import model.OrderDetails;
import model.SaleBill;
import service.ServiceFactory;
import service.custom.MedicineService;
import service.custom.OrderDetailsService;
import service.custom.SaleBillService;
import util.ServiceType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderDetailsController implements Initializable {
    
    OrderDetailsService orderDetailsService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDERDETAILS);
    SaleBillService saleBillService = ServiceFactory.getInstance().getServiceType(ServiceType.SALEBILL);
    MedicineService medicineService = ServiceFactory.getInstance().getServiceType(ServiceType.MEDICINE);

    private ObservableList<OrderDetails> orderDetailsObservableList = FXCollections.observableArrayList();
    private ObservableList<OrderDetails> orderItemsList = FXCollections.observableArrayList();

    // Already present and correct:

    @FXML
    private TextField itemQuantityField;
    // not txtQuantity
    @FXML
    private TextField itemPriceField;
    // not txtUnitPrice

    @FXML
    private TableView<?> ordersTable;
    // for the main orders list

    @FXML
    private TableView<OrderDetails> orderDetailsTable;

    @FXML
    private TableColumn<OrderDetails, Integer> colOrderDetailId;

    @FXML
    private TableColumn<OrderDetails, Integer> colBillId;

    @FXML
    private TableColumn<OrderDetails, Integer> colMedicineId;

    @FXML
    private TableColumn<OrderDetails, Integer> colQuantity;

    @FXML
    private TableColumn<OrderDetails, Double> colUnitPrice;

    @FXML
    private TableColumn<OrderDetails, Double> colLineTotal;

    @FXML
    private TextField txtOrderDetailId;

    @FXML
    private TextField txtBillId;

    @FXML
    private ComboBox<String> cmbMedicine;

    @FXML
    private ComboBox<String> productCmb;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private TextField searchField;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private TableView<OrderDetails> orderItemsTable;

    @FXML
    private TableColumn<OrderDetails, String> itemMedicineColumn;

    @FXML
    private TableColumn<OrderDetails, Integer> itemQuantityColumn;

    @FXML
    private TableColumn<OrderDetails, Double> itemPriceColumn;

    @FXML
    private TableColumn<OrderDetails, Double> itemTotalColumn;

    public void createOrder(ActionEvent actionEvent) {
        try {
            if (orderItemsList.isEmpty()) {
                showAlert("Validation Error", "Please add at least one item to create an order", Alert.AlertType.WARNING);
                return;
            }

            String customerName = "Walk-in Customer";
            Double totalAmount = calculateTotalFromItems(orderItemsList);

            SaleBill saleBill = new SaleBill(null, LocalDate.now(), totalAmount, customerName,"");
            boolean billSaved = saleBillService.addSaleBill(saleBill);

            if (!billSaved) {
                showAlert("Error", "Failed to create order", Alert.AlertType.ERROR);
                return;
            }

            List<SaleBill> allBills = saleBillService.getAll();
            SaleBill savedBill = allBills.get(allBills.size() - 1);
            Integer newBillId = savedBill.getBillId();

            List<OrderDetails> detailsToSave = new ArrayList<>();
            for (OrderDetails item : orderItemsList) {
                item.setBillId(newBillId);
                detailsToSave.add(item);
            }

            boolean detailsSaved = orderDetailsService.addOrderDetailsBatch(detailsToSave);

            if (detailsSaved) {
                showAlert("Success", "Order created successfully! Bill ID: " + newBillId, Alert.AlertType.INFORMATION);
                orderItemsList.clear();
                clearFields();
                loadAllOrderDetails();
                updateStatistics();
            } else {
                showAlert("Error", "Failed to save order details", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            showAlert("Error", "Failed to create order: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void addOrderItem(ActionEvent actionEvent) {
        try {
            String medicineName = productCmb.getValue();
            if (medicineName == null || medicineName.isEmpty()) {
                showAlert("Validation Error", "Please select a medicine", Alert.AlertType.WARNING);
                return;
            }

            String qtyText = itemQuantityField.getText();
            if (qtyText == null || qtyText.isEmpty()) {
                showAlert("Validation Error", "Please enter quantity", Alert.AlertType.WARNING);
                return;
            }

            String priceText = itemPriceField.getText();
            if (priceText == null || priceText.isEmpty()) {
                showAlert("Validation Error", "Please enter unit price", Alert.AlertType.WARNING);
                return;
            }

            Integer quantity = Integer.parseInt(itemQuantityField.getText());
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

            int nextId = orderItemsList.size() + 1;
            OrderDetails orderDetail = new OrderDetails(nextId, null, medicine.getId(), quantity, unitPrice);
            orderItemsList.add(orderDetail);

            clearFields();
            showAlert("Success", "Item added to order", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to add item: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void removeOrderItem(ActionEvent actionEvent) {
        OrderDetails selectedItem = orderItemsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            orderItemsList.remove(selectedItem);
            showAlert("Success", "Item removed from order", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Selection Error", "Please select an item to remove", Alert.AlertType.WARNING);
        }
    }

    public void updateOrder(ActionEvent actionEvent) {
        try {
            OrderDetails selectedOrder = orderDetailsTable.getSelectionModel().getSelectedItem();
            if (selectedOrder == null) {
                showAlert("Selection Error", "Please select an order to update", Alert.AlertType.WARNING);
                return;
            }

            String qtyText = txtQuantity.getText();
            String priceText = txtUnitPrice.getText();

            if (qtyText == null || qtyText.isEmpty() || priceText == null || priceText.isEmpty()) {
                showAlert("Validation Error", "Please fill in all fields", Alert.AlertType.WARNING);
                return;
            }

            Integer quantity = Integer.parseInt(qtyText);
            Double unitPrice = Double.parseDouble(priceText);

            selectedOrder.setQuantity(quantity);
            selectedOrder.setUnitPrice(unitPrice);

            boolean updated = orderDetailsService.updateOrderDetail(selectedOrder);

            if (updated) {
                showAlert("Success", "Order updated successfully", Alert.AlertType.INFORMATION);
                loadAllOrderDetails();
                clearFields();
            } else {
                showAlert("Error", "Failed to update order", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to update order: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void deleteOrder(ActionEvent actionEvent) {
        OrderDetails selectedOrder = orderDetailsTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Selection Error", "Please select an order to delete", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Delete Order Detail");
        confirmation.setContentText("Are you sure you want to delete this order detail?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            boolean deleted = orderDetailsService.deleteOrderDetail(selectedOrder.getOrderDetailId());

            if (deleted) {
                showAlert("Success", "Order detail deleted successfully", Alert.AlertType.INFORMATION);
                loadAllOrderDetails();
                clearFields();
                updateStatistics();
            } else {
                showAlert("Error", "Failed to delete order detail", Alert.AlertType.ERROR);
            }
        }
    }

    public void updateOrderStatus(ActionEvent actionEvent) {
        OrderDetails selectedOrder = orderDetailsTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            showOrderStatus(selectedOrder);
        } else {
            showAlert("Selection Error", "Please select an order to view status", Alert.AlertType.WARNING);
        }
    }

    public void printOrder(ActionEvent actionEvent) {
        OrderDetails selectedOrder = orderDetailsTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Selection Error", "Please select an order to print", Alert.AlertType.WARNING);
            return;
        }

        try {
            List<OrderDetails> billItems = orderDetailsService.getOrderDetailsByBillId(selectedOrder.getBillId());
            SaleBill bill = saleBillService.searchById(selectedOrder.getBillId());

            if (bill == null) {
                showAlert("Error", "Bill not found", Alert.AlertType.ERROR);
                return;
            }

            StringBuilder printContent = new StringBuilder();
            printContent.append("========================================\n");
            printContent.append("      ORDER DETAILS\n");
            printContent.append("========================================\n\n");
            printContent.append("Bill ID: ").append(bill.getBillId()).append("\n");
            printContent.append("Date: ").append(bill.getDate()).append("\n");
            printContent.append("Customer: ").append(bill.getCustomerName()).append("\n");
            printContent.append("----------------------------------------\n\n");

            for (OrderDetails od : billItems) {
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

            Text text = new Text(printContent.toString());
            PrinterJob printerJob = PrinterJob.createPrinterJob();

            if (printerJob != null && printerJob.showPrintDialog(orderDetailsTable.getScene().getWindow())) {
                boolean success = printerJob.printPage(text);
                if (success) {
                    printerJob.endJob();
                    showAlert("Success", "Order printed successfully", Alert.AlertType.INFORMATION);
                }
            }

        } catch (Exception e) {
            showAlert("Error", "Printing failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void exportOrders(ActionEvent actionEvent) {
        try {
            String fileName = "orders_export_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv";
            File file = new File(fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write("Order Detail ID,Bill ID,Medicine ID,Quantity,Unit Price,Total\n");

            for (OrderDetails od : orderDetailsObservableList) {
                double total = od.getQuantity() * od.getUnitPrice();
                writer.write(od.getOrderDetailId() + "," +
                           od.getBillId() + "," +
                           od.getMedicineId() + "," +
                           od.getQuantity() + "," +
                           od.getUnitPrice() + "," +
                           total + "\n");
            }

            writer.close();
            showAlert("Success", "Orders exported to " + fileName, Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Error", "Export failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (colOrderDetailId != null) {
            colOrderDetailId.setCellValueFactory(new PropertyValueFactory<>("orderDetailId"));
        }
        if (colBillId != null) {
            colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        }
        if (colMedicineId != null) {
            colMedicineId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        }
        if (colQuantity != null) {
            colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        }
        if (colUnitPrice != null) {
            colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        }
        if (colLineTotal != null) {
            colLineTotal.setCellValueFactory(cellData -> {
                OrderDetails od = cellData.getValue();
                double total = od.getQuantity() * od.getUnitPrice();
                return new javafx.beans.property.SimpleDoubleProperty(total).asObject();
            });
        }

        if (itemMedicineColumn != null) {
            itemMedicineColumn.setCellValueFactory(cellData -> {
                Medicine medicine = getMedicineById(cellData.getValue().getMedicineId());
                String name = medicine != null ? medicine.getName() : "Unknown";
                return new javafx.beans.property.SimpleStringProperty(name);
            });
        }
        if (itemQuantityColumn != null) {
            itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        }
        if (itemPriceColumn != null) {
            itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        }
        if (itemTotalColumn != null) {
            itemTotalColumn.setCellValueFactory(cellData -> {
                OrderDetails od = cellData.getValue();
                double total = od.getQuantity() * od.getUnitPrice();
                return new javafx.beans.property.SimpleDoubleProperty(total).asObject();
            });
        }

        if (orderDetailsTable != null) {
            orderDetailsTable.setItems(orderDetailsObservableList);
            orderDetailsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    populateFields(newVal);
                }
            });
        }

        if (orderItemsTable != null) {
            orderItemsTable.setItems(orderItemsList);
        }

        loadMedicineComboBox();
        loadAllOrderDetails();
        updateStatistics();

        if (cmbMedicine != null) {
            cmbMedicine.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    Medicine medicine = getMedicineByName(newVal);
                    if (medicine != null && txtUnitPrice != null) {
                        txtUnitPrice.setText(String.valueOf(medicine.getSelling_price()));
                    }
                }
            });
        }
    }

    private void loadMedicineComboBox() {

        List<Medicine> medicines = medicineService.getAll();
        ObservableList<String> medicineNames = FXCollections.observableArrayList();
        for (Medicine medicine : medicines) {
            medicineNames.add(medicine.getName());
        }
        productCmb.setItems(medicineNames);

    }

    private void loadAllOrderDetails() {
        if (orderDetailsTable == null) return;
        
        List<OrderDetails> allOrderDetails = orderDetailsService.getAll();
        orderDetailsObservableList.clear();
        orderDetailsObservableList.addAll(allOrderDetails);
    }

    private void populateFields(OrderDetails orderDetail) {
        if (txtOrderDetailId != null) {
            txtOrderDetailId.setText(String.valueOf(orderDetail.getOrderDetailId()));
        }
        if (txtBillId != null) {
            txtBillId.setText(String.valueOf(orderDetail.getBillId()));
        }
        if (txtQuantity != null) {
            txtQuantity.setText(String.valueOf(orderDetail.getQuantity()));
        }
        if (txtUnitPrice != null) {
            txtUnitPrice.setText(String.valueOf(orderDetail.getUnitPrice()));
        }

        Medicine medicine = getMedicineById(orderDetail.getMedicineId());
        if (medicine != null && cmbMedicine != null) {
            cmbMedicine.setValue(medicine.getName());
        }
    }

    private void clearFields() {
        if (txtOrderDetailId != null) txtOrderDetailId.clear();
        if (txtBillId != null) txtBillId.clear();
        if (txtQuantity != null) txtQuantity.clear();
        if (txtUnitPrice != null) txtUnitPrice.clear();
        if (cmbMedicine != null) cmbMedicine.setValue(null);
    }

    private void updateStatistics() {
        if (totalOrdersLabel != null) {
            totalOrdersLabel.setText(String.valueOf(orderDetailsObservableList.size()));
        }

        if (totalAmountLabel != null) {
            double total = 0.0;
            for (OrderDetails od : orderDetailsObservableList) {
                total += od.getQuantity() * od.getUnitPrice();
            }
            totalAmountLabel.setText(String.format("$%.2f", total));
        }
    }

    private double calculateTotalFromItems(List<OrderDetails> items) {
        double total = 0.0;
        for (OrderDetails item : items) {
            total += item.getQuantity() * item.getUnitPrice();
        }
        return total;
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

    private void showOrderStatus(OrderDetails orderDetail) {
        SaleBill bill = saleBillService.searchById(orderDetail.getBillId());
        Medicine medicine = getMedicineById(orderDetail.getMedicineId());

        StringBuilder status = new StringBuilder();
        status.append("Order Detail ID: ").append(orderDetail.getOrderDetailId()).append("\n");
        status.append("Bill ID: ").append(orderDetail.getBillId()).append("\n");
        
        if (bill != null) {
            status.append("Customer: ").append(bill.getCustomerName()).append("\n");
            status.append("Date: ").append(bill.getDate()).append("\n");
        }
        
        if (medicine != null) {
            status.append("Medicine: ").append(medicine.getName()).append("\n");
        }
        
        status.append("Quantity: ").append(orderDetail.getQuantity()).append("\n");
        status.append("Unit Price: $").append(String.format("%.2f", orderDetail.getUnitPrice())).append("\n");
        status.append("Line Total: $").append(String.format("%.2f", orderDetail.getQuantity() * orderDetail.getUnitPrice()));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Status");
        alert.setHeaderText("Order Detail #" + orderDetail.getOrderDetailId());
        alert.setContentText(status.toString());
        alert.showAndWait();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
