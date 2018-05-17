package sample;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import sample.Models.Invoice;
import sample.Models.Product;
import sample.Models.Supplier;

import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseHelper {

    private final Morphia morphia = new Morphia();
    private static String DATABASE_NAME = "Super_Market_Pos";
    final Datastore datastore = morphia.createDatastore(new MongoClient(), DATABASE_NAME);

    public DatabaseHelper() {
        morphia.mapPackage("org.mongodb.morphia.example");
        this.datastore.ensureIndexes();
    }

    public Datastore getDatastore() {
        return this.datastore;
    }

    public HashMap<Long, Product> getProductsMap() {
        HashMap<Long, Product> productMap = new HashMap<>();
        final Query query = getDatastore().find(Product.class);

        query.fetch().forEach(obj -> {
            Product p = (Product) obj;
            productMap.put(p.getBarcode(), p);

        });

        return productMap;
    }

    public HashMap<Long, Supplier> getSuppliersMap() {
        HashMap<Long, Supplier> suppliersMap = new HashMap<>();
        final Query query = getDatastore().find(Supplier.class);

        query.fetch().forEach(obj -> {
            Supplier p = (Supplier) obj;
            suppliersMap.put(p.getVatNumber(), p);

        });

        return suppliersMap;
    }

    public HashMap<String, Supplier> getSuppliersMapNames() {
        HashMap<String, Supplier> suppliersMap = new HashMap<>();
        final Query query = getDatastore().find(Supplier.class);

        query.fetch().forEach(obj -> {
            Supplier p = (Supplier) obj;
            suppliersMap.put(p.getName(), p);

        });

        return suppliersMap;
    }

    public ArrayList<Product> getProductsList() {
        return (ArrayList<Product>) getDatastore().createQuery(Product.class).asList(new FindOptions());
    }

    public ArrayList<Supplier> getSuppliersList() {
        return (ArrayList<Supplier>) getDatastore().createQuery(Supplier.class).asList(new FindOptions());
    }

    public ArrayList<Long> getBarcodes() {
        ArrayList<Long> barcodes = new ArrayList<>();
        datastore.find(Product.class).retrievedFields(true, "barcode").forEach(obj -> {
            barcodes.add(obj.getBarcode());
        });
        return barcodes;
    }

    public ArrayList<Long> getVatNumbers() {
        ArrayList<Long> suppliers = new ArrayList<>();
        datastore.find(Supplier.class).retrievedFields(true, "vatNumber").forEach(obj -> {
            suppliers.add(obj.getVatNumber());
        });
        return suppliers;
    }

    public Product getSpecificProductByBarcode(long barcode) {

        return datastore.createQuery(Product.class).field("barcode").equal(barcode).get();
    }

    public ArrayList<Invoice> getLatestInvoices() {
        return (ArrayList<Invoice>) getDatastore().createQuery(Invoice.class).asList(new FindOptions().limit(15));
    }

    public ArrayList<Product> getLackOfProductsList(){
        ArrayList<Product> products = getProductsList();
        ArrayList<Product> lackProducts = new ArrayList<>();
        for(Product p : products)
            if(p.getCurrentQuantity()<p.getMinQuantity())
                lackProducts.add(p);

        return lackProducts;
    }

}
