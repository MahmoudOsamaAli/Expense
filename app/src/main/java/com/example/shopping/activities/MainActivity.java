package com.example.shopping.activities;

import android.os.Bundle;

import com.example.shopping.R;
import com.example.shopping.fragments.AboutUsFragment;
import com.example.shopping.fragments.AddProjectFragment;
import com.example.shopping.fragments.ContactUsFragment;
import com.example.shopping.fragments.FavoriteFragment;
import com.example.shopping.fragments.HomeFragment;
import com.example.shopping.fragments.NotificationsFragment;
import com.example.shopping.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @BindView(R.id.container)
    DrawerLayout drawer;
    @BindView(R.id.nav_drawer_view)
    NavigationView navigationView;
    @BindView(R.id.bottom_nav_view)
    BottomNavigationView navView;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            setFragments(new HomeFragment(), AnimationStates.BOTTOM_TO_TOP);
        }
        init();
    }

    public enum AnimationStates {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT, BOTTOM_TO_TOP
    }

    private void init() {
        ButterKnife.bind(this);
        navDrawerConfig();
        bottomNavConfig();
    }

    private void bottomNavConfig() {
        navView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    setFragments(new HomeFragment(), AnimationStates.LEFT_TO_RIGHT);
                    break;
                case R.id.navigation_notifications:
                    if (fragment instanceof HomeFragment)
                        setFragments(new NotificationsFragment(), AnimationStates.RIGHT_TO_LEFT);
                    else setFragments(new NotificationsFragment(), AnimationStates.LEFT_TO_RIGHT);
                    break;
                case R.id.navigation_favorite:
                    if (fragment instanceof ProfileFragment)
                        setFragments(new FavoriteFragment(), AnimationStates.LEFT_TO_RIGHT);
                    else setFragments(new FavoriteFragment(), AnimationStates.RIGHT_TO_LEFT);
                    break;
                case R.id.navigation_profile:
                    setFragments(new ProfileFragment(), AnimationStates.RIGHT_TO_LEFT);
                    break;
            }
            return true;
        });
    }


    private void navDrawerConfig() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            AnimationStates states = AnimationStates.BOTTOM_TO_TOP;
            switch (menuItem.getItemId()) {
                case R.id.add_project_nav:
                    setFragments(new AddProjectFragment(), states);
                    break;
                case R.id.about_us_nav:
                    setFragments(new AboutUsFragment(), states);
                    break;
                case R.id.contact_us_nav:
                    setFragments(new ContactUsFragment(), states);
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setFragments(Fragment fragment, AnimationStates state) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (state == AnimationStates.RIGHT_TO_LEFT)
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);

        else if (state == AnimationStates.LEFT_TO_RIGHT)
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);

        else if (state == AnimationStates.BOTTOM_TO_TOP)
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);

        transaction.replace(R.id.fragment_container, fragment, TAG_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment instanceof HomeFragment) {
            finish();
        } else {
            if (fragment instanceof AboutUsFragment || fragment instanceof ContactUsFragment
                    || fragment instanceof AddProjectFragment) {
                navView.setSelectedItemId(navView.getSelectedItemId());
            } else if (fragment instanceof FavoriteFragment || fragment instanceof NotificationsFragment
                    || fragment instanceof ProfileFragment) {
                navView.setSelectedItemId(R.id.navigation_home);
            }
        }
    }
}