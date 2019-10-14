package com.example.shopping.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.shopping.R;
import com.example.shopping.adapters.RVHomeAdapter;
import com.example.shopping.adapters.SelectedCategoryAdapter;
import com.example.shopping.data.Data;
import com.example.shopping.fragments.HomeFragment;
import com.example.shopping.model.HomeViewModel;
import com.example.shopping.model.RestaurantModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedCategory extends AppCompatActivity {

    @BindView(R.id.RV_selected_category)
    RecyclerView mRV;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.selected_category_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchViewConfig();
        RVConfig();
        toolBarConfig();
    }

    private void toolBarConfig() {
        if(toolbar != null) {
            String label = getIntent().getStringExtra("label");
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(label);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(v -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            });
        }
    }

    private void RVConfig() {
        ArrayList<RestaurantModel> data = Data.getSelectedCategoryData();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRV.setLayoutManager(manager);
        SelectedCategoryAdapter adapter = new SelectedCategoryAdapter(this, data);
        mRV.setAdapter(adapter);
    }

    private void searchViewConfig() {
        mSearchView.setOnClickListener(view -> mSearchView.setIconified(false));
    }

}
