package com.example.expense.view.activities.displayImage;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.expense.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayImage extends AppCompatActivity {

    @BindView(R.id.viewpager_full_screen)
    ViewPager viewPager;
    int position;
    private static final String TAG = "DisplayImage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        position = getIntent().getIntExtra("position" ,0);
        Log.i(TAG, "init: image position = "+position);
    }
}
