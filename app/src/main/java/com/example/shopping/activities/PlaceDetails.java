package com.example.shopping.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.adapters.LocationAdapter;
import com.example.shopping.adapters.MyPagerAdapter;
import com.example.shopping.data.Data;
import com.example.shopping.model.PlaceImage;
import com.example.shopping.model.locationModel;
import com.mancj.slimchart.SlimChart;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class PlaceDetails extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private MyPagerAdapter myPager;
    @BindView(R.id.seekBar_likes)
    SeekBar seekBar1;
    @BindView(R.id.seekBar_okays)
    SeekBar seekBar2;
    @BindView(R.id.seekBar_dislikes)
    SeekBar seekBar3;
    @BindView(R.id.likes_text)
    TextView likeText;
    @BindView(R.id.okays_text)
    TextView okeyText;
    @BindView(R.id.dislikes_text)
    TextView disLikeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        slimChartConfig(8.8);
        seekBarConfig(85 , 6 , 9);
        ArrayList<PlaceImage> data = Data.getPlaceImages();
        myPager = new MyPagerAdapter(this, data);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(myPager);
        viewPager.setCurrentItem(myPager.getCount() - 1);
        circleIndicator = findViewById(R.id.circle);
        circleIndicator.setViewPager(viewPager);


        ArrayList<locationModel> mData = Data.getLocations();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.RV_places_location);
        recyclerView.setLayoutManager(manager);
        LocationAdapter adapter1 = new LocationAdapter(this, mData);
        recyclerView.setAdapter(adapter1);

    }

    private void seekBarConfig(int value1 , int value2 , int value3) {
        seekBar1.setMax(100);
        seekBar2.setMax(100);
        seekBar3.setMax(100);
        String likes = value1 + getResources().getString(R.string.likes_format);
        String okays = value2 + getResources().getString(R.string.okay_format);
        String dislikes = value3 + getResources().getString(R.string.dislikes_format);
        likeText.setText(likes);
        okeyText.setText(okays);
        disLikeText.setText(dislikes);
        ValueAnimator anim1 = ValueAnimator.ofInt(0, value1);
        anim1.setDuration(1500);
        ValueAnimator anim2 = ValueAnimator.ofInt(0, value2);
        anim1.setDuration(1500);
        ValueAnimator anim3 = ValueAnimator.ofInt(0, value3);
        anim1.setDuration(1500);
        anim1.addUpdateListener(animation -> seekBar1.setProgress((Integer) animation.getAnimatedValue()));
        anim1.start();
        anim2.addUpdateListener(animation -> seekBar2.setProgress((Integer) animation.getAnimatedValue()));
        anim2.start();
        anim3.addUpdateListener(animation -> seekBar3.setProgress((Integer) animation.getAnimatedValue()));
        anim3.start();
    }

    private void slimChartConfig(double state) {
        SlimChart slimChart = findViewById(R.id.slimChart);

        int[] colors = new int[4];
        colors[0] = Color.rgb(185, 185,185);
        colors[1] = Color.rgb(23, 246, 9);
        slimChart.setColors(colors);

        final float[] stats = new float[2];
        stats[0] = 100;
        stats[1] = (float) state *10;
        slimChart.setStats(stats);
        //Play animation
        slimChart.setStartAnimationDuration(1500);
        slimChart.playStartAnimation();

        slimChart.setText(Double.toString(state));
        slimChart.setRoundEdges(true);
    }
}
