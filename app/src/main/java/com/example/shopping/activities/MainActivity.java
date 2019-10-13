package com.example.shopping.activities;

import android.os.Bundle;
import android.view.MenuItem;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private DrawerLayout drawer;
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
        navDrawerConfig();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
            }
        });
        navView.getMenu().getItem(0).setActionView(R.layout.menu_arrow);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupWithNavController(navView, navController);

    }

    private void navDrawerConfig() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawer , toolbar ,
                R.string.open_drawer , R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
            }
        });
    }
    private void setFragments(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }
}