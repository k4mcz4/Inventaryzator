package com.example.inventura.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.inventura.product.ProductModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class DatabaseConnector extends SQLiteOpenHelper {

    private static final String PRODUCT_TABLE = "PRODUCT_TABLE";
    private static final String COLUMN_PRODUCT_ID = "PRODUCT_ID";
    private static final String COLUMN_PRODUCT_BARCODE = "PRODUCT_BARCODE";
    private static final String COLUMN_PRODUCT_NAME = "PRODUCT_NAME";
    private static final String COLUMN_IS_KG = "COLUMN_IS_KG";
    private static final String COLUMN_PRICE_NET = "PRICE_NET";
    private static final String COLUMN_PRICE_GROSS = "PRICE_GROSS";

    private static final String stringCreateProductTable =
            String.format(
                    "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, %s TEXT, %s TEXT, %s REAL, %s REAL, %s NUMERIC)",
                    PRODUCT_TABLE, COLUMN_PRODUCT_ID, COLUMN_PRODUCT_BARCODE, COLUMN_PRODUCT_NAME, COLUMN_PRICE_NET, COLUMN_PRICE_GROSS, COLUMN_IS_KG);

    private static final String INVENTORY_TABLE = "INVENTORY_TABLE";
    private static final String COLUMN_INVENTORY_ID = "INVENTORY_ID";
    private static final String COLUMN_INVENTORY_QTY = "INVENTORY_QTY";


    private static final String createInventoryTable = "";


    public DatabaseConnector(@Nullable Context context) {
        super(context, "inventura.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(stringCreateProductTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addNewProduct(ProductModel productModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PRODUCT_BARCODE, productModel.getBarcode());
        cv.put(COLUMN_PRODUCT_NAME, productModel.getName());
        cv.put(COLUMN_PRICE_NET, productModel.getPrice());
        //cv.put(COLUMN_IS_KG, productModel.isKg());


        return db.insert(PRODUCT_TABLE, "", cv);
    }

    public ArrayList<ProductModel> getProducts(String searchString) {

        String sql;

        try{
            Long.parseLong(searchString);

            if(searchString.length() == 13){
                sql = String.format("SELECT %s, %s, %s FROM %s WHERE %s = '%s'",
                        COLUMN_PRODUCT_BARCODE,
                        COLUMN_PRODUCT_NAME,
                        COLUMN_PRICE_NET,
                        PRODUCT_TABLE,
                        COLUMN_PRODUCT_BARCODE,
                        searchString);
            } else {
                sql = String.format("SELECT %s, %s, %s FROM %s WHERE %s like '%s%%'",
                        COLUMN_PRODUCT_BARCODE,
                        COLUMN_PRODUCT_NAME,
                        COLUMN_PRICE_NET,
                        PRODUCT_TABLE,
                        COLUMN_PRODUCT_BARCODE,
                        searchString);
            }

        } catch (Exception e){
            sql = String.format("SELECT %s, %s, %s FROM %s WHERE %s LIKE '%%%s%%'",
                    COLUMN_PRODUCT_BARCODE,
                    COLUMN_PRODUCT_NAME,
                    COLUMN_PRICE_NET,
                    PRODUCT_TABLE,
                    COLUMN_PRODUCT_NAME,
                    searchString);
        }


        return selectQuery(sql);
    }

    public ArrayList<ProductModel> getAllProductsList() {
        String sql = String.format("SELECT %s, %s, %s FROM %s",
                COLUMN_PRODUCT_BARCODE,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRICE_NET,
                PRODUCT_TABLE);

        return selectQuery(sql);
    }

    public void deleteProduct(ProductModel product){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(PRODUCT_TABLE,COLUMN_PRODUCT_BARCODE + "=?", new String[] {product.getBarcode()});

        db.close();
    }


    private ArrayList<ProductModel> selectQuery(String sql) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ProductModel> productList = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            String barcode = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_BARCODE));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
            double price_net = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_PRICE_NET)));

            productList.add(new ProductModel(barcode, name, price_net));
        }

        db.close();

        return productList;
    }
}

