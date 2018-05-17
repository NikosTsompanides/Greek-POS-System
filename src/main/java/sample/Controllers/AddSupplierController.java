package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.mongodb.morphia.Datastore;
import sample.Context;
import sample.Main;
import sample.Models.Supplier;

import java.util.ArrayList;

public class AddSupplierController {

    @FXML
    private GridPane addSupplierGrid;
    private TextField nameTextField, VatNumberTextField, balanceTextField;

    private TableView suppliersTable;

    public void initialize() {
        suppliersTable = Context.getInstance().suppliersTableView();
        addSupplierGrid.setAlignment(Pos.CENTER);
        addSupplierGrid.setHgap(10);
        addSupplierGrid.setVgap(10);
        addSupplierGrid.setPadding(new Insets(25, 25, 25, 25));

        createLabelsOnAddSuppliersGrid();
        createTextFieldsOnAddSuppliersGrid();
        createAddSupplierButton();
    }

    public void createLabelsOnAddSuppliersGrid() {
        Label productTitle = new Label("Όνομα :");
        addSupplierGrid.add(productTitle, 0, 0);

        Label productBarcode = new Label("Α.Φ.Μ :");
        addSupplierGrid.add(productBarcode, 0, 1);

        Label productInitialPrice = new Label("Πιστωτικό Υπόλοιπο :");
        addSupplierGrid.add(productInitialPrice, 0, 2);

    }

    public void createTextFieldsOnAddSuppliersGrid() {
        nameTextField = new TextField();
        addSupplierGrid.add(nameTextField, 2, 0);

        VatNumberTextField = new TextField();
        addSupplierGrid.add(VatNumberTextField, 2, 1);

        balanceTextField = new TextField();
        addSupplierGrid.add(balanceTextField, 2, 2);
    }


    public void createAddSupplierButton() {

        Button addSupplierButton = new Button("Προσθήκη Προμηθευτή");
        addSupplierGrid.add(addSupplierButton, 1, 3);
        addSupplierButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isNotEmpty(nameTextField) && isNotEmpty(VatNumberTextField)) {
                    String name = nameTextField.getText();
                    Datastore datastore = Main.databaseHelper.getDatastore();
                    ArrayList<Long> vatNumbers = Main.databaseHelper.getVatNumbers();

                    if (isVATCorrect(VatNumberTextField)) {
                        long vatNumber = Long.parseLong(VatNumberTextField.getText());

                        if (!vatNumbers.contains(vatNumber)) {
                            if (isNotEmpty(balanceTextField)) {
                                Double balance = Double.parseDouble(balanceTextField.getText());
                                Supplier supplier = new Supplier(name, balance, vatNumber);

                                datastore.save(supplier);

                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                                alert.setTitle("Προσθήκη Προμηθευτή");
                                alert.setHeaderText("Επιτυχής προσθήκη προμηθευτή");

                                alert.showAndWait();

                                suppliersTable.getItems().clear();
                                ObservableList<Supplier> suppliers = FXCollections.observableArrayList(Main.databaseHelper.getSuppliersList());
                                suppliersTable.setItems(suppliers);
                            } else {
                                Supplier supplier = new Supplier(name, vatNumber);
                                datastore.save(supplier);

                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                                alert.setTitle("Προσθήκη Προμηθευτή");
                                alert.setHeaderText("Επιτυχής προσθήκη προμηθευτή");

                                alert.showAndWait();
                                suppliersTable.getItems().clear();
                                ObservableList<Supplier> suppliers = FXCollections.observableArrayList(Main.databaseHelper.getSuppliersList());
                                suppliersTable.setItems(suppliers);

                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Προσθήκη Προμηθευτή");
                            alert.setHeaderText("Το ΑΦΜ υπαρχει ήδη");

                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                        alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προμηθευτή");
                        alert.setHeaderText("Η μορφή του ΑΦΜ είναι λανθασμένη");

                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προμηθευτή");
                    alert.setHeaderText("Το Όνομα και το ΑΦΜ είναι απαραίτητα για την προσθήκη προμηθευτη");

                    alert.showAndWait();
                }
            }
        });
    }


    public boolean isNumber(TextField textField) {
        try {
            double d = Double.parseDouble(textField.getText());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean isNotEmpty(TextField textField) {
        return !textField.getText().equals("");
    }

    public boolean isVATCorrect(TextField textField) {
        return (isNumber(textField) && textField.getText().length() == 9);
    }
}
