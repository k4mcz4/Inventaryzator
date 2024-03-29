package com.example.inventura.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventura.PopupHelper;
import com.example.inventura.R;
import com.example.inventura.database.DatabaseConnector;
import com.example.inventura.model.ProductModel;

import java.util.ArrayList;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {

    private ArrayList<ProductModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int clickPosition = -1;

    public ProductRecyclerAdapter(Context context, ArrayList<ProductModel> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void resetClickPosition(){
        this.clickPosition = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.product_card, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatabaseConnector dbconn = new DatabaseConnector(holder.itemView.getContext());
        ProductModel product = mData.get(position);
        holder.barcodeTextView.setText(product.getBarcode());
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(product.getPrice());
        holder.linearLayout.setVisibility(View.INVISIBLE);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                clickPosition = position;
                notifyDataSetChanged();
                return false;
            }
        });


        if(clickPosition == position){
            holder.linearLayout.setVisibility(View.VISIBLE);
        }

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopupHelper(mInflater).onButtonShowPopupWindowClick(v,mData.get(position),mData,ProductRecyclerAdapter.this,dbconn);
                resetClickPosition();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dbconn.deleteProduct(product);
                mData.remove(product);
                resetClickPosition();
                notifyDataSetChanged();
            }
        });


    }



    @Override
    public int getItemCount() {
        int count;
        try {
            count = mData.size();
        } catch(Exception e) {
            count = 0;
        }

        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView barcodeTextView;
        TextView nameTextView;
        TextView priceTextView;
        LinearLayout linearLayout;
        Button deleteButton;
        Button editButton;

        ViewHolder(View itemView){
            super(itemView);
            barcodeTextView = itemView.findViewById(R.id.product_card_barcode);
            nameTextView = itemView.findViewById(R.id.product_card_name);
            priceTextView = itemView.findViewById(R.id.product_card_price);
            linearLayout = itemView.findViewById(R.id.product_card_hidden_buttons);

            deleteButton = itemView.findViewById(R.id.product_card_delete_button);
            editButton = itemView.findViewById(R.id.product_card_edit_button);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view){
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            resetClickPosition();
            notifyDataSetChanged();
        }

    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
