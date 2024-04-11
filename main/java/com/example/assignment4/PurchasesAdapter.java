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

public class PurchasesAdapter extends RecyclerView.Adapter<PurchasesAdapter.ImageViewHolder>{
    private Context itemContext;
    private List<Upload> itemUploads;


    public PurchasesAdapter(Context context, List<Upload>uploads){
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
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewName.setTextColor(Color.RED);

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

    public void filter(List<Upload> filterList) {
        itemUploads = filterList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(PurchasesView purchasesProductView) {
    }

    public interface OnItemCLickListener {
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textItem);
            imageView = itemView.findViewById(R.id.itemImage);


        }
    }
}


