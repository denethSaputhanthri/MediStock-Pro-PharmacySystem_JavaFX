package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class NavbarController {

    @FXML
    private AnchorPane dashRoot;

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
        System.out.println("hello");
    }

    @FXML
    void btnOrderOnAction(ActionEvent event) {
        System.out.println("hello");
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        System.out.println("hello");
    }

    @FXML
    void btnSalesBOnAction(ActionEvent event) {
        System.out.println("hello");
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) {
        System.out.println("hello");
    }

}
