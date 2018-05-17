package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import sample.DatabaseHelper;
import sample.Models.Product;

import java.util.ArrayList;

public class AddProductController {

    @FXML
    private GridPane addProductFormGrid;
    TextField productTitleTextField, productBarcodeTextField, productInitialPriceTextField, productProfitRateTextField, productMinimumQuantityTextField, productCurrentQuantityTextField;
    ChoiceBox<String> taxRateChoiceBox, categoryChoiceBox;

    public void initialize() {


        addProductFormGrid.setAlignment(Pos.CENTER);
        addProductFormGrid.setHgap(10);
        addProductFormGrid.setVgap(10);
        addProductFormGrid.setPadding(new Insets(25, 25, 25, 25));
        createLabelsOnAddProductForm();
        createTextFieldsOnProductForm();
        createChoiceBoxesOnAddProductForm();

        Button addProductButton = new Button("Εισαγωγή Προϊόντος");
        addProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String title;
                String category;
                int taxRate;
                int profitRate;
                int minQuantity;
                int currentQuantity;
                long barcode;
                double initialPrice;

                if (allFieldsAreNotEmpty()) {
                    title = productTitleTextField.getText();

                    if (isNumber(productBarcodeTextField) && (productBarcodeTextField.getText().length() > 11 && productBarcodeTextField.getText().length() < 14)) {
                        barcode = Long.parseLong(productBarcodeTextField.getText());
                        if (isNumber(productInitialPriceTextField)) {
                            if (hasComma(productInitialPriceTextField))
                                initialPrice = Double.parseDouble(changeCommaToDot(productInitialPriceTextField));
                            else
                                initialPrice = Double.parseDouble(productInitialPriceTextField.getText());

                            if (isNumber(productProfitRateTextField)) {
                                profitRate = Integer.parseInt(productProfitRateTextField.getText());

                                taxRate = Integer.parseInt(taxRateChoiceBox.getValue());
                                double taxedPrice = initialPrice + initialPrice * ((double) taxRate / 100.0);
                                double sellingPrice = taxedPrice + (taxedPrice * ((double) profitRate / 100.0));

                                if (isNumber(productMinimumQuantityTextField)) {
                                    minQuantity = Integer.parseInt(productMinimumQuantityTextField.getText());

                                    if (isNumber(productCurrentQuantityTextField)) {
                                        currentQuantity = Integer.parseInt(productCurrentQuantityTextField.getText());

                                        category = String.valueOf(categoryChoiceBox.getValue());

                                        DatabaseHelper databaseHelper = new DatabaseHelper();
                                        ArrayList<Long> barcodes = databaseHelper.getBarcodes();
                                        if (!barcodes.contains(barcode)) {
                                            Product p = new Product(title, barcode, category, initialPrice, sellingPrice, taxRate, profitRate, minQuantity, currentQuantity);
                                            databaseHelper.getDatastore().save(p);

                                            clearForm();
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
                                            alert.setTitle("Επιτυχής Προοσθήκη Προϊόντος");
                                            alert.setHeaderText("Το Προϊόν προστέθηκε με επιτυχία στη βάση δεδομένων");
                                            alert.showAndWait();
                                        } else {
                                            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
                                            alert.setTitle("Ανεπιτυχής Προοσθήκη Προϊόντος");
                                            alert.setHeaderText("Το Προϊόν υπάρχει ήδη!");
                                            alert.showAndWait();
                                        }


                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR, "Η τρέχουσα ποσότητα πρέπει να είναι ακέραιος αριθμος!", ButtonType.OK);
                                        alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προϊόντος");
                                        alert.setHeaderText("Λανθασμένη μορφή τρέχουσας ποσότητας");

                                        alert.showAndWait();
                                    }

                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR, "Η ελάχιστη ποσότητα πρέπει να είναι ακέραιος αριθμος!", ButtonType.OK);
                                    alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προϊόντος");
                                    alert.setHeaderText("Λανθασμένη μορφή ελάχιστης ποσότητας");
                                    alert.showAndWait();
                                }

                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Το ποσοστό κέρδους πρέπει να είναι ακέραιος αριθμός!", ButtonType.OK);
                                alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προϊόντος");
                                alert.setHeaderText("Λανθασμένη μορφή ποσοστού κέρδους");
                                alert.showAndWait();
                            }

                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Η αρχική τιμή θα πρέπει να είναι πραγματικός αριθμός χωρισμένος με κόμμα!", ButtonType.OK);
                            alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προϊόντος");
                            alert.setHeaderText("Λανθασμένη μορφή αρχικής τιμής ");
                            alert.showAndWait();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Το barcode πρέπει να είναι αριθμός μήκους 12 ή 13 ψηφίων!", ButtonType.OK);
                        alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προϊόντος");
                        alert.setHeaderText("Λανθασμένη μορφή Barcode ");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Όλα τα πεδία είναι υποχρεωτικά!", ButtonType.OK);
                    alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προϊόντος");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε όλα τα πεδία ");
                    alert.showAndWait();
                }
            }

        });

        addProductFormGrid.add(addProductButton, 1, 9);

    }

    public void createLabelsOnAddProductForm() {

        Label productTitle = new Label("Τίτλος :");
        addProductFormGrid.add(productTitle, 0, 0);

        Label productBarcode = new Label("Barcode :");
        addProductFormGrid.add(productBarcode, 0, 1);

        Label productInitialPrice = new Label("Αρχική Τιμή :");
        addProductFormGrid.add(productInitialPrice, 0, 2);

        Label productTaxRate = new Label("Φ.Π.Α :");
        addProductFormGrid.add(productTaxRate, 0, 3);

        Label productProfitRate = new Label("Ποσοστό Κέρδους :");
        addProductFormGrid.add(productProfitRate, 0, 4);

        Label productMinimumQuantity = new Label("Ελάχιστη Ποσότητα :");
        addProductFormGrid.add(productMinimumQuantity, 0, 5);

        Label productCurrentQuantity = new Label("Τρέχουσα Ποσότητα :");
        addProductFormGrid.add(productCurrentQuantity, 0, 6);

        Label productCategoryQuantity = new Label("Κατηγορία :");
        addProductFormGrid.add(productCategoryQuantity, 0, 7);

    }

    public void createTextFieldsOnProductForm() {
        productTitleTextField = new TextField();
        addProductFormGrid.add(productTitleTextField, 2, 0);

        productBarcodeTextField = new TextField();
        addProductFormGrid.add(productBarcodeTextField, 2, 1);

        productInitialPriceTextField = new TextField();
        addProductFormGrid.add(productInitialPriceTextField, 2, 2);

        productProfitRateTextField = new TextField();
        addProductFormGrid.add(productProfitRateTextField, 2, 4);

        productMinimumQuantityTextField = new TextField();
        addProductFormGrid.add(productMinimumQuantityTextField, 2, 5);

        productCurrentQuantityTextField = new TextField();
        addProductFormGrid.add(productCurrentQuantityTextField, 2, 6);


    }

    public void createChoiceBoxesOnAddProductForm() {

        taxRateChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("24", "13", "6", "0"));
        addProductFormGrid.add(taxRateChoiceBox, 2, 3);

        categoryChoiceBox = new ChoiceBox<>
                (FXCollections.observableArrayList("Τσιγάρα", "Ποτά-Αναψυκτικά", "Είδη 13%", "Είδη 24%", "Γαλακτοκομικά", "Αρτοσκευάσματα", "Περιοδικά", "Τηλεκάρτες"));
        addProductFormGrid.add(categoryChoiceBox, 2, 7);

    }

    public boolean allFieldsAreNotEmpty() {
        return (isNotEmpty(productTitleTextField) &&
                isNotEmpty(productBarcodeTextField) &&
                isNotEmpty(productInitialPriceTextField) &&
                isNotEmpty(productProfitRateTextField) &&
                choiceBoxIsNotEmpty(taxRateChoiceBox) &&
                isNotEmpty(productMinimumQuantityTextField) &&
                isNotEmpty(productCurrentQuantityTextField) &&
                choiceBoxIsNotEmpty(categoryChoiceBox)
        );
    }

    public boolean isNotEmpty(TextField textField) {
        return !textField.getText().equals("");
    }

    public boolean choiceBoxIsNotEmpty(ChoiceBox choiceBox) {
        return !choiceBox.getValue().equals("");
    }

    public boolean isNumber(TextField textField) {
        try {
            double d = Double.parseDouble(textField.getText());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean hasComma(TextField textField) {
        return textField.getText().contains(",");
    }

    public String changeCommaToDot(TextField textField) {
        return textField.getText().replace(",", ".");
    }

    public void clearForm() {
        productTitleTextField.clear();
        productBarcodeTextField.clear();
        productInitialPriceTextField.clear();
        productProfitRateTextField.clear();
        productMinimumQuantityTextField.clear();
        productCurrentQuantityTextField.clear();

    }

}
