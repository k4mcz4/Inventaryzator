package com.example.inventura;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.inventura.database.DatabaseConnector;
import com.example.inventura.model.ProductModel;
import com.example.inventura.recycler.ProductRecyclerAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class PopupHelper {

    private View popupView;
    private TextInputEditText textBarcode;
    private TextInputEditText textName;
    private TextInputEditText textPrice;

    private boolean insert;

    public boolean isInsert() {
        return insert;
    }


    public PopupHelper(LayoutInflater inflater) {
        popupView = inflater.inflate(R.layout.product_popup_insert_form, null);
        textBarcode = popupView.findViewById(R.id.product_popup_insert_barcode);
        textName = popupView.findViewById(R.id.product_popup_insert_name);
        textPrice = popupView.findViewById(R.id.product_popup_insert_price);
    }

    public void onButtonShowPopupWindowClick(View view,
                                             Object searchString,
                                             ArrayList<ProductModel> productNames,
                                             ProductRecyclerAdapter adapter,
                                             DatabaseConnector dbconn) {

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        Button submitButton = (Button) popupView.findViewById(R.id.product_popup_submit_button);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();
                return true;
            }
        });


        if (searchString instanceof String) {
            try {
                Long.parseLong((String) searchString);
                textBarcode.setText((String) searchString);
            } catch (Exception e) {
                textName.setText((String) searchString);
            }

            insert = true;

        } else if (searchString instanceof ProductModel) {
            textName.setText(((ProductModel) searchString).getName());
            textBarcode.setText(((ProductModel) searchString).getBarcode());
            textPrice.setText(((ProductModel) searchString).getPrice());
            insert = false;
        }


        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isInsert()) {
                    insertProductRecord(dbconn);
                } else {
                    updateProductRecord(dbconn, (ProductModel) searchString);
                }

                productNames.clear();
                productNames.addAll(dbconn.getAllProductsList());
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

    }

    private void updateProductRecord(DatabaseConnector dbconn, ProductModel product) {

        product.setName(textName.getText().toString());
        product.setBarcode(textBarcode.getText().toString());
        product.setPrice_net(Double.parseDouble(textPrice.getText().toString()));

        dbconn.updateProduct(product);
    }

    private void insertProductRecord(DatabaseConnector dbconn) {

        Double priceNet;
        ProductModel product;
        try {
            priceNet = Double.parseDouble(textPrice.getText().toString());
        } catch (Exception e) {
            priceNet = 0.0;
        }
        product = new ProductModel(textBarcode.getText().toString(),
                textName.getText().toString(),
                priceNet);

        dbconn.addNewProduct(product);

    }

}