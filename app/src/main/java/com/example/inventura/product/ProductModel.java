package com.example.inventura.product;

public class ProductModel {
    private int _id;
    private String barcode;
    private String name;
    private int quantity;
    private double price_net;
    private float price_gross;
    private boolean kg;

    public ProductModel(String barcode){
        this.barcode = barcode;
    }

    public ProductModel(String barcode, String name, int quantity, boolean kg){
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.kg = kg;
    }

    public ProductModel(String barcode, String name, double price_net){
        this.barcode = barcode;
        this.name = name;
        this.price_net = price_net;
    }

    public String getBarcode() {
        return barcode;
    }


    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return String.valueOf(price_net);
    }

    public boolean isKg() {
        return kg;
    }

    public void getProductData(){

    }

}
