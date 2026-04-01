package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import service.ServiceFactory;
import service.custom.SaleBillService;
import util.ServiceType;

import java.net.URL;
import java.util.ResourceBundle;

public class SaleBillController implements Initializable {
    SaleBillService saleBillService = ServiceFactory.getInstance().getServiceType(ServiceType.SALEBILL);


    @FXML
    private TableColumn<?, ?> actionColumn;

    @FXML
    private TableView<?> cartTable;

    @FXML
    private TextArea customerAddressField;

    @FXML
    private TableColumn<?, ?> customerColumn;

    @FXML
    private TextField customerContactField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> invoiceIdColumn;

    @FXML
    private ComboBox<?> medicineComboBox;

    @FXML
    private TableColumn<?, ?> medicineNameColumn;

    @FXML
    private TableColumn<?, ?> qtyColumn;

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<?> salesHistoryTable;

    @FXML
    private TextField searchHistoryField;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Label taxLabel;

    @FXML
    private TableColumn<?, ?> totalAmountColumn;

    @FXML
    private TableColumn<?, ?> totalColumn;

    @FXML
    private Label totalLabel;

    @FXML
    private TableColumn<?, ?> unitPriceColumn;

    @FXML
    private TextField unitPriceField;

    @FXML
    void addToCart(ActionEvent event) {

    }

    @FXML
    void checkLowStock(ActionEvent event) {

    }

    @FXML
    void clearCart(ActionEvent event) {

    }

    @FXML
    void generateInvoice(ActionEvent event) {

    }

    @FXML
    void printInvoice(ActionEvent event) {

    }

    @FXML
    void refreshSalesHistory(ActionEvent event) {

    }

    @FXML
    void removeFromCart(ActionEvent event) {

    }

    @FXML
    void reprintInvoice(ActionEvent event) {

    }

    @FXML
    void searchSalesHistory(ActionEvent event) {

    }

    @FXML
    void showDailyReport(ActionEvent event) {

    }

    @FXML
    void viewInvoiceDetails(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

