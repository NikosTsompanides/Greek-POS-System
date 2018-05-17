package sample.Models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;

@Entity(value="receipts",noClassnameStored=true)
@Indexes(@Index(fields = {
        @Field("receipt")
}))
public class Receipt {

    @Id
    private ObjectId id;

    public ArrayList<Product> productsOfReceipt;

    public Receipt() {
    }

    public Receipt(ArrayList<Product> productsOfReceipt) {
        this.productsOfReceipt = productsOfReceipt;
    }

    public ArrayList<Product> getProductsOfReceipt() {
        return productsOfReceipt;
    }

    public void setProductsOfReceipt(ArrayList<Product> productsOfReceipt) {
        this.productsOfReceipt = productsOfReceipt;
    }
}
