package com.example.expense.view.activities.Home;

import android.app.SearchManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.expense.R;
import com.example.expense.Utilities.AppUtils;
import com.example.expense.Utilities.PrefManager;
import com.example.expense.pojo.Model.LocationModel;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.view.fragments.aboutUs.AboutUsFragment;
import com.example.expense.view.fragments.addProject.AddProjectFragment;
import com.example.expense.view.fragments.contactUs.ContactUsFragment;
import com.example.expense.view.fragments.favorites.FavoriteFragment;
import com.example.expense.view.fragments.home.HomeFragment;
import com.example.expense.view.fragments.notifications.NotificationsFragment;
import com.example.expense.view.fragments.profile.ProfileFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeActivityListener {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @BindView(R.id.container)
    DrawerLayout drawer;
    @BindView(R.id.nav_drawer_view)
    NavigationView navigationView;
    @BindView(R.id.bottom_nav_view)
    BottomNavigationView navView;

    private Fragment currFragment;
    private final static String TAG = "HomeActivity";
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    public HomeActivity mCurrent;
    private HomeActivityPresenter presenter;
    private FusedLocationProviderClient fusedLocationClient;
    public Location mCurrentLocation;

    private PrefManager mPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            Bundle extras = getIntent().getExtras();
            String m = null;
            if (extras != null) {
                m = extras.getString("message");
                //get the value based on the key
            }
            Log.i(TAG, "onCreate: intent has string " + m);
            if (savedInstanceState == null && m == null) {
                setFragments(new HomeFragment(), AnimationStates.BOTTOM_TO_TOP);
            } else if (m != null) {
                setFragments(new ProfileFragment(), AnimationStates.BOTTOM_TO_TOP);
//                navView.setSelectedItemId(R.id.navigation_profile);
            }

            initLocation();
            //calling init fun
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String loginFrom = mPrefManager.readString(PrefManager.LOGIN_FROM);

            if (loginFrom != null && loginFrom.matches("Profile")) {
                Log.i(TAG, "onResume(): login from Profile");
                mPrefManager.saveString(PrefManager.LOGIN_FROM, null);
                setFragments(new ProfileFragment(), AnimationStates.BOTTOM_TO_TOP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLocation() {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mCurrentLocation = location;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum AnimationStates {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT, BOTTOM_TO_TOP
    }

    private void init() {
        try {
            //initializing views
            ButterKnife.bind(this);

            //initializing context
            mCurrent = HomeActivity.this;
            //initializing presenter
            presenter = new HomeActivityPresenter(mCurrent, this);

            mPrefManager = new PrefManager(mCurrent);

            //setting support action bar
            setSupportActionBar(toolbar);
            //setting toolbar title
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle("Home");

            //initializing nav drawer
            navDrawerConfig();
            //initializing bottom navigation
            bottomNavConfig();

            //reading data from firebase
            presenter.readPlaceByCategoryFromFireStore();

            //reading favorites from firebase
            presenter.readFavoritePlacesFromFireStore();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bottomNavConfig() {
        try {
            navView.setOnNavigationItemSelectedListener(menuItem -> {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        try {
                            if (currFragment instanceof HomeFragment) {
                                break;
                            } else {
                                setActionBarTitle(getString(R.string.title_home));
                                setFragments(new HomeFragment(), AnimationStates.LEFT_TO_RIGHT);
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    case R.id.navigation_notifications:
                        try {
                            if (currFragment instanceof NotificationsFragment) break;
                            if (fragment instanceof HomeFragment) {
                                setActionBarTitle(getString(R.string.title_notifications));
                                setFragments(new NotificationsFragment(), AnimationStates.RIGHT_TO_LEFT);
                            } else {
                                setActionBarTitle(getString(R.string.title_notifications));
                                setFragments(new NotificationsFragment(), AnimationStates.LEFT_TO_RIGHT);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.navigation_favorite:
                        try {
                            if (currFragment instanceof FavoriteFragment) break;
                            if (fragment instanceof ProfileFragment) {
                                setActionBarTitle(getString(R.string.title_favorite));
                                setFragments(new FavoriteFragment(), AnimationStates.LEFT_TO_RIGHT);
                            } else {
                                setActionBarTitle(getString(R.string.title_favorite));
                                setFragments(new FavoriteFragment(), AnimationStates.RIGHT_TO_LEFT);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.navigation_profile:
                        try {
                            if (currFragment instanceof ProfileFragment) break;
                            setActionBarTitle(getString(R.string.title_profile));
                            setFragments(new ProfileFragment(), AnimationStates.RIGHT_TO_LEFT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return true;
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void navDrawerConfig() {
        try {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.open_drawer, R.string.close_drawer);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            drawer.setScrimColor(getResources().getColor(R.color.white));

            navigationView.setNavigationItemSelectedListener(menuItem -> {
                AnimationStates states = AnimationStates.BOTTOM_TO_TOP;
                switch (menuItem.getItemId()) {

                    case R.id.add_project_nav:// Add Project
                        if (currFragment instanceof AddProjectFragment) break;
                        setActionBarTitle(getString(R.string.add_project));
                        setFragments(new AddProjectFragment(), states);
                        menuItem.setChecked(true);
                        break;

                    case R.id.about_us_nav:// Place
                        setActionBarTitle(getString(R.string.about_us));
                        if (currFragment instanceof AboutUsFragment) break;
                        setFragments(new AboutUsFragment(), states);
                        menuItem.setChecked(true);
                        break;

                    case R.id.contact_us_nav:
                        setActionBarTitle(getString(R.string.contact_us));
                        if (currFragment instanceof ContactUsFragment) break;
                        setFragments(new ContactUsFragment(), states);
                        menuItem.setChecked(true);
                        break;

                    case R.id.sign_out:
                        FirebaseAuth.getInstance().signOut();
                        if (currFragment instanceof HomeFragment) break;
//                        setFragments(new HomeFragment(), states);
                        navView.setSelectedItemId(R.id.navigation_home);
                        navView.setSelected(true);
                        break;

                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            });

            //These lines should be added in the OnCreate() of your main activity
            //requestsCount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
            //findItem(R.id.requests_nav));
            //This method will initialize the count value
            //initializeRequestCountDrawer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(title);

        }
    }

    public void setFragments(Fragment fragment, AnimationStates state) {
        try {
            currFragment = fragment;
            Log.i(TAG, "setFragments: current fragment = " + currFragment.getClass().getCanonicalName());
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
            Log.i(TAG, "setFragments: fragment tag = " + TAG_FRAGMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ------------------------------------Activity Overrides-----------------------------------
     **/
    @Override
    public void onBackPressed() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.options_menu, menu);

            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(
                    Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * ------------------------------------Callbacks-----------------------------------------
     **/
    @Override
    public void readPlacesByCategoryFromFirebase(ArrayList<PlaceModel> data) {
        try {
            Log.i(TAG, "readPlacesByCategoryFromFirebase(): is called");
            if (data != null && !data.isEmpty()) {
                Log.i(TAG, "readPlacesByCategoryFromFirebase(): places size = " + data.size());

                for (PlaceModel place : data) {
                    String result = "---------------------------------\n";
                    result += "id = " + place.getId() + " name = " + place.getName() + " phone = " + place.getPhoneNumber() + " \n";
                    result += "description = " + place.getDescription() + " facebook = " + place.getFacebookUrl() + " twitter = " + place.getTwitterUrl() + " \n";
                    result += "website = " + place.getWebsiteUrl() + " \n";
                    result += "\nLocations\n";

                    for (LocationModel locationModel : place.getLocationModels()) {
                        result += "Country = " + locationModel.getCountry() + " city = " + locationModel.getCity() + " Street = " + locationModel.getStreet();
                        result += " Latitude = " + locationModel.getLatitude() + " Longitude = " + locationModel.getLongitude() + "\n";
                    }
                    result += "\nImages\n";

                    for (String image : place.getImagesURL()) {
                        result += "place ID = " + place.getId() + " url = " + image + "\n";
                    }
                    result += "---------------------------------\n";
                    Log.i(TAG, "onReadPlaceByCategory(): " + result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayConnectionError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    AppUtils.showAlertDialog(mCurrent, getString(R.string.check_network_connection));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}