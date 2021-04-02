package com.example.inventura.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventura.R;
import com.example.inventura.database.DatabaseConnector;
import com.example.inventura.product.ProductModel;
import com.example.inventura.recycler.ProductRecyclerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProductFragment extends Fragment {

    private ProductRecyclerAdapter recyclerAdapter;


    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        DatabaseConnector dbconn = new DatabaseConnector(getContext());

        View view = inflater.inflate(R.layout.products_view, container, false);

        ArrayList<ProductModel> productNames = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.product_recycler_view);
        recyclerAdapter = new ProductRecyclerAdapter(view.getContext(), productNames);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        Button scanButton = (Button) view.findViewById(R.id.product_view_scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "EAN_13");
                startActivityForResult(intent, 0);
            }
        });


        TextInputEditText textView = (TextInputEditText) view.findViewById(R.id.product_text_edit);
        TextInputEditText.OnEditorActionListener exampleListener = new TextInputEditText.OnEditorActionListener() {

            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchOrInsert(dbconn, textView, productNames, inflater, view);
                }
                return true;
            }
        };

        textView.setOnEditorActionListener(exampleListener);

        Button button = (Button) view.findViewById(R.id.product_view_ok_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchOrInsert(dbconn, textView, productNames, inflater, view);
            }
        });


        recyclerAdapter.notifyDataSetChanged();


        return view;
    }

    public void searchOrInsert(DatabaseConnector dbconn,
                               TextInputEditText textView,
                               ArrayList<ProductModel> productNames,
                               LayoutInflater inflater, View view) {
        String text = textView.getText().toString();

        if (!text.isEmpty()) {

            productNames.clear();
            productNames.addAll(dbconn.getProducts(text));

            if (productNames.isEmpty()) {
                onButtonShowPopupWindowClick(inflater, view, text);
                productNames.clear();
                productNames.addAll(dbconn.getAllProductsList());
            }
            recyclerAdapter.notifyDataSetChanged();

            clearAndUpdateScreen(textView);

        } else {
            productNames.clear();
            productNames.addAll(dbconn.getAllProductsList());
            recyclerAdapter.notifyDataSetChanged();
        }
        recyclerAdapter.resetClickPosition();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                TextInputEditText textView = (TextInputEditText) getView().findViewById(R.id.product_text_edit);
                textView.setText(contents);

                Button button = (Button) getView().findViewById(R.id.product_view_ok_button);
                button.performClick();

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    private void clearAndUpdateScreen(TextView textView) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        textView.setText("");
        textView.clearFocus();
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void onButtonShowPopupWindowClick(LayoutInflater inflater, View view, String productString) {
        View popupView = inflater.inflate(R.layout.product_popup_insert_form, null);

        TextInputEditText textBarcode = popupView.findViewById(R.id.product_popup_insert_barcode);
        TextInputEditText textName = popupView.findViewById(R.id.product_popup_insert_name);
        TextInputEditText textPrice = popupView.findViewById(R.id.product_popup_insert_price);

        DatabaseConnector dbconn = new DatabaseConnector(getContext());

        try {
            Long.parseLong(productString);
            textBarcode.setText(productString);
        } catch (Exception e) {
            textName.setText(productString);
        }

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        Button submitButton = (Button) popupView.findViewById(R.id.product_popup_submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Double priceNet;
                ProductModel product;
                try {
                    priceNet = Double.parseDouble(textPrice.getText().toString());
                } catch (Exception e) {
                    priceNet = 0.0;
                }
                product = new ProductModel(textBarcode.getText().toString(), textName.getText().toString(), priceNet);

                dbconn.addNewProduct(product);
                popupWindow.dismiss();
                recyclerAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
