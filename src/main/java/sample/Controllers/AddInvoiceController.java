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
import javafx.util.StringConverter;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import sample.Context;
import sample.DatabaseHelper;
import sample.Main;
import sample.Models.Invoice;
import sample.Models.Supplier;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


public class AddInvoiceController {

    @FXML
    private GridPane addInvoiceGrid;

    private ChoiceBox suppliersBox;
    private DatePicker datePicker;
    private TextField sumTextField;

    DatabaseHelper databaseHelper;
    Datastore datastore;
    private TableView suppliersTable, latestInvoicesTable;
    private CheckBox hasBeenPaidCheckBox;

    public void initialize() throws IOException {

        databaseHelper = Main.databaseHelper;
        datastore = databaseHelper.getDatastore();

        suppliersTable = Context.getInstance().suppliersTableView();
        latestInvoicesTable = Context.getInstance().invoicesTableView();

        addInvoiceGrid.setAlignment(Pos.CENTER);
        addInvoiceGrid.setHgap(10);
        addInvoiceGrid.setVgap(10);
        addInvoiceGrid.setPadding(new Insets(25, 25, 25, 25));

        createLabelsOnAddInvoiceForm();
        createInsertControllssonAddInvoiceForm();
    }

    public void createLabelsOnAddInvoiceForm() {
        Label selectSupplierLabel = new Label("Επιλογή Προμηθευτή :");
        addInvoiceGrid.add(selectSupplierLabel, 0, 0);

        Label addDate = new Label("Προσθήκη Ημερ/νιας :");
        addInvoiceGrid.add(addDate, 0, 1);

        Label addSum = new Label("Προσθήκη Τελικού Ποσού :");
        addInvoiceGrid.add(addSum, 0, 2);

        Label hasBeenPaidLabel = new Label("Έχει Εξοφληθεί;");
        addInvoiceGrid.add(hasBeenPaidLabel, 0, 3);
    }

    public void createInsertControllssonAddInvoiceForm() throws IOException {

        suppliersBox = new ChoiceBox();
        HashMap<String, Supplier> suppliers = databaseHelper.getSuppliersMapNames();

        ArrayList<String> names = new ArrayList<>();
        for (Supplier s : suppliers.values())
            names.add(s.getName());


        ObservableList<String> namesObs = FXCollections.observableArrayList(names);
        suppliersBox.setItems(namesObs);
        addInvoiceGrid.add(suppliersBox, 2, 0);

        /*Supplier supplier = suppliers.get(suppliersBox.getValue());
        System.out.println(supplier.getName());*/


        datePicker = new DatePicker();
        datePicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "dd-MM-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                datePicker.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        addInvoiceGrid.add(datePicker, 2, 1);

        sumTextField = new TextField();
        sumTextField.setPromptText("Εισάγετε το τελικό ποσό");
        addInvoiceGrid.add(sumTextField, 2, 2);

        hasBeenPaidCheckBox = new CheckBox();
        addInvoiceGrid.add(hasBeenPaidCheckBox, 2, 3);

        Button button = new Button("Προσθήκη");
        addInvoiceGrid.add(button, 1, 4);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (isNumber(sumTextField)) {

                    Supplier supplier = suppliers.get(suppliersBox.getValue());
                    Double sum = Double.parseDouble(sumTextField.getText());
                    if (hasBeenPaidCheckBox.isSelected()) {
                        Invoice invoice = new Invoice(supplier, datePicker.getValue(), sum, true);
                        datastore.save(invoice);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
                        alert.setTitle("Επιτυχής Προοσθήκη Τιμολογίου");
                        alert.setHeaderText("Το τιμολόγιο προστέθηκε στη βάση");
                        alert.showAndWait();
                    } else {
                        Invoice invoice = new Invoice(supplier, datePicker.getValue(), sum, false);
                        datastore.save(invoice);

                        Query<Supplier> query = datastore.createQuery(Supplier.class).field("vatNumber").equal(supplier.getVatNumber());
                        UpdateOperations<Supplier> ops = datastore.createUpdateOperations(Supplier.class).set("balance", supplier.getBalance() + sum);
                        datastore.update(query, ops);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
                        alert.setTitle("Επιτυχής Προοσθήκη Τιμολογίου");
                        alert.setHeaderText("Το τιμολόγιο προστέθηκε στη βάση");
                        alert.showAndWait();
                    }

                    suppliersTable.getItems().clear();
                    ObservableList<Supplier> suppliers = null;
                    suppliers = FXCollections.observableArrayList(databaseHelper.getSuppliersList());
                    suppliersTable.setItems(suppliers);

                    latestInvoicesTable.getItems().clear();
                    ObservableList<Invoice> invoices = null;
                    invoices = FXCollections.observableArrayList(databaseHelper.getLatestInvoices());
                    latestInvoicesTable.setItems(invoices);

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
                    alert.setTitle("Ανεπιτυχής Προοσθήκη Τιμολογίου");
                    alert.setHeaderText("Λανθασμένη μορφή τελικού ποσού");
                    alert.showAndWait();
                }


            }
        });


    }

    public boolean areAllControlsEmpty() {
        return suppliersBox.getSelectionModel().isEmpty() || (datePicker.getValue() == null) || sumTextField.getText().equals("");
    }

    public boolean isNumber(TextField textField) {
        try {
            double d = Double.parseDouble(textField.getText());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
