package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import sample.DatabaseHelper;
import sample.Main;
import sample.Models.Product;
import sample.Models.Receipt;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class MainController {

    @FXML
    Button addProductButton, editProductButton, searchProductButton,lackOfProductsButton;
    @FXML
    GridPane cashierButtonsGrid;

    @FXML
    private Tab cashierTab, suppliersTab, statisticsTab;

    @FXML
    private TableView cashierTable;

    @FXML
    private GridPane insertGridPane;

    private TextField insertBarcodeTextField, insertQuantityTextField;

    private Label costTitle;

    private double cost = 0;
    private DecimalFormat df2;

    public void initialize() {

        createInsertGridPane();
        addTabsImages();
        createMenuCashierButtons();
        createCashierNumPadsButtons();
        createOtherDrawerButtons();
        createCashierTableView();

    }



    public void createMenuCashierButtons() {
        addProductButton.setText("Εισαγωγή Προϊόντος");
        Image addImage = new Image(getClass().getResourceAsStream("/Images/add_icon.png"));
        addProductButton.setGraphic(new ImageView(addImage));
        addProductButton.setContentDisplay(ContentDisplay.TOP);
        addProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/addProduct.fxml"));
                    Parent addProduct = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Εισαγωγή Προϊόντος");
                    stage.setScene(new Scene(addProduct));
                    stage.setResizable(false);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        editProductButton.setText("Επεξεργασία Προϊόντος");
        Image editImage = new Image(getClass().getResourceAsStream("/Images/edit_icon.png"));
        editProductButton.setGraphic(new ImageView(editImage));
        editProductButton.setContentDisplay(ContentDisplay.TOP);
        editProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/editProduct.fxml"));
                    Parent editProduct = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Επεξεργασία Προϊόντος");
                    stage.setScene(new Scene(editProduct));
                    stage.setResizable(false);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        searchProductButton.setText("Αναζήτηση Προϊόντος");
        Image searchImage = new Image(getClass().getResourceAsStream("/Images/search_icon.png"));
        searchProductButton.setGraphic(new ImageView(searchImage));
        searchProductButton.setContentDisplay(ContentDisplay.TOP);
        searchProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/searchProduct.fxml"));
                    Parent searchProduct = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Αναζήτηση Προϊόντος");
                    stage.setScene(new Scene(searchProduct));
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        lackOfProductsButton.setText("Ελλείψεις Προϊόντων");
        Image lackOfProductsImage = new Image(getClass().getResourceAsStream("/Images/list_icon.ico"));
        lackOfProductsButton.setGraphic(new ImageView(lackOfProductsImage));
        lackOfProductsButton.setContentDisplay(ContentDisplay.TOP);
        lackOfProductsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/lackOfProducts.fxml"));
                    Parent searchProduct = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Ελλειπείς Προϊόντα");
                    stage.setScene(new Scene(searchProduct));
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createCashierTableView() {

        TableColumn titleTableColumn = new TableColumn("Περιγραφή");
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("title"));
        titleTableColumn.setPrefWidth(200);

        TableColumn barcodeTableColumn = new TableColumn("Barcode");
        barcodeTableColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("barcode"));
        barcodeTableColumn.setPrefWidth(150);

        TableColumn quantityTableColumn = new TableColumn("Ποσότητα");
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quantityForSell"));
        quantityTableColumn.setPrefWidth(100);

        TableColumn costTableColumn = new TableColumn("Κόστος");
        costTableColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("sellingPrice"));
        costTableColumn.setPrefWidth(100);

        TableColumn taxRateTableColumn = new TableColumn("Φ.Π.Α");
        taxRateTableColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("taxRate"));
        taxRateTableColumn.setPrefWidth(100);

        cashierTable.getColumns().addAll(titleTableColumn, barcodeTableColumn, quantityTableColumn, costTableColumn, taxRateTableColumn);

    }

    public void createCashierNumPadsButtons() {
        int k = 9;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Text number = new Text("" + k);
                Button myButton = new Button(number.getText());
                myButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                int finalK = k;
                myButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        insertQuantityTextField.setText(insertQuantityTextField.getText() + "" + finalK);
                    }
                });
                cashierButtonsGrid.add(myButton, j, i);
                k--;
            }
        }

        Button zeroButton = new Button("0");
        zeroButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        zeroButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                insertQuantityTextField.setText(insertQuantityTextField.getText() + "" + 0);
            }
        });
        cashierButtonsGrid.add(zeroButton, 0, 3);

    }

    public void createOtherDrawerButtons() {
        Button clearButton = new Button("C");
        clearButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                insertQuantityTextField.setText("");
            }
        });
        cashierButtonsGrid.add(clearButton, 1, 3);

        Button cashDrawerButton = new Button("Συρτάρι");
        cashDrawerButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cashierButtonsGrid.add(cashDrawerButton, 2, 3);

        Button productSubstractButton = new Button("Αφαίρεση");
        productSubstractButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cashierButtonsGrid.add(productSubstractButton, 3, 2);
        productSubstractButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!cashierTable.getItems().isEmpty()) {

                    Product removedProduct = (Product) cashierTable.getItems().remove(cashierTable.getItems().size() - 1);
                    cost -= removedProduct.getSellingPrice();
                    costTitle.setText("Συνολικό Ποσό :" + df2.format(cost) + " €");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη αφαίρεση απο το καλάθι αγορών");
                    alert.setHeaderText("Δεν υπάρχει προϊόν προς αφαίρεση!");
                    alert.showAndWait();
                }
            }
        });

        Button cancellationButton = new Button("Ακύρωση Απόδειξης");
        cancellationButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cashierButtonsGrid.add(cancellationButton, 3, 3);
        cancellationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!cashierTable.getItems().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.YES, ButtonType.CANCEL);
                    alert.setTitle("Ακύρωση Απόδειξης");
                    alert.setHeaderText("Είστε σίγουρος/η ότι θέλετε να ακυρώσετε την απόδειξη;");


                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get().equals(ButtonType.YES)) {
                        cashierTable.getItems().clear();
                        cost = 0;
                        costTitle.setText("Συνολικό Ποσό : " + cost + " €");
                    } else {
                        alert.close();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη αφαίρεση απο το καλάθι αγορών");
                    alert.setHeaderText("Δεν υπάρχει προϊόν στο καλάθι αγορών για την ακύρωση της απόδειξης!");
                    alert.showAndWait();
                }
            }
        });

        Button cashPaymentButton = new Button("Μετρητά");
        cashPaymentButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cashierButtonsGrid.add(cashPaymentButton, 3, 0);
        cashPaymentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!cashierTable.getItems().isEmpty()) {
                    Datastore datastore = Main.databaseHelper.getDatastore();
                    ArrayList<Product> products = new ArrayList<>();
                    for (Object p : cashierTable.getItems()) {
                        Product product = (Product) p;
                        products.add(product);
                    }
                    Receipt receipt = new Receipt(products);
                    datastore.save(receipt);

                    for (Product p : products) {
                        Query<Product> query = datastore.createQuery(Product.class).field("barcode").equal(p.getBarcode());
                        UpdateOperations<Product> ops = datastore.createUpdateOperations(Product.class).set("sumOfSales", p.getSumOfSales() + p.getQuantityForSell());
                        datastore.update(query, ops);
                        UpdateOperations<Product> ops2 = datastore.createUpdateOperations(Product.class).set("currentQuantity", p.getCurrentQuantity() - p.getQuantityForSell());
                        datastore.update(query,ops2);
                    }


                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                    alert.setTitle("Έκδοση Απόδειξης");
                    alert.setHeaderText("Έκδοση απόδειξης στην ταμεική μηχανή!");
                    alert.showAndWait();

                    for (int i = 0; i < cashierTable.getItems().size(); i++) {
                        cashierTable.getItems().clear();
                    }
                    cost = 0;
                    costTitle.setText("Συνολικό Ποσό : " + cost + " €");

                    insertBarcodeTextField.setText("");
                    insertQuantityTextField.setText("");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη έκδοση απόδειξης");
                    alert.setHeaderText("Δεν υπάρχει προϊόν στο καλάθι αγορών για την έκδοση απόδειξης!");
                    alert.showAndWait();
                }
            }
        });

        Button cardPaymentButton = new Button("Κάρτα");
        cardPaymentButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cashierButtonsGrid.add(cardPaymentButton, 3, 1);

    }

    public void addTabsImages() {
        Image cashierImage = new Image(getClass().getResourceAsStream("/Images/cashier-icon.png"));
        ImageView cashierImageView = new ImageView(cashierImage);
        cashierTab.setGraphic(cashierImageView);

        Image suppliersImage = new Image(getClass().getResourceAsStream("/Images/supplier-icon.png"));
        ImageView supplierImageView = new ImageView(suppliersImage);
        suppliersTab.setGraphic(supplierImageView);

        Image statisticsImage = new Image(getClass().getResourceAsStream("/Images/statistics-icon.png"));
        ImageView statisticsImageView = new ImageView(statisticsImage);
        statisticsTab.setGraphic(statisticsImageView);
    }

    public void createInsertGridPane() {
        insertBarcodeTextField = new TextField();
        insertBarcodeTextField.setPromptText("Εισαγωγή Barcode");
        insertBarcodeTextField.requestFocus();
        insertBarcodeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    addToBasket();
                }
            }
        });
        insertGridPane.add(insertBarcodeTextField, 1, 0);


        insertQuantityTextField = new TextField();
        insertQuantityTextField.setPromptText("Εισαγωγή Ποσότητας");
        insertQuantityTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    addToBasket();
                }
            }
        });
        insertGridPane.add(insertQuantityTextField, 2, 0);

        costTitle = new Label("Συνολικο Ποσό : 0 €");
        costTitle.setFont(new Font("Lato", 20));
        insertGridPane.add(costTitle, 0, 0);


        Button addToTableButton = new Button("Προσθήκη στο Καλάθι");
        addToTableButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               addToBasket();
            }
        });
        insertGridPane.add(addToTableButton, 3, 0);

        insertGridPane.setHgap(10);
        insertGridPane.setVgap(10);
        insertGridPane.setPadding(new Insets(10, 10, 10, 10));
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

    public void addToBasket(){
        df2 = new DecimalFormat("##.##");

        if (!insertBarcodeTextField.getText().equals("")) {
            if (isBarcodeCorrect(insertBarcodeTextField)) {
                Long barcode = Long.parseLong(insertBarcodeTextField.getText());
                DatabaseHelper databaseHelper = Main.databaseHelper;
                if (databaseHelper.getBarcodes().contains(barcode)) {
                    if (insertQuantityTextField.getText().equals("") || insertQuantityTextField.getText().equals("Εισαγωγή Ποσότητας")) {
                        HashMap<Long, Product> products = databaseHelper.getProductsMap();
                        Product product = products.get(barcode);
                        product.setQuantityForSell(1);
                        double productCost = product.getSellingPrice() * 1;
                        cost += product.getSellingPrice() * 1;
                        cashierTable.getItems().add(product);
                        costTitle.setText("Συνολικό Ποσό :" + df2.format(cost) + " €");
                    } else {
                        if (isNumber(insertQuantityTextField)) {
                            HashMap<Long, Product> products = databaseHelper.getProductsMap();
                            Product product = products.get(barcode);
                            product.setQuantityForSell(Integer.parseInt(insertQuantityTextField.getText()));
                            double productCost = product.getSellingPrice() * Integer.parseInt(insertQuantityTextField.getText());
                            cost += productCost;
                            cashierTable.getItems().add(product);
                            costTitle.setText("Συνολικό Ποσό :" + df2.format(cost) + " €");

                            insertQuantityTextField.setText("");
                            insertBarcodeTextField.setText("");
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setTitle("Λανθασμένη εισαγωγή στο καλάθι αγορών");
                            alert.setHeaderText("Λανθασμένη μορφή ποσότητας!");

                            alert.showAndWait();
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Λανθασμένη εισαγωγή στο καλάθι αγορών");
                    alert.setHeaderText("Το Προϊόν δεν υπάρχει στη βάση δεδομένων!");

                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                alert.setTitle("Λανθασμένη εισαγωγή στο καλάθι αγορών");
                alert.setHeaderText("Λανθασμένη μορφή Barcode!");
                alert.showAndWait();

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
            alert.setTitle("Λανθασμένη εισαγωγή στο καλάθι αγορών");
            alert.setHeaderText("Παρακαλώ εισάγετε το barcode στο πεδίο");

            alert.showAndWait();
        }
    }


}
