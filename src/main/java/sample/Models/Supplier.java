package sample.Models;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

import java.util.ArrayList;
import java.util.HashMap;

@Entity(value="suppliers",noClassnameStored=true)
@Indexes(@Index(
        fields = {
                @Field("name"),
                @Field("balance"),
                @Field("vatNumber"),
                @Field("invoices"),
        }
))
public class Supplier {

    private ArrayList<Invoice> invoices;
    private String name;
    private double balance;
    private long vatNumber;


    public Supplier() {
    }

    public Supplier(String name, long vatNumber) {
        this.name = name;
        this.vatNumber = vatNumber;
        this.invoices = new ArrayList<>();
        this.balance = 0;
    }

    public Supplier(String name, double balance, long vatNumber) {
        this.name = name;
        this.balance = balance;
        this.vatNumber = vatNumber;
        this.invoices = new ArrayList<>();
    }

    public Supplier(ArrayList<Invoice> invoices, String name, double balance, HashMap<String, Double> payments, long vatNumber) {
        this.invoices = invoices;
        this.name = name;
        this.balance = balance;
        this.vatNumber = vatNumber;
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(long vatNumber) {
        this.vatNumber = vatNumber;
    }
}
