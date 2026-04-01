package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Medicine;
import model.Supplier;
import service.ServiceFactory;
import service.custom.SupplierService;
import util.ServiceType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {
    SupplierService supplierService= ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);

    @FXML
    private TableColumn<?, ?> addressColumn;

    @FXML
    private TextArea addressField;

    @FXML
    private TableColumn<?, ?> contactColumn;

    @FXML
    private TextField contactField;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TableView<Supplier> supplierTable;

    @FXML
    void btnAddSupplier(ActionEvent event) {

        String name = nameField.getText();
        String contact = contactField.getText();
        String email = emailField.getText();
        String address = addressField.getText();

        Supplier supplier = new Supplier(null, name, contact, email, address);
        supplierService.addSupplier(supplier);
        automaticallyLoadSupplierTable();

    }

    @FXML
    void btnSendEmailNotification(ActionEvent event) {

    }

    @FXML
    void btnUpdateSupplier(ActionEvent event) {
        String name = nameField.getText();
        String contact = contactField.getText();
        String email = emailField.getText();
        String address = addressField.getText();

        Supplier supplier = new Supplier(null, name, contact, email, address);
        supplierService.updateSupplier(supplier);
        automaticallyLoadSupplierTable();

    }

    @FXML
    void btndeleteSupplier(ActionEvent event) {
        String name = nameField.getText();
        supplierService.deleteSupplier(name);
        automaticallyLoadSupplierTable();
    }

    @FXML
    void checkSupplierAlerts(ActionEvent event) {

    }

    @FXML
    void contactAllSuppliers(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        supplierTable.getSelectionModel().selectedItemProperty().addListener((observableValue, customerDTO, newValue) -> {
            if(newValue!=null){
                nameField.setText(newValue.getName());
                contactField.setText(newValue.getContactNumber());
                emailField.setText(newValue.getEmail());
                addressField.setText(newValue.getAddress());
            }
        });
        automaticallyLoadSupplierTable();
    }

    private void automaticallyLoadSupplierTable() {
        try {
            List<Supplier> all = supplierService.getAll();
            ArrayList<Supplier> supplierArrayList = new ArrayList<>();
            all.forEach(supplier -> {
                supplierArrayList.add(new Supplier(
                        supplier.getId(),
                        supplier.getName(),
                        supplier.getContactNumber(),
                        supplier.getEmail(),
                        supplier.getAddress()
                ));
            });
            supplierTable.setItems(FXCollections.observableArrayList(supplierArrayList));
            supplierArrayList.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
