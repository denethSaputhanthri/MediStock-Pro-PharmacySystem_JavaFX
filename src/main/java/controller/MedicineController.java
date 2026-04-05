package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Medicine;
import service.ServiceFactory;
import service.custom.MedicineService;
import util.ServiceType;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MedicineController implements Initializable {
    MedicineService medicineService= ServiceFactory.getInstance().getServiceType(ServiceType.MEDICINE);

    @FXML
    private TableColumn<?, ?> colBrand;

    @FXML
    private TableColumn<?, ?> colExpiryDate;

    @FXML
    private TableColumn<?, ?> colMedicineId;

    @FXML
    private TableColumn<?, ?> colMedicineName;

    @FXML
    private TableColumn<?, ?> colPurchasePrice;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colSellingPrice;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private DatePicker expiryDatePicker;

    @FXML
    private TableView<Medicine> tblmedicineTable;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtMedicineName;

    @FXML
    private TextField txtPurchasePrice;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtSellingPrice;

    @FXML
    private TextField txtSupplierId;

    @FXML
    void btnAddMedicine(ActionEvent event) {
        String medicineName = txtMedicineName.getText();
        String brand = txtBrand.getText();
        String supplierId = txtSupplierId.getText();
        Double purchasePrice =Double.valueOf(txtPurchasePrice.getText());
        Double sellingPrice = Double.valueOf(txtSellingPrice.getText());
        Integer quantity = Integer.valueOf(txtQuantity.getText());
        LocalDate expiryDate = LocalDate.parse(expiryDatePicker.getValue().toString());

        Medicine medicine=new Medicine(null,medicineName,brand,expiryDate,quantity,purchasePrice,sellingPrice,supplierId);

        medicineService.addMedicine(medicine);
        automaticallyLoadMedicineTable();
    }


    @FXML
    void btnDeleteMedicine(ActionEvent event) {
            String medicineId = txtMedicineName.getText();
            medicineService.deleteMedicine(medicineId);
            automaticallyLoadMedicineTable();

    }

    @FXML
    void btnUpdateMedicine(ActionEvent event) {
        String medicineName = txtMedicineName.getText();
        String brand = txtBrand.getText();
        String supplierId = txtSupplierId.getText();
        Double purchasePrice =Double.valueOf(txtPurchasePrice.getText());
        Double sellingPrice = Double.valueOf(txtSellingPrice.getText());
        Integer quantity = Integer.valueOf(txtQuantity.getText());
        LocalDate expiryDate = LocalDate.parse(expiryDatePicker.getValue().toString());

        Medicine medicine=new Medicine(null,medicineName,brand,expiryDate,quantity,purchasePrice,sellingPrice,supplierId);

        medicineService.updateMedicine(medicine);
        automaticallyLoadMedicineTable();
    }

    @FXML
    void checkAlerts(ActionEvent event) {

    }

    @FXML
    void createSaleInvoice(ActionEvent event) {

    }

    @FXML
    void showSalesInvoice(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colMedicineId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMedicineName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiry_date"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchase_price"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplier_id"));

        tblmedicineTable.getSelectionModel().selectedItemProperty().addListener((observableValue, customerDTO, newValue) -> {
            if(newValue!=null){
                txtMedicineName.setText(newValue.getName());
                txtBrand.setText(newValue.getBrand());
                txtSupplierId.setText(newValue.getSupplier_id());
                txtPurchasePrice.setText(String.valueOf(newValue.getPurchase_price()));
                txtSellingPrice.setText(String.valueOf(newValue.getSelling_price()));
                txtQuantity.setText(String.valueOf(newValue.getQuantity()));
                expiryDatePicker.setValue(newValue.getExpiry_date());
                
            }
        });
        automaticallyLoadMedicineTable();
    }

    private void automaticallyLoadMedicineTable() {
        try {
            List<Medicine> all = medicineService.getAll();
            ArrayList<Medicine> medicineArrayList = new ArrayList<>();
            all.forEach(medicine -> {
                medicineArrayList.add(new Medicine(
                        medicine.getId(),
                        medicine.getName(),
                        medicine.getBrand(),
                        medicine.getExpiry_date(),
                        medicine.getQuantity(),
                        medicine.getPurchase_price(),
                        medicine.getSelling_price(),
                        medicine.getSupplier_id()
                ));
            });
            tblmedicineTable.setItems(FXCollections.observableArrayList(medicineArrayList));
            medicineArrayList.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
