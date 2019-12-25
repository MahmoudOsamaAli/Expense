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
import com.example.expense.view.activities.selectedCategory.SelectedCategory;
import com.example.expense.pojo.HomeViewModel;

import java.util.ArrayList;

public class RVHomeAdapter extends RecyclerView.Adapter<RVHomeAdapter.MyHolder> {

    private Context mContext;
    private ArrayList<HomeViewModel> mData;
    private int lastPosition = -1;

    public RVHomeAdapter(Context mContext, ArrayList<HomeViewModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item
                , parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.mText.setText(mData.get(position).getmText());
        holder.mImage.setImageResource(mData.get(position).getmImageId());
        setAnimation(holder.mImage, position);
    }
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView mText;
        ImageView mImage;
        ConstraintLayout parent;
        MyHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            mText = itemView.findViewById(R.id.category_text);
            mImage = itemView.findViewById(R.id.category_image);
            itemView.setOnClickListener(view -> {

                String label = mText.getText().toString();
                Intent i = new Intent(mContext , SelectedCategory.class);
                i.putExtra("label" , label);
                mContext.startActivity(i);
            });
        }
    }
}
