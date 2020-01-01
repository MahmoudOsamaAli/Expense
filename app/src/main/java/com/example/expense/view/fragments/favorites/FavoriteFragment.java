package com.example.expense.view.fragments.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense.R;
import com.example.expense.adapters.FavoritesRecAdapter;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.view.activities.Home.HomeActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteFragment extends Fragment implements FavoritesView {

    @BindView(R.id.RV_favorite_fragment)
    RecyclerView mRV;
    @BindView(R.id.fragment_favorite_view_animator)
    ViewAnimator mViewAnimator;

    private ArrayList<PlaceModel> places;

    private FavoritesRecAdapter adapter;
    FavoritesPresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            ButterKnife.bind(this, view);

            init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() {

        try {
            mViewAnimator.setDisplayedChild(0);
            presenter = new FavoritesPresenter(this, (HomeActivity) getActivity());
            presenter.getFavorites();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFavoriteRecyclerView(ArrayList<PlaceModel> mPlaces) {
        try {
            this.places = mPlaces;
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            mRV.setLayoutManager(manager);
            adapter = new FavoritesRecAdapter(getContext(), places);
            mRV.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReadFavorites(ArrayList<PlaceModel> places) {
        try {
            if (getActivity() != null && getActivity() instanceof HomeActivity) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (places != null) {
                               mViewAnimator.setDisplayedChild(2);
                                initFavoriteRecyclerView(places);
                            } else {

                                mViewAnimator.setDisplayedChild(1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRemoveFavorite(String placeID) {
        try {
            int index = -1;

            for (int i = 0; i < places.size(); i++) {
                if (places.get(i).getId().matches(placeID)) {
                    index = i;
                    break;
                }
            }
            if (index != -1)
                places.remove(index);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}