package sample.Models;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

import java.time.LocalDate;

@Entity(value = "invoice",noClassnameStored=true)
@Indexes(@Index(
        fields = {
                @Field("supplier"),
                @Field("date"),
                @Field("sum"),
                @Field("hasBeenPaid")
        }
))
public class Invoice {
    private Supplier supplier;
    private LocalDate date;
    private double sum;
    private boolean hasBeenPaid;


    public Invoice(Supplier supplier, LocalDate date, double sum, Boolean hasBeenPaid) {
        this.supplier = supplier;
        this.date = date;
        this.sum = sum;
        this.hasBeenPaid = hasBeenPaid;
    }

    public Invoice() {
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public boolean isHasBeenPaid() {
        return hasBeenPaid;
    }

    public void setHasBeenPaid(boolean hasBeenPaid) {
        this.hasBeenPaid = hasBeenPaid;
    }
}
