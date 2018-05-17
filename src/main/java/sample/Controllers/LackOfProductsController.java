package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import sample.DatabaseHelper;
import sample.Main;
import sample.Models.Product;

import java.util.ArrayList;

import java.util.Collections;
import java.util.concurrent.SynchronousQueue;


public class LackOfProductsController {

    @FXML private ListView<String> lackOfProductsListView;
    public void initialize(){

        DatabaseHelper databaseHelper = Main.databaseHelper;
        ArrayList<Product> products = databaseHelper.getLackOfProductsList();
        products.forEach(product -> System.out.println(product.getTitle()));
        Collections.sort(products);
        products.forEach(product -> System.out.println(product.getTitle()));
        ArrayList<String> lackProducts = new ArrayList<>();
        products.forEach(product ->lackProducts.add(product.getTitle()) );
        ObservableList<String> observableList = FXCollections.observableArrayList(lackProducts);
        lackOfProductsListView.setItems(observableList);
    }

}
