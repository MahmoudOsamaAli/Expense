package com.example.shopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.shopping.R;
import com.example.shopping.model.PlaceImage;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<PlaceImage> mData;
    LayoutInflater mLayoutInflater;

    public MyPagerAdapter(Context mContext, ArrayList<PlaceImage> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.place_image_item ,container , false);
        ImageView imageView = view.findViewById(R.id.place_images);
        imageView.setImageResource(mData.get(position).getmImage());
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
