package com.example.shopping.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.shopping.R;
import com.example.shopping.adapters.RVHomeAdapter;
import com.example.shopping.adapters.SelectedCategoryAdapter;
import com.example.shopping.data.Data;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        try {
            searchViewConfig();
            ArrayList<RestaurantModel> data = Data.getSelectedCategoryData();
            LinearLayoutManager manager = new LinearLayoutManager(this);
            mRV.setLayoutManager(manager);
            SelectedCategoryAdapter adapter = new SelectedCategoryAdapter(this , data);
            mRV.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void searchViewConfig() {
        mSearchView.setOnClickListener(view -> mSearchView.setIconified(false));
    }
}
