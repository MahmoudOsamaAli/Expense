package com.example.expense.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.expense.R;
import com.example.expense.view.activities.displayImage.DisplayImage;
import com.example.expense.pojo.PlaceImage;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<PlaceImage> mData;
    LayoutInflater mLayoutInflater;
    private static final String TAG = "MyPagerAdapter";

    public MyPagerAdapter(Context mContext, ArrayList<PlaceImage> mData ) {
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
        view.setOnClickListener(view1 -> {
            Intent i = new Intent(mContext , DisplayImage.class);
            i.putExtra("position" , position);
            mContext.startActivity(i);
        });
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
