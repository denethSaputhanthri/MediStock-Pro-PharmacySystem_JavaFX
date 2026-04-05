package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class NavbarController implements Initializable {

    @FXML
    private AnchorPane dashRoot;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDashboard();
    }
    private void loadDashboard() {
        try {
            URL resource = getClass().getResource("/view/dashboard_form.fxml");
            Parent parent = FXMLLoader.load(resource);
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/dashboard_form.fxml");
            assert resource!= null ;
            Parent parent = FXMLLoader.load(resource);
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnMedicineOnAction(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/medicine_form.fxml");
            assert resource!= null ;
            Parent parent = FXMLLoader.load(resource);
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnOrderOnAction(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/orderdetails_form.fxml");
            assert resource!= null ;
            Parent parent = FXMLLoader.load(resource);
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/reports_form.fxml");
            assert resource!= null ;
            Parent parent = FXMLLoader.load(resource);
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSalesBOnAction(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/sale_bil_form.fxml");
            assert resource!= null ;
            Parent parent = FXMLLoader.load(resource);
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) {
        try {
            URL resource = this.getClass().getResource("/view/supplier_form.fxml");
            assert resource!= null ;
            Parent parent = FXMLLoader.load(resource);
            dashRoot.getChildren().clear();
            dashRoot.getChildren().add(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
