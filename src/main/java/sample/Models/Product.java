package sample.Models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.text.DecimalFormat;

@Entity(value="products",noClassnameStored=true)
@Indexes(@Index(
        fields = {
                @Field("title"),
                @Field("category"),
                @Field("barcode"),
                @Field("initialPrice"),
                @Field("sellingPrice"),
                @Field("taxRate"),
                @Field("profitRate"),
                @Field("minQuantity"),
                @Field("currentQuantity"),
                @Field("sumOfSales"),
                @Field("priorityLevel")}
))
public class Product implements Serializable,Comparable<Product> {
    private String title, category;
    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    private long barcode;

    private double initialPrice, sellingPrice, priorityLevel;

    private int taxRate, profitRate, minQuantity, currentQuantity, quantityForSell, sumOfSales;


    public Product() {
    }

    public Product(String title, long barcode, String category, double initialPrice, double sellingPrice, int taxRate, int profitRate, int minQuantity, int currentQuantity) {
        this.title = title;
        this.barcode = barcode;
        this.category = category;
        this.initialPrice = initialPrice;
        this.sellingPrice = sellingPrice;
        this.taxRate = taxRate;
        this.profitRate = profitRate;
        this.minQuantity = minQuantity;
        this.currentQuantity = currentQuantity;
        this.sumOfSales = 0;
        this.priorityLevel = calculatePriorityLevel();
        this.quantityForSell = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(double initialPrice) {
        this.initialPrice = initialPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public int getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(int profitRate) {
        this.profitRate = profitRate;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public double getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(double priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public int getSumOfSales() {
        return sumOfSales;
    }

    public void setSumOfSales(int sumOfSales) {
        this.sumOfSales = sumOfSales;
    }

    public double calculatePriorityLevel() {
        return initialPrice * 0.5 + sumOfSales * 0.3 + taxRate * 0.2;
    }

    public java.lang.reflect.Field[] getProductFields() {
        return getClass().getDeclaredFields();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getQuantityForSell() {
        return quantityForSell;
    }

    public void setQuantityForSell(int quantityForSell) {
        this.quantityForSell = quantityForSell;
    }

    public String getParametersGreekName(String field) {
        switch (field) {
            case "title":
                return "Τίτλος";
            case "barcode":
                return "Barcode";
            case "initialPrice":
                return "Αρχική Τιμή";
            case "sellingPrice":
                return "Τιμή Πώλησης";
            case "taxRate":
                return "Φ.Π.Α";
            case "profitRate":
                return "Ποσοστό Κέρδους";
            case "minQuantity":
                return "Ελάχιστη Ποσότητα";
            case "currentQuantity":
                return "Τρέχουσα Ποσότητα";
            case "sumOfSales":
                return "Άθροισμα Πωλήσεων";
            case "priorityLevel":
                return "Αριθμός Προτεραιότητας";
            case "category":
                return "Κατηγορία";
        }
        return "";
    }

    public Object getParameterValue(String field) {
        DecimalFormat df2 = new DecimalFormat("##.##");
        switch (field) {
            case "title":
                return this.getTitle();
            case "barcode":
                return this.getBarcode();
            case "initialPrice":
                return df2.format(this.getInitialPrice());
            case "sellingPrice":
                return df2.format(this.getSellingPrice());
            case "taxRate":
                return this.getTaxRate();
            case "profitRate":
                return this.getProfitRate();
            case "minQuantity":
                return this.getMinQuantity();
            case "currentQuantity":
                return this.getCurrentQuantity();
            case "sumOfSales":
                return this.getSumOfSales();
            case "priorityLevel":
                return df2.format(this.getPriorityLevel());
            case "category":
                return this.getCategory();
        }
        return "";
    }

    @Override
    public int compareTo(Product other) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this == other||this.getPriorityLevel()== other.getPriorityLevel()) return EQUAL;
        if (this.priorityLevel < other.getPriorityLevel()) return AFTER;
        if (this.priorityLevel > other.getPriorityLevel()) return BEFORE;
        return EQUAL;
    }

}
