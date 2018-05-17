package sample.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.Context;
import sample.DatabaseHelper;
import sample.Main;
import sample.Models.Invoice;
import sample.Models.Supplier;

import java.io.IOException;

public class SuppliersController {

    @FXML
    private Button addSupplierButton, addInvoicetoSupplierButton, supplierPaymentButton;

    @FXML
    private TableView suppliersTable, latestInvoicesTable;
    @FXML
    private VBox suppliersCenterVBox;

    public void initialize() throws IOException {

        DatabaseHelper databaseHelper = Main.databaseHelper;
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList(databaseHelper.getSuppliersList());
        suppliersTable.setItems(suppliers);

        ObservableList<Invoice> latestInvoices = FXCollections.observableArrayList(databaseHelper.getLatestInvoices());
        latestInvoicesTable.setItems(latestInvoices);

        Label suppliersLabel = new Label("Πίνακας Προμηθευτών");
        Font font = new Font("Tahoma", 20);
        suppliersLabel.setFont(font);
        Label invoicesLabel = new Label("Πίνακας Τιμολογίων");
        invoicesLabel.setFont(font);
        suppliersCenterVBox.getChildren().add(0, suppliersLabel);
        suppliersCenterVBox.getChildren().add(2, invoicesLabel);
        createLatestInvoicesTableView();
        createSuppliersTableView();
        Context.getInstance().setSuppliersTableView(suppliersTable);
        Context.getInstance().setInvoicesTableView(latestInvoicesTable);
        createMenuSuppliersButtons();


    }

    public void createSuppliersTableView() {

        TableColumn nameTableColumn = new TableColumn("Όνομα");
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
        nameTableColumn.setPrefWidth(200);

        TableColumn vatNumberTableColumn = new TableColumn("ΑΦΜ");
        vatNumberTableColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("vatNumber"));
        vatNumberTableColumn.setPrefWidth(150);

        TableColumn balanceTableColumn = new TableColumn("Οφειλές");
        balanceTableColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("balance"));
        balanceTableColumn.setPrefWidth(200);


        suppliersTable.getColumns().addAll(nameTableColumn, vatNumberTableColumn, balanceTableColumn);
        suppliersTable.setEditable(true);

    }

    public void createLatestInvoicesTableView() {
        TableColumn supplierColumn = new TableColumn("Προμηθευτής");
        supplierColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Invoice, String>, ObservableValue<String>>) p -> {
            if (p.getValue() != null) {
                return new SimpleStringProperty(p.getValue().getSupplier().getName());
            } else {
                return new SimpleStringProperty("<no name>");
            }
        });
        supplierColumn.setPrefWidth(300);
        /*supplierColumn.setCellFactory(col -> new TableCell<Invoice, Supplier>() {
            @Override
            protected void updateItem(Supplier item, boolean empty) {
                super.updateItem(item, empty) ;
                setText(empty ?" ":item.getName());
            }
        });*/

        TableColumn dateColumn = new TableColumn("Ημερ/νια");
        dateColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("date"));
        dateColumn.setPrefWidth(150);

        TableColumn sumColumn = new TableColumn("Ποσό");
        sumColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("sum"));
        sumColumn.setPrefWidth(200);

        TableColumn hasBeenPaidColumn = new TableColumn("Μετρητοίς");
        hasBeenPaidColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("hasBeenPaid"));
        hasBeenPaidColumn.setPrefWidth(200);
        hasBeenPaidColumn.setCellFactory(col -> new TableCell<Invoice, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "Ναι" : "Όχι");
            }
        });


        latestInvoicesTable.getColumns().addAll(supplierColumn, dateColumn, sumColumn, hasBeenPaidColumn);
        latestInvoicesTable.setEditable(true);
    }


    public void createMenuSuppliersButtons() {
        addSupplierButton.setText("Προσθήκη Προμηθευτή");
        Image addImage = new Image(getClass().getResourceAsStream("/Images/supplier_icon.png"));
        addSupplierButton.setGraphic(new ImageView(addImage));
        addSupplierButton.setContentDisplay(ContentDisplay.TOP);
        addSupplierButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/addSupplier.fxml"));
                    Parent addProduct = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Εισαγωγή Προμηθευτή");
                    stage.setScene(new Scene(addProduct));
                    stage.setResizable(false);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        addInvoicetoSupplierButton.setText("Προσθήκη Τιμολογίου");
        Image addImageToInvoice = new Image(getClass().getResourceAsStream("/Images/add_invoice_icon.png"));
        addInvoicetoSupplierButton.setGraphic(new ImageView(addImageToInvoice));
        addInvoicetoSupplierButton.setContentDisplay(ContentDisplay.TOP);
        addInvoicetoSupplierButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/addInvoice.fxml"));
                    Parent addProduct = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Εισαγωγή Τιμολογίου");
                    stage.setScene(new Scene(addProduct));
                    stage.setResizable(false);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        supplierPaymentButton.setText("Πληρωμή Προμηθευτή");
        Image supplierImage = new Image(getClass().getResourceAsStream("/Images/pay_supplier_icon.png"));
        supplierPaymentButton.setGraphic(new ImageView(supplierImage));
        supplierPaymentButton.setContentDisplay(ContentDisplay.TOP);
        supplierPaymentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/paySupplier.fxml"));
                    Parent addProduct = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Πληρωμή Προμηθευτή");
                    stage.setScene(new Scene(addProduct));
                    stage.setResizable(false);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
