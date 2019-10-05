package com.example.shopping.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping.R;
import com.example.shopping.activities.ContactUsActivity;
import com.example.shopping.data.Data;
import com.example.shopping.model.HomeViewModel;
import com.example.shopping.adapters.RVHomeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    @BindView(R.id.categories_RV)
    RecyclerView mRV;
    @BindView(R.id.logo_image)
    ImageView logoImage;
    @BindView(R.id.search_view_fragment)
    SearchView mSearchView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this , view);
        init();
    }

    private void init() {
        searchViewConfig();
        ArrayList<HomeViewModel> data = Data.getCategoriesData();
        GridLayoutManager manager = new GridLayoutManager(getContext() , 2);
        mRV.setLayoutManager(manager);
        RVHomeAdapter adapter = new RVHomeAdapter(getContext() , data);
        mRV.setAdapter(adapter);
        logoImage.setOnClickListener(view -> {
            getContext().startActivity(new Intent(getContext() , ContactUsActivity.class));
        });
    }

    private void searchViewConfig() {
        mSearchView.setOnClickListener(view -> mSearchView.setIconified(false));
    }
}