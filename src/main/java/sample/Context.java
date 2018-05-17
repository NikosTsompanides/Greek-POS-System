package sample;


import javafx.scene.control.TableView;

public class Context {

    private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }

    private TableView supplierTable, latestInvoicesTable;

    public void setSuppliersTableView(TableView tableView) {
        this.supplierTable = tableView;
    }

    public void setInvoicesTableView(TableView tableView) {
        this.latestInvoicesTable = tableView;
    }

    public TableView suppliersTableView() {
        return supplierTable;
    }

    public TableView invoicesTableView() {
        return latestInvoicesTable;
    }

}
