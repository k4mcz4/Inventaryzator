package com.example.inventura.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.inventura.model.InventoryModel;
import com.example.inventura.model.ProductModel;
import com.example.inventura.model.StockTakeModel;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DatabaseConnector extends SQLiteOpenHelper {


    private static final String INVENTORY_TABLE = "INVENTORY_TABLE";
    private static final String COLUMN_INVENTORY_ID = "INVENTORY_ID";
    private static final String COLUMN_INVENTORY_PRODUCT_ID = "INVENTORY_PRODUCT_ID";
    private static final String COLUMN_INVENTORY_QTY = "INVENTORY_QTY";
    private static final String COLUMN_INVENTORY_ST_ID = "INVENTORY_ST_ID";

    private static final String stringCreateInventoryTable =
            String.format(
                    "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, %s INTEGER, %s INTEGER, %s INTEGER)",
                    INVENTORY_TABLE,
                    COLUMN_INVENTORY_ID,
                    COLUMN_INVENTORY_PRODUCT_ID,
                    COLUMN_INVENTORY_QTY,
                    COLUMN_INVENTORY_ST_ID);

    private static final String STOCK_TAKE_TABLE = "STOCK_TAKE_TABLE";
    private static final String COLUMN_ST_ID = "STOCK_TAKE_ID";
    private static final String COLUMN_ST_STARTDATE = "STOCK_TAKE_STARTDATE";
    private static final String COLUMN_ST_ENDDATE = "STOCK_TAKE_ENDTDATE";

    private static final String stringCreateStockTakeTable =
            String.format(
                    "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, %s TEXT, %s TEXT)",
                    STOCK_TAKE_TABLE,
                    COLUMN_ST_ID,
                    COLUMN_ST_STARTDATE,
                    COLUMN_ST_ENDDATE);

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
                    PRODUCT_TABLE,
                    COLUMN_PRODUCT_ID,
                    COLUMN_PRODUCT_BARCODE,
                    COLUMN_PRODUCT_NAME,
                    COLUMN_PRICE_NET,
                    COLUMN_PRICE_GROSS,
                    COLUMN_IS_KG);

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

    public void addNewProduct(ProductModel productModel) {


        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PRODUCT_BARCODE, productModel.getBarcode());
        cv.put(COLUMN_PRODUCT_NAME, productModel.getName());
        cv.put(COLUMN_PRICE_NET, productModel.getPrice());

        db.insert(PRODUCT_TABLE, "", cv);
        db.close();
    }

    public ArrayList<ProductModel> getProducts(String searchString) {

        String sql;

        try {
            Long.parseLong(searchString);

            if (searchString.length() == 13 || searchString.length() == 8) {
                sql = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s = '%s'",
                        COLUMN_PRODUCT_ID,
                        COLUMN_PRODUCT_BARCODE,
                        COLUMN_PRODUCT_NAME,
                        COLUMN_PRICE_NET,
                        PRODUCT_TABLE,
                        COLUMN_PRODUCT_BARCODE,
                        searchString);
            } else {
                sql = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s like '%s%%'",
                        COLUMN_PRODUCT_ID,
                        COLUMN_PRODUCT_BARCODE,
                        COLUMN_PRODUCT_NAME,
                        COLUMN_PRICE_NET,
                        PRODUCT_TABLE,
                        COLUMN_PRODUCT_BARCODE,
                        searchString);
            }

        } catch (Exception e) {
            sql = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s LIKE '%%%s%%'",
                    COLUMN_PRODUCT_ID,
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
        String sql = String.format("SELECT %s, %s, %s, %s FROM %s",
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_BARCODE,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRICE_NET,
                PRODUCT_TABLE);

        return selectQuery(sql);
    }

    public void deleteProduct(ProductModel product) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(PRODUCT_TABLE, COLUMN_PRODUCT_BARCODE + "=?", new String[]{product.getBarcode()});

        db.close();
    }

    public void updateProduct(ProductModel product) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCT_BARCODE, product.getBarcode());
        cv.put(COLUMN_PRODUCT_NAME, product.getName());
        cv.put(COLUMN_PRICE_NET, product.getPrice());
        db.update(PRODUCT_TABLE, cv, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(product.get_id())});
        db.close();
    }


    private ArrayList<ProductModel> selectQuery(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ProductModel> productList = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            String barcode = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_BARCODE));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
            double price_net = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_PRICE_NET)));
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID));

            productList.add(new ProductModel(id, barcode, name, price_net));
        }

        db.close();

        return productList;
    }

/*    public ArrayList<InventoryModel> getInventoryList() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<InventoryModel> inventoryList = new ArrayList<>();

        String sql = String.format("SELECT %s, %s, %s, FROM %s",
                COLUMN_INVENTORY_ID,
                COLUMN_INVENTORY_PRODUCT_ID,
                COLUMN_INVENTORY_QTY,
                INVENTORY_TABLE);

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_INVENTORY_ID));
            int productId = cursor.getInt(cursor.getColumnIndex(COLUMN_INVENTORY_ID));
            int inventoryQty = cursor.getInt(cursor.getColumnIndex(COLUMN_INVENTORY_QTY));
        }


    }*/

    private int getCurrentStockTake() {
        SQLiteDatabase db = getReadableDatabase();

        String sql =
                String.format("SELECT MAX(%s) FROM %s WHERE %s IS NULL",
                        COLUMN_ST_ID,
                        INVENTORY_TABLE,
                        COLUMN_ST_ENDDATE);

        Cursor cursor = db.rawQuery(sql, null);

        Integer id = cursor.getInt(cursor.getColumnIndex(COLUMN_ST_ID));

        if (id == null) {
            id = 0;
        }

        return id;
    }

    public StockTakeModel startNewStockTake() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);

        cv.put(COLUMN_ST_STARTDATE, ts);

        long id = db.insert(STOCK_TAKE_TABLE, "", cv);

        return new StockTakeModel(id, ts);

    }

    public void endCurrentStockTake(StockTakeModel stockTakeModel) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);

        cv.put(COLUMN_ST_ENDDATE, ts);
        db.insert(STOCK_TAKE_TABLE, "", cv);

    }


}

