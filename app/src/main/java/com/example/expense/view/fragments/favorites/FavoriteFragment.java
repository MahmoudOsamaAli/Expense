package com.example.expense.view.fragments.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense.R;
import com.example.expense.adapters.SelectedCategoryAdapter;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.pojo.RestaurantModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteFragment extends Fragment {

    @BindView(R.id.RV_favorite_fragment)
    RecyclerView mRV;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite , container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this , view);
        init();
    }

    private void init() {
//        presenter = new FavoritesPresenter(this);
//        presenter.getFavorites();
    }

    private void setmRV(ArrayList<PlaceModel> list){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRV.setLayoutManager(manager);
        SelectedCategoryAdapter adapter = new SelectedCategoryAdapter(getContext() , list);
        mRV.setAdapter(adapter);
    }
}