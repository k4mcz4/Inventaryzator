package com.example.inventura.model;

public class InventoryModel {
    private int _id;
    private ProductModel product;
    private int inventoryQuantity;

    InventoryModel(ProductModel product, int inventoryQuantity) {
        this.product = product;
        this.inventoryQuantity = inventoryQuantity;
    }

    InventoryModel(int id, ProductModel product, int inventoryQuantity) {
        this._id = id;
        this.product = product;
        this.inventoryQuantity = inventoryQuantity;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProductId(ProductModel product) {
        this.product = product;
    }

    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(int inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

}
