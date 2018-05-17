package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import sample.DatabaseHelper;
import sample.Main;
import sample.Models.Product;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;


public class SearchProductController {

    @FXML
    private GridPane searchWindowGridPane;
    private TextField searchWithBarcodeTextField;
    @FXML
    private TreeView productTreeView;
    private Button searchButton;

    @FXML private VBox searchVBox;
    private ArrayList<Long> barcodes;

    public void initialize() {
        DatabaseHelper databaseHelper= Main.databaseHelper;
        barcodes = databaseHelper.getBarcodes();
        Datastore datastore = databaseHelper.getDatastore();

        searchVBox.setSpacing(10);
        Button deleteButton = new Button("Διαγραφή");
        searchVBox.setMargin(deleteButton,new Insets(25,50,25,50));

        searchWindowGridPane.setAlignment(Pos.CENTER);
        searchWindowGridPane.setHgap(10);
        searchWindowGridPane.setVgap(10);
        searchWindowGridPane.setPadding(new Insets(25, 25, 25, 25));
        createSearchArea();
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchProduct();
            }
        });

        searchVBox.getChildren().add(2,deleteButton);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!isEmpty(searchWithBarcodeTextField)){
                    if(isBarcodeCorrect(searchWithBarcodeTextField)){
                        Long barcode = Long.parseLong(searchWithBarcodeTextField.getText());
                        if(barcodes.contains(barcode)){

                            Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.YES, ButtonType.CANCEL);
                            alert.setTitle("Διαγραφή Προϊόντος");
                            alert.setHeaderText("Είστε σίγουρος/η ότι θέλετε να διαγράψετε το συγκεκριμένο προϊόν ;");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get().equals(ButtonType.YES)) {

                                final Query<Product> overPaidQuery = datastore.createQuery(Product.class)
                                        .filter("barcode =", barcode);
                                datastore.delete(overPaidQuery);

                                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
                                alert2.setTitle("Επιτυχής Διαγραφή Προϊόντος");
                                alert2.setHeaderText("Επιτυχής διαγραφλη προϊόντος");


                            } else {
                                alert.close();
                            }

                            final Query<Product> overPaidQuery = datastore.createQuery(Product.class).field("barcode").equal(barcode);

                            datastore.delete(overPaidQuery);
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προϊόντος");
                            alert.setHeaderText("Το Προϊόν δεν βρέθηκε ");
                            alert.showAndWait();
                        }
                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Το barcode πρέπει να είναι αριθμός μήκους 12 ή 13 ψηφίων!", ButtonType.OK);
                        alert.setTitle("Λανθασμένη Εισαγωγή Στοιχείων Προϊόντος");
                        alert.setHeaderText("Λανθασμένη μορφή Barcode ");
                        alert.showAndWait();
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR,"", ButtonType.OK);
                    alert.setTitle("Κενό Πεδίο Barcode");
                    alert.setHeaderText("Παρακαλώ συμπληρώστε το barcode!");
                    alert.showAndWait();
                }
            }
        });

        deleteButton.setAlignment(Pos.CENTER);
    }

    public void createSearchArea() {

        searchWithBarcodeTextField = new TextField();
        searchWithBarcodeTextField.setPrefHeight(50);
        searchWithBarcodeTextField.setPromptText("Εισάγετε το barcode του προϊόντος");
        searchWithBarcodeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    searchProduct();
                }
            }
        });
        searchWindowGridPane.add(searchWithBarcodeTextField, 0, 0);

        searchButton = new Button("Αναζήτηση");
        searchButton.setPrefHeight(50);
        searchWindowGridPane.add(searchButton, 1, 0);


    }

    public boolean isEmpty(TextField textField) {
        return (textField.getText().equals("Εισάγετε το barcode του προϊόντος") || textField.getText().equals(""));
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

    public void searchProduct(){
        if (!isEmpty(searchWithBarcodeTextField)) {
            if (isBarcodeCorrect(searchWithBarcodeTextField)) {
                Long barcode = Long.parseLong(searchWithBarcodeTextField.getText());
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
                    productTreeView.setRoot(rootItem);

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

}
