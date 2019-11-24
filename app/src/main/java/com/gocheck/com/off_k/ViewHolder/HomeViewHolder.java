package com.gocheck.com.off_k.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gocheck.com.off_k.Interface.itemClickListener;
import com.gocheck.com.off_k.R;

public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mHomeName, mHomeDescription, mHomeRent, mLocation, mVerify;
    public ImageView mHomeImage;
    public itemClickListener itemClickListener;

    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);

        mHomeName = itemView.findViewById(R.id.rec_home_name);
        mHomeDescription = itemView.findViewById(R.id.home_desc);
        mHomeImage = itemView.findViewById(R.id.rec_home_image);
        mHomeRent = itemView.findViewById(R.id.rec_home_price);
        mLocation = itemView.findViewById(R.id.rec_home_location);
        mVerify = itemView.findViewById(R.id.home_verify);
    }

    public void setItemOnClick(itemClickListener itemOnClick){
        this.itemClickListener = itemOnClick;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
