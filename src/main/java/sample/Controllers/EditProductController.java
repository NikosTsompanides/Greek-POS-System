package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import sample.Main;
import sample.Models.Product;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class EditProductController {

    @FXML
    private GridPane editProductForm;
    @FXML
    private TreeView searchTreeView;

    TextField productTitleTextField, productBarcodeTextField, productInitialPriceTextField, productProfitRateTextField, productSellingPriceTextField, productMinimumQuantityTextField, productCurrentQuantityTextField, searchWithBarcodeTextField;
    ChoiceBox<String> taxRateChoiceBox, categoryChoiceBox;

    Label productTitle, productBarcode, productInitialPrice, productTaxRate, productProfitRate, productMinimumQuantity, productCurrentQuantity, productCategory;
    Button editTitleButton, editSelligPriceButton, editBarcodeButton, editInitialPriceButton, editMinQuantityButton, editCurrentQuantityButton, searchButton, editProfitRateButton, editTaxRateButton, editCategoryButton;


    Datastore datastore;

    public void initialize() throws IOException {
        editProductForm.setAlignment(Pos.CENTER);
        editProductForm.setHgap(10);
        editProductForm.setVgap(10);
        editProductForm.setPadding(new Insets(25, 25, 25, 25));

        datastore = Main.databaseHelper.getDatastore();


        createSearchArea();
        createTextFieldsOnEditProductForm();
        createChoiceBoxesOnEditProductForm();
        createButtonsOnEditProductsForm();


    }

    public void createTextsOnEditProductForm() {

        productTitle = new Label("Τίτλος :");
        editProductForm.add(productTitle, 0, 1);

        productBarcode = new Label("Barcode :");
        editProductForm.add(productBarcode, 0, 2);

        productInitialPrice = new Label("Αρχική Τιμή :");
        editProductForm.add(productInitialPrice, 0, 3);

        productTaxRate = new Label("Φ.Π.Α :");
        editProductForm.add(productTaxRate, 0, 4);

        productProfitRate = new Label("Ποσοστό Κέρδους :");
        editProductForm.add(productProfitRate, 0, 5);

        productMinimumQuantity = new Label("Ελάχιστη Ποσότητα :");
        editProductForm.add(productMinimumQuantity, 0, 6);

        productCurrentQuantity = new Label("Τρέχουσα Ποσότητα :");
        editProductForm.add(productCurrentQuantity, 0, 7);

        productCategory = new Label("Κατηγορία :");
        editProductForm.add(productCategory, 0, 8);

    }

    public void createTextFieldsOnEditProductForm() {
        productTitleTextField = new TextField();
        productTitleTextField.setPromptText("Εισάγετε τον καιρνούργιο τίτλο");
        editProductForm.add(productTitleTextField, 0, 1);

        productBarcodeTextField = new TextField();
        productBarcodeTextField.setPromptText("Εισάγετε το καινούργιο barcode");
        editProductForm.add(productBarcodeTextField, 0, 2);

        productInitialPriceTextField = new TextField();
        productInitialPriceTextField.setPromptText("Εισάγετε την καινούργια αρχική τιμή");
        editProductForm.add(productInitialPriceTextField, 0, 3);

        productProfitRateTextField = new TextField();
        productProfitRateTextField.setPromptText("Εισάγετε το καινούργιο ποσοστό κέρδους");
        editProductForm.add(productProfitRateTextField, 0, 5);

        productMinimumQuantityTextField = new TextField();
        productMinimumQuantityTextField.setPromptText("Εισάγετε την καινούργια ελάχιστη ποσότητα");
        editProductForm.add(productMinimumQuantityTextField, 0, 6);

        productCurrentQuantityTextField = new TextField();
        productCurrentQuantityTextField.setPromptText("Εισάγετε την καινούργια τρέχουσα ποσότητα");
        editProductForm.add(productCurrentQuantityTextField, 0, 7);

        productSellingPriceTextField = new TextField();
        productSellingPriceTextField.setPromptText("Εισάγετε την καινούργια τιμή πώλησης");
        editProductForm.add(productSellingPriceTextField, 0, 9);


    }

    public void createButtonsOnEditProductsForm() {


        editTitleButton = new Button("Αλλαγή Τίτλου");
        editProductForm.add(editTitleButton, 1, 1);
        editTitleButton.setPrefWidth(250);
        editTitleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!searchWithBarcodeTextField.getText().equals("")) {
                    if (isBarcodeCorrect(searchWithBarcodeTextField)) {

                        if (!productTitleTextField.getText().equals("")) {

                            Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));
                            UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("title", productTitleTextField.getText());
                            datastore.update(query, ops);

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                            alert.setTitle("Αλλαγή δεδομένων");
                            alert.setHeaderText("Επιτυχης αλλαγή τίτλου προϊόντος ");
                            alert.showAndWait();

                            createTreeView();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                            alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο αλλαγής τίτλου ");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                        alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                        alert.setHeaderText("Λανθασμένη Μορφή Barcode");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο του Barcode ");
                    alert.showAndWait();
                }
            }
        });


        editBarcodeButton = new Button("Αλλαγή Barcode");
        editProductForm.add(editBarcodeButton, 1, 2);
        editBarcodeButton.setPrefWidth(250);
        editBarcodeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!searchWithBarcodeTextField.getText().equals("")) {
                    if (isBarcodeCorrect(searchWithBarcodeTextField)) {

                        if (!productBarcodeTextField.getText().equals("")) {

                            if (isBarcodeCorrect(productBarcodeTextField)) {
                                ArrayList<Long> barcodes = Main.databaseHelper.getBarcodes();
                                if (!barcodes.contains(Long.parseLong(productBarcodeTextField.getText()))) {
                                    Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));
                                    System.out.println(query.toString());
                                    UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("barcode", Long.parseLong(productBarcodeTextField.getText()));
                                    System.out.println(ops);
                                    datastore.update(query, ops);

                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                                    alert.setTitle("Αλλαγή δεδομένων");
                                    alert.setHeaderText("Επιτυχης αλλαγή Barcode προϊόντος ");
                                    alert.showAndWait();

                                    createTreeView();
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                                    alert.setTitle("Αλλαγή δεδομένων");
                                    alert.setHeaderText("Το προϊόν υπάρχει ήδη");
                                    alert.showAndWait();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                                alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                                alert.setHeaderText("Λανθασμένη Μορφή Barcode");
                                alert.showAndWait();
                            }

                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                            alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο αλλαγής Barcode ");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                        alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                        alert.setHeaderText("Λανθασμένη Μορφή Barcode");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο του Barcode ");
                    alert.showAndWait();
                }
            }
        });

        editInitialPriceButton = new Button("Αλλαγή Αρχικής Τιμής");
        editProductForm.add(editInitialPriceButton, 1, 3);
        editInitialPriceButton.setPrefWidth(250);
        editInitialPriceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!searchWithBarcodeTextField.getText().equals("")) {
                    if (isBarcodeCorrect(searchWithBarcodeTextField)) {

                        if (!productInitialPriceTextField.getText().equals("")) {

                            if (containsComma(productInitialPriceTextField)) {
                                String convertedString = productInitialPriceTextField.getText().replace(",", ".");
                                Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));

                                Product product = Main.databaseHelper.getSpecificProductByBarcode(Long.parseLong(searchWithBarcodeTextField.getText()));
                                double newInitialPrice = Double.parseDouble(convertedString);


                                UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("initialPrice", newInitialPrice);
                                datastore.update(query, ops);

                                double taxedPrice = newInitialPrice + newInitialPrice * ((double) product.getTaxRate() / 100.0);
                                double newSellingPrice = taxedPrice + (taxedPrice * ((double) product.getProfitRate() / 100.0));
                                UpdateOperations<Product> ops1 = datastore.createUpdateOperations(Product.class).set("sellingPrice", newSellingPrice);
                                datastore.update(query, ops1);

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                                alert.setTitle("Αλλαγή δεδομένων");
                                alert.setHeaderText("Επιτυχης αλλαγή αρχικής τιμής προϊόντος ");
                                alert.showAndWait();

                                createTreeView();

                            } else {

                                Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));

                                Product product = Main.databaseHelper.getSpecificProductByBarcode(Long.parseLong(searchWithBarcodeTextField.getText()));
                                double newInitialPrice = Double.parseDouble(productInitialPriceTextField.getText());


                                UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("initialPrice", newInitialPrice);
                                datastore.update(query, ops);

                                double taxedPrice = newInitialPrice + newInitialPrice * ((double) product.getTaxRate() / 100.0);
                                double newSellingPrice = taxedPrice + (taxedPrice * ((double) product.getProfitRate() / 100.0));
                                UpdateOperations<Product> ops1 = datastore.createUpdateOperations(Product.class).set("sellingPrice", newSellingPrice);
                                datastore.update(query, ops1);

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                                alert.setTitle("Αλλαγή δεδομένων");
                                alert.setHeaderText("Επιτυχης αλλαγή αρχικής τιμής προϊόντος ");
                                alert.showAndWait();

                                createTreeView();

                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                            alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο αλλαγής αρχικής τιμής ");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                        alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                        alert.setHeaderText("Λανθασμένη Μορφή Barcode");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο του Barcode ");
                    alert.showAndWait();
                }
            }
        });

        editSelligPriceButton = new Button("Αλλαγή Τιμής Πώλησης");
        editProductForm.add(editSelligPriceButton, 1, 9);
        editSelligPriceButton.setPrefWidth(250);
        editSelligPriceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!searchWithBarcodeTextField.getText().equals("")) {
                    if (isBarcodeCorrect(searchWithBarcodeTextField)) {

                        if (!productSellingPriceTextField.getText().equals("")) {
                            if (containsComma(productSellingPriceTextField)) {
                                String convertedSellingPrice = productSellingPriceTextField.getText().replace(",", ".");
                                Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));

                                Product product = Main.databaseHelper.getSpecificProductByBarcode(Long.parseLong(searchWithBarcodeTextField.getText()));
                                double newSellingPrice = Double.parseDouble(convertedSellingPrice);


                                UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("sellingPrice", newSellingPrice);
                                datastore.update(query, ops);

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                                alert.setTitle("Αλλαγή δεδομένων");
                                alert.setHeaderText("Επιτυχης αλλαγή αρχικής τιμής προϊόντος ");
                                alert.showAndWait();

                                createTreeView();
                            } else {
                                Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));

                                Product product = Main.databaseHelper.getSpecificProductByBarcode(Long.parseLong(searchWithBarcodeTextField.getText()));
                                double newSellingPrice = Double.parseDouble(productSellingPriceTextField.getText());


                                UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("sellingPrice", newSellingPrice);
                                datastore.update(query, ops);

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                                alert.setTitle("Αλλαγή δεδομένων");
                                alert.setHeaderText("Επιτυχης αλλαγή αρχικής τιμής προϊόντος ");
                                alert.showAndWait();

                                createTreeView();
                            }


                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                            alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο αλλαγής αρχικής τιμής ");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                        alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                        alert.setHeaderText("Λανθασμένη Μορφή Barcode");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο του Barcode ");
                    alert.showAndWait();
                }
            }
        });


        editTaxRateButton = new Button("Αλλαγή Φ.Π.Α");
        editProductForm.add(editTaxRateButton, 1, 4);
        editTaxRateButton.setPrefWidth(250);
        editTaxRateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!searchWithBarcodeTextField.getText().equals("")) {
                    if (isBarcodeCorrect(searchWithBarcodeTextField)) {

                        if (!taxRateChoiceBox.getValue().equals("")) {

                            Product product = Main.databaseHelper.getSpecificProductByBarcode(Long.parseLong(searchWithBarcodeTextField.getText()));

                            Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));
                            UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("taxRate", Integer.parseInt(taxRateChoiceBox.getValue()));
                            datastore.update(query, ops);

                            double taxedPrice = product.getInitialPrice() + product.getInitialPrice() * (Double.parseDouble(taxRateChoiceBox.getValue()) / 100.0);
                            double newSellingPrice = taxedPrice + (taxedPrice * ((double) product.getProfitRate() / 100.0));
                            UpdateOperations<Product> ops1 = datastore.createUpdateOperations(Product.class).set("sellingPrice", newSellingPrice);
                            datastore.update(query, ops1);


                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                            alert.setTitle("Αλλαγή δεδομένων");
                            alert.setHeaderText("Επιτυχης αλλαγή Φ.Π.Α προϊόντος ");
                            alert.showAndWait();

                            createTreeView();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                            alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο αλλαγής Φ.Π.Α ");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                        alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                        alert.setHeaderText("Λανθασμένη Μορφή Barcode");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο του Barcode ");
                    alert.showAndWait();
                }
            }
        });

        editProfitRateButton = new Button("Αλλαγή Ποσοστού κέρδους");
        editProductForm.add(editProfitRateButton, 1, 5);
        editProfitRateButton.setPrefWidth(250);
        editProfitRateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!searchWithBarcodeTextField.getText().equals("")) {
                    if (isBarcodeCorrect(searchWithBarcodeTextField)) {

                        if (!productProfitRateTextField.getText().equals("")) {

                            Product product = Main.databaseHelper.getSpecificProductByBarcode(Long.parseLong(searchWithBarcodeTextField.getText()));

                            Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));
                            UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("taxRate", Integer.parseInt(productProfitRateTextField.getText()));
                            datastore.update(query, ops);

                            double taxedPrice = product.getInitialPrice() + product.getInitialPrice() * ((double) product.getTaxRate() / 100.0);
                            double newSellingPrice = taxedPrice + (taxedPrice * (Double.parseDouble(productProfitRateTextField.getText()) / 100.0));
                            UpdateOperations<Product> ops1 = datastore.createUpdateOperations(Product.class).set("sellingPrice", newSellingPrice);
                            datastore.update(query, ops1);


                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                            alert.setTitle("Αλλαγή δεδομένων");
                            alert.setHeaderText("Επιτυχης αλλαγή ποσοστού κέδρους προϊόντος ");
                            alert.showAndWait();

                            createTreeView();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                            alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο αλλαγής ποσοστού κέρδους ");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                        alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                        alert.setHeaderText("Λανθασμένη Μορφή Barcode");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο του Barcode ");
                    alert.showAndWait();
                }
            }
        });

        editMinQuantityButton = new Button("Αλλαγή Ελάχιστης Ποσότητας ");
        editProductForm.add(editMinQuantityButton, 1, 6);
        editMinQuantityButton.setPrefWidth(250);
        editMinQuantityButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!searchWithBarcodeTextField.getText().equals("")) {
                    if (isBarcodeCorrect(searchWithBarcodeTextField)) {

                        if (!productMinimumQuantityTextField.getText().equals("")) {

                            Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));
                            UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("minQuantity", Integer.parseInt(productMinimumQuantityTextField.getText()));
                            datastore.update(query, ops);

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                            alert.setTitle("Αλλαγή δεδομένων");
                            alert.setHeaderText("Επιτυχης αλλαγή ελάχιστης ποσότητας προϊόντος ");
                            alert.showAndWait();

                            createTreeView();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                            alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο αλλαγής ελάχιστης ποσότητας ");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                        alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                        alert.setHeaderText("Λανθασμένη Μορφή Barcode");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο του Barcode ");
                    alert.showAndWait();
                }
            }
        });

        editCurrentQuantityButton = new Button("Αλλαγή Τρέχουσας Ποσότητας");
        editProductForm.add(editCurrentQuantityButton, 1, 7);
        editCurrentQuantityButton.setPrefWidth(250);
        editCurrentQuantityButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!searchWithBarcodeTextField.getText().equals("")) {
                    if (isBarcodeCorrect(searchWithBarcodeTextField)) {

                        if (!productCurrentQuantityTextField.getText().equals("")) {

                            Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));
                            UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("currentQuantity", Integer.parseInt(productCurrentQuantityTextField.getText()));
                            datastore.update(query, ops);

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                            alert.setTitle("Αλλαγή δεδομένων");
                            alert.setHeaderText("Επιτυχης αλλαγή τρέχουσας ποσότητας προϊόντος ");
                            alert.showAndWait();

                            createTreeView();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                            alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο αλλαγής τρέχουσας ποσότητας ");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                        alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                        alert.setHeaderText("Λανθασμένη Μορφή Barcode");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο του Barcode ");
                    alert.showAndWait();
                }
            }
        });

        editCategoryButton = new Button("Αλλαγή Κατηγορίας");
        editProductForm.add(editCategoryButton, 1, 8);
        editCategoryButton.setPrefWidth(250);
        editCategoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!searchWithBarcodeTextField.getText().equals("")) {
                    if (isBarcodeCorrect(searchWithBarcodeTextField)) {

                        if (!categoryChoiceBox.getValue().equals("")) {

                            Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(Long.parseLong(searchWithBarcodeTextField.getText()));
                            UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("category", categoryChoiceBox.getValue().toString());
                            datastore.update(query, ops);

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                            alert.setTitle("Αλλαγή δεδομένων");
                            alert.setHeaderText("Επιτυχης αλλαγή κατηγορίας προϊόντος ");
                            alert.showAndWait();

                            createTreeView();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                            alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο αλλαγής κατηγορίας ");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                        alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                        alert.setHeaderText("Λανθασμένη Μορφή Barcode");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη εισαγωγή στοιχείων");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε το πεδίο του Barcode ");
                    alert.showAndWait();
                }
            }
        });


    }

    public void createChoiceBoxesOnEditProductForm() {

        taxRateChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("24", "13", "6", "0"));
        editProductForm.add(taxRateChoiceBox, 0, 4);
        taxRateChoiceBox.setPrefWidth(300);

        categoryChoiceBox = new ChoiceBox<>
                (FXCollections.observableArrayList("Τσιγάρα", "Ποτά-Αναψυκτικά", "Είδη 13%", "Είδη 24%", "Γαλακτοκομικά", "Αρτοσκευάσματα", "Περιοδικά", "Τηλεκάρτες"));
        editProductForm.add(categoryChoiceBox, 0, 8);
        categoryChoiceBox.setPrefWidth(300);


    }

    public void createSearchArea() {

        searchWithBarcodeTextField = new TextField();
        searchWithBarcodeTextField.setPrefHeight(50);
        searchWithBarcodeTextField.setPromptText("Εισάγετε το barcode του προϊόντος για αναζήτηση");
        editProductForm.add(searchWithBarcodeTextField, 0, 0);
        searchWithBarcodeTextField.prefHeight(50);
        searchWithBarcodeTextField.setFocusTraversable(false);

        searchButton = new Button("Αναζήτηση");
        searchButton.setPrefHeight(50);
        searchButton.setPrefWidth(250);
        editProductForm.add(searchButton, 1, 0);
        searchButton.prefHeight(50);
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createTreeView();
            }
        });

    }

    public void createTreeView() {
        if (!isEmpty(searchWithBarcodeTextField)) {
            if (isBarcodeCorrect(searchWithBarcodeTextField)) {
                Long barcode = Long.parseLong(searchWithBarcodeTextField.getText());
                ArrayList<Long> barcodes = Main.databaseHelper.getBarcodes();
                if (barcodes.contains(barcode)) {
                    HashMap<Long, Product> products = Main.databaseHelper.getProductsMap();
                    Product product = products.get(barcode);

                    Field[] fields = product.getProductFields();
                    ArrayList<Field> productFields = new ArrayList<>(Arrays.asList(fields));

                    TreeItem<String> rootItem = new TreeItem<String>("" + product.getTitle());
                    rootItem.setExpanded(true);
                    for (Field f : productFields) {
                        f.setAccessible(true);
                        TreeItem<String> item = new TreeItem<>("" + product.getParametersGreekName(f.getName()) + " : " + product.getParameterValue(f.getName()));
                        rootItem.getChildren().add(item);
                    }
                    searchTreeView.setRoot(rootItem);

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προϊόντος");
                    alert.setHeaderText("Το Προϊόν δεν βρέθηκε ");
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Το barcode πρέπει να είναι αριθμός μήκους 12 ή 13 ψηφίων!", ButtonType.OK);
                alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προϊόντος");
                alert.setHeaderText("Λανθασμένη μορφή Barcode ");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Η αναζήτηση προϊόντος γίνεται βάση του barcode!", ButtonType.OK);
            alert.setTitle("Κενό Πεδίο Barcode");
            alert.setHeaderText("Παρακαλώ συμπληρώστε το barcode!");
            alert.showAndWait();
        }
    }

    public boolean isBarcodeCorrect(TextField textField) {
        return (isNumber(textField) && (textField.getText().length() > 11 && textField.getText().length() < 14));
    }

    public boolean isNumber(TextField textField) {
        try {
            double d = Double.parseDouble(textField.getText());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean isEmpty(TextField textField) {
        return (textField.getText().equals("Εισάγετε το barcode του προϊόντος") || textField.getText().equals(""));
    }

    public boolean containsComma(TextField textField) {
        return textField.getText().contains(",");
    }


}
