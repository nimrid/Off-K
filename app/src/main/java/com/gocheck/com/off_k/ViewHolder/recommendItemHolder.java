package com.gocheck.com.off_k.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gocheck.com.off_k.Interface.itemClickListener;
import com.gocheck.com.off_k.Modal.Accommodation;
import com.gocheck.com.off_k.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class recommendItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Accommodation mAccommodation;
    public TextView mHomeName, mHomeRent, mLocation;
    public ImageView mHomeImage;
    public com.gocheck.com.off_k.Interface.itemClickListener itemClickListener;

    public recommendItemHolder(@NonNull View itemView) {
        super(itemView);

        mHomeName = itemView.findViewById(R.id.rec_home_name);
        mHomeImage = itemView.findViewById(R.id.rec_home_image);
        mHomeRent = itemView.findViewById(R.id.rec_home_price);
        mLocation = itemView.findViewById(R.id.rec_home_location);
    }

    public void bind(Accommodation accommodation){
        mAccommodation = accommodation;
        Picasso.get().load(accommodation.getImageUri()).into(mHomeImage);
        mHomeName.setText(accommodation.getPropertyName());
        mHomeRent.setText(accommodation.getRent());
        mLocation.setText(accommodation.getLocation());
//            mLocation.setText(tour.getLocation());
    }

    public void setItemOnClick(itemClickListener itemOnClick){
        this.itemClickListener = itemOnClick;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }


    public static class Adapter extends RecyclerView.Adapter<recommendItemHolder>{
        private List<Accommodation> mAccomodation;
        private Context mContext;

        public Adapter(Context context, List<Accommodation> accommodations){
            mContext = context;
            mAccomodation= accommodations;
        }

        @NonNull
        @Override
        public recommendItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.recommend_items, parent, false);
            return new recommendItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull recommendItemHolder holder, int position) {
            Accommodation tour = mAccomodation.get(position);
            holder.bind(tour);
        }

        @Override
        public int getItemCount() {
            return mAccomodation.size();
        }
    }
}
