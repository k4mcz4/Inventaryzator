package com.example.inventura.product;

public class ProductModel {
    private int _id;
    private long barcode;
    private String name;
    private int quantity;
    private float price_net;
    private float price_gross;
    private boolean kg;

    public ProductModel(long barcode){
        this.barcode = barcode;
    }

    public ProductModel(long barcode, String name, int quantity) {
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.kg = false;
    }

    public ProductModel(long barcode, String name, int quantity, boolean kg){
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.kg = kg;
    }

    public long getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isKg() {
        return kg;
    }

    public void getProductData(){

    }

}
