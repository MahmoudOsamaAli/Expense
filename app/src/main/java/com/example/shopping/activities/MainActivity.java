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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @BindView(R.id.container)
    DrawerLayout drawer;
    @BindView(R.id.nav_drawer_view)
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            setFragments(new HomeFragment());
        }
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        navDrawerConfig();
        bottomNavConfig();
    }

    private void bottomNavConfig() {
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        navView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.navigation_home :
                    setFragments(new HomeFragment());
                    break;
                case R.id.navigation_favorite :
                    setFragments(new FavoriteFragment());
                    break;
                case R.id.navigation_profile :
                    setFragments(new ProfileFragment());
                    break;
                case R.id.navigation_notifications :
                    setFragments(new NotificationsFragment());
                    break;
            }
            return true;
        });
    }

    private void navDrawerConfig() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawer , toolbar ,
                R.string.open_drawer , R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.add_project_nav :
                    setFragments(new AddProjectFragment());
                    break;
                case R.id.about_us_nav :
                    setFragments(new AboutUsFragment());
                    break;
                case R.id.contact_us_nav :
                    setFragments(new ContactUsFragment());
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }
    private void setFragments(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }
}