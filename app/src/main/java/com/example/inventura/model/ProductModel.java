package com.example.inventura.model;

public class ProductModel {
    private int _id;
    private String barcode;
    private String name;
    private double price_net;
    private float price_gross;
    private boolean kg;

    public ProductModel(String barcode) {
        this.barcode = barcode;
    }

    public ProductModel(String barcode, String name, boolean kg) {
        this.barcode = barcode;
        this.name = name;
        this.kg = kg;
    }

    public ProductModel(String barcode, String name, double price_net) {
        this.barcode = barcode;
        this.name = name;
        this.price_net = price_net;
    }

    public ProductModel(int id, String barcode, String name, double price_net) {
        this._id = id;
        this.barcode = barcode;
        this.name = name;
        this.price_net = price_net;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public int get_id() {
        return this._id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setPrice_net(double price_net) {
        this.price_net = price_net;
    }

    public String getBarcode() {
        return barcode;
    }


    public String getName() {
        return name;
    }

    public String getPrice() {
        return String.valueOf(price_net);
    }

    public boolean isKg() {
        return kg;
    }


}
