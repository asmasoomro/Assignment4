package com.example.assignment4;

import android.content.Context;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ImageViewHolder>{
    private Context itemContext;
    private List<Upload> itemUploads;
    private OnItemCLickListener itemCLickListener;

    public CartAdapter(Context context, List<Upload>uploads){
        itemContext=context;
        itemUploads=uploads;

    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(itemContext).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent = itemUploads.get(position);
        int amount = Integer.parseInt(uploadCurrent.getStock());
        if (amount == 0) {
            holder.textViewName.setText(uploadCurrent.getName() + " OUT OF STOCK");
            holder.textViewName.setTextColor(Color.RED);
        } else {
            holder.textViewName.setText(uploadCurrent.getName() + " €" + uploadCurrent.getPrice());
        }

        // Load image with Picasso
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.drawable.admin)
                .fit()
                .centerInside()
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return itemUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView){
            super(itemView);

            textViewName = itemView.findViewById(R.id.textItem);
            imageView = itemView.findViewById(R.id.itemImage);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemCLickListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    itemCLickListener.OnItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem purch = contextMenu.add(Menu.NONE,1,1,"Purchase");

            purch.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(itemCLickListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    switch(menuItem.getItemId()){
                        case 1:
                            itemCLickListener.onPurchaseCLick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface OnItemCLickListener {

        void onPurchaseCLick(int position);

        void OnItemClick(int position);
    }
    public void setOnItemClickListener(OnItemCLickListener listener){
        itemCLickListener=listener;
    }

}