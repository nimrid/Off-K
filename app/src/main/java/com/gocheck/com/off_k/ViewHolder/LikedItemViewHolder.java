package com.gocheck.com.off_k.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gocheck.com.off_k.Interface.itemClickListener;
import com.gocheck.com.off_k.R;

public class LikedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mHomeCategory, mHomeDescription, mHomeRent, mLocation;
    public itemClickListener itemClickListener;

    public LikedItemViewHolder(@NonNull View itemView) {
        super(itemView);

        mHomeCategory = itemView.findViewById(R.id.text_category);
        mHomeDescription = itemView.findViewById(R.id.text_desc);
        mLocation = itemView.findViewById(R.id.text_locate);
        mHomeRent = itemView.findViewById(R.id.text_rent);
    }

    public void setItemOnClick(itemClickListener itemOnClick){
        this.itemClickListener = itemOnClick;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
