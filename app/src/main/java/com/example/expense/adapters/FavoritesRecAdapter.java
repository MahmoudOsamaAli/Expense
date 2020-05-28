package com.example.expense.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense.R;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.view.activities.placeDetails.PlaceDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesRecAdapter extends RecyclerView.Adapter<FavoritesRecAdapter.MyHolder> {

    private Context mContext;
    private ArrayList<PlaceModel> data;
    private int lastPosition = -1;

    public FavoritesRecAdapter(Context mContext, ArrayList<PlaceModel> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public FavoritesRecAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_rec_row_item
                , parent, false);
        return new FavoritesRecAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesRecAdapter.MyHolder holder, int position) {
        try {
            holder.mName.setText(data.get(position).getName());
            holder.mDescriptionTV.setText(data.get(position).getDescription());
            Picasso.get().load(data.get(position).getImagesURL().get(0)).into(holder.mImage);
//            setAnimation(holder.parent , position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.FRRI_place_name)
        TextView mName;

        @BindView(R.id.FRRI_description_preview)
        TextView mDescriptionTV;

        @BindView(R.id.FRRI_place_image)
        ImageView mImage;

        @BindView(R.id.FRRI_parent)
        ConstraintLayout parent;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);

                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, PlaceDetails.class);
                    intent.putExtra(mContext.getString(R.string.place_intent_lbl), data.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
