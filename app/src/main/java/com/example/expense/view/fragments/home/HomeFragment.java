package com.example.expense.view.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense.R;
import com.example.expense.pojo.HomeViewModel;
import com.example.expense.adapters.RVHomeAdapter;
import com.example.expense.view.activities.Home.HomeActivity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    @BindView(R.id.categories_RV)
    RecyclerView mRV;
    @BindView(R.id.search_view_fragment)
    SearchView mSearchView;
    private HomeActivity mCurrent;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this , view);
        init();
    }

    private void init() {
        mCurrent = (HomeActivity) getActivity();
        searchViewConfig();
        ArrayList<HomeViewModel> list = createHomeViewList();
        RVConfig(list);
    }

    private ArrayList<HomeViewModel> createHomeViewList() {
        ArrayList<HomeViewModel> data = new ArrayList<>();
        String[] array = mCurrent.getResources().getStringArray(R.array.categories);

        ArrayList<String> categories = new ArrayList<>(Arrays.asList(array));

        for (String category : categories) {
            if (category.matches(getString(R.string.coffee)))
                data.add(new HomeViewModel(category, R.drawable.cafe));
            else if (category.matches(getString(R.string.electronics))) {
                data.add(new HomeViewModel(category, R.drawable.electronics_2));
            } else if (category.matches(getString(R.string.restaurant))) {
                data.add(new HomeViewModel(category, R.drawable.restaurants_3));
            } else if (category.matches(getString(R.string.clothes))) {
                data.add(new HomeViewModel(category, R.drawable.clothes));
            } else if (category.matches(getString(R.string.beauty))) {
                data.add(new HomeViewModel(category, R.drawable.makeup));
            } else if (category.matches(getString(R.string.others))) {
                data.add(new HomeViewModel(category, R.drawable.shopping));
            }
        }
        return data;
    }

    private void RVConfig(ArrayList<HomeViewModel> data) {
        LinearLayoutManager manager = new LinearLayoutManager(mCurrent);
        mRV.setLayoutManager(manager);
        RVHomeAdapter adapter = new RVHomeAdapter(getContext() , data);
        mRV.setAdapter(adapter);
    }
    private void searchViewConfig() {
        mSearchView.setOnClickListener(view -> mSearchView.setIconified(false));
    }
}