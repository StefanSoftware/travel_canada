package com.allytours.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.utilities.ExceptionHandler;
import com.allytours.controller.Helpers.InitHelper;
import com.allytours.utilities.GPSTracker;
import com.allytours.utilities.TimeUtility;
import com.allytours.utilities.TimeZoneHelper;
import com.allytours.utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.controller.fragment.AboutFragment;
import com.allytours.controller.fragment.ContactUsFragment;
import com.allytours.controller.fragment.MapFragment;
import com.allytours.controller.fragment.ProfileFragment;
import com.allytours.controller.fragment.PromotionFragment;
import com.allytours.controller.fragment.customer.CustomerToursFragment;
import com.allytours.controller.fragment.NavigationMenuFragment;
import com.allytours.controller.fragment.operator.OperatorToursFragment;
import com.allytours.controller.fragment.operator.TourListingFragment;

public class HomeActivity extends AppCompatActivity {

    public static DrawerLayout mDrawerLayout;
    public static FragmentManager fragmentManager;
    private Toolbar toolbar;
    private TextView tvTitle;
    private MenuItem searchItem;
    private MenuItem newTourItem;
    private NavigationMenuFragment navigationMenuFragment;
    private MapFragment mainFragment;
    private Context mContext;

    public static int currentFragmentNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ///set exception handler
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        initVariable();
        initUI();
        getTimeZoneOffset();
    }
    private void initVariable() {
        mContext = this;
    }
    private void initUI() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton btnMenu = (ImageButton)toolbar.findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        tvTitle = (TextView)toolbar.findViewById(R.id.tv_home_title);
        setTitle("Allytours");


        //////////////navigation view
        fragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawerlayout);
        navigationMenuFragment = new NavigationMenuFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.main_menu_container, navigationMenuFragment)
                .commit();

        mainFragment = new MapFragment();

        toolbar.setVisibility(View.VISIBLE);
        if (Utils.getFromPreference(mContext, Constant.USER_TYPE).equals(Constant.USER_TYPE_OPERATOR)) {
            currentFragmentNumber = 1;
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new OperatorToursFragment())
                    .commit();
        } else {
            currentFragmentNumber = 2;
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, mainFragment)
                    .commit();
        }

        
    }
    private void getTimeZoneOffset() {
        GPSTracker gpsTracker = new GPSTracker(mContext);
        String currentTime = TimeUtility.getCurrentTimeStamp();
        TimeZoneHelper timeZoneHelper = new TimeZoneHelper(mContext, String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()), currentTime);
        timeZoneHelper.getTimeZone();
    }

    public void setTitle(String strTitle) {
        tvTitle.setText(strTitle);

    }
    public android.app.FragmentManager getFragmentManager() {
        return getFragmentManager();
    }
     
    public void showSearchView(boolean flag) {
        if (flag) {
            searchItem.setVisible(true);
        } else {
            searchItem.setVisible(false);
        }
    }
    public void navigationTo(int num) {
        switch (num) {

            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new CustomerToursFragment(), "")
                        .commit();
                currentFragmentNumber = 0;
                showSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new OperatorToursFragment(), "")
                        .commit();
                currentFragmentNumber = 1;
                showSearchView(false);
                newTourItem.setVisible(false);
                break;

            case 2:
                mainFragment = new MapFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mainFragment, "")
                        .commit();
                currentFragmentNumber = 2;
                showSearchView(true);
                newTourItem.setVisible(false);
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new TourListingFragment(), "")
                        .commit();
                currentFragmentNumber = 3;
                showSearchView(false);
                newTourItem.setVisible(true);
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new PromotionFragment(), "")
                        .commit();
                currentFragmentNumber = 4;
                showSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new ContactUsFragment(), "")
                        .commit();
                currentFragmentNumber = 5;
                showSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 6:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new AboutFragment(), "")
                        .commit();
                currentFragmentNumber = 6;
                showSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 7:
                signout();
                break;
            case 10:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment(), "")
                        .commit();
                currentFragmentNumber = 10;
                showSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 11:
                startActivityForResult(new Intent(this, SigninActivity.class), SIGN_IN);
                break;
        }
        mDrawerLayout.closeDrawers();
    }
    private static int SIGN_IN = 103;
    private static int BUY_TOUR = 101;
    private void signout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getResources().getString(R.string.app_name));
        builder.setMessage("Do you want sign out?");
        builder.setCancelable(true);
        builder.setPositiveButton("Sign Out",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        InitHelper.initPreference(mContext);

                        Utils.setOnPreference(HomeActivity.this, Constant.FB_ACCESS_TOKEN, "");
//                        Utils.setOnPreference(HomeActivity.this, Constant.FB_PHOTO, "");
                        Utils.setOnPreference(HomeActivity.this, Constant.FB_EMAIL, "");
//                        Utils.setOnPreference(HomeActivity.this, Constant.FB_NAME, "");
//                        Utils.setOnPreference(HomeActivity.this, Constant.FB_ID, "");

                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                        finish();
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN) {
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(this, HomeActivity.class));
                this.finish();
            }
        }
        if (requestCode == BUY_TOUR) {
            if (resultCode == 100) {
                startActivity(new Intent(this, HomeActivity.class));
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        searchItem = menu.findItem(R.id.action_search);
        newTourItem = menu.findItem(R.id.add_new_tour);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                mainFragment.search(query);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    mainFragment.search("");

                }
                return false;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mainFragment.search("");

                return true;
            }
        });
        if (Utils.getFromPreference(mContext, Constant.USER_TYPE).equals(Constant.USER_TYPE_OPERATOR)) {
            searchItem.setVisible(false);
        } else {
            searchItem.setVisible(true);
        }

        newTourItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.add_new_tour) {
            startActivity(new Intent(this, AddNewTourActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
